"""Genera el icono de init y las imágenes de los instaladores con la paleta Catppuccin.

Uso: python3 packaging/generate_assets.py <carpeta con los TTF de JetBrains Mono>

La carpeta es `fonts/ttf` de la release oficial de JetBrains Mono (https://github.com/JetBrains/JetBrainsMono).
Requiere Pillow. Los archivos generados se versionan: el empaquetado no ejecuta este script.
"""

import sys
from pathlib import Path

from PIL import Image, ImageDraw, ImageFont

# Catppuccin Macchiato: el icono y el DMG no cambian con el tema del sistema.
BASE = (0x24, 0x27, 0x3A)
MANTLE = (0x1E, 0x20, 0x30)
SURFACE0 = (0x36, 0x3A, 0x4F)
OVERLAY0 = (0x6E, 0x73, 0x8D)
TEXT = (0xCA, 0xD3, 0xF5)
MAUVE = (0xC6, 0xA0, 0xF6)
# Catppuccin Latte: el asistente del MSI pinta su texto en negro, así que la zona de texto va en claro.
LATTE_BASE = (0xEF, 0xF1, 0xF5)
LATTE_MANTLE = (0xE6, 0xE9, 0xEF)

# Se dibuja a varias veces el tamaño final y se reduce: bordes suaves sin depender del antialiasing de Pillow.
SUPERSAMPLING = 4

PACKAGING = Path(__file__).resolve().parent


def font(font_dir: Path, weight: str, size: float) -> ImageFont.FreeTypeFont:
    return ImageFont.truetype(str(font_dir / f"JetBrainsMono-{weight}.ttf"), round(size))


def vertical_gradient(size: tuple[int, int], top: tuple, bottom: tuple) -> Image.Image:
    width, height = size
    gradient = Image.new("RGBA", (1, height))
    for y in range(height):
        t = y / max(height - 1, 1)
        gradient.putpixel((0, y), tuple(round(a + (b - a) * t) for a, b in zip(top, bottom)) + (255,))
    return gradient.resize(size)


def draw_centered(draw: ImageDraw.ImageDraw, center: tuple[float, float], text: str, text_font, fill) -> None:
    """Centra el texto por su caja visible, no por la métrica de la línea."""
    left, top, right, bottom = draw.textbbox((0, 0), text, font=text_font)
    draw.text((center[0] - (left + right) / 2, center[1] - (top + bottom) / 2), text, font=text_font, fill=fill)


def wordmark(font_dir: Path, size: float, text_color, prompt_color) -> Image.Image:
    """«> init» en JetBrains Mono, con el `>` del prompt en mauve. Devuelve la imagen recortada a su contenido."""
    text_font = font(font_dir, "Bold", size)
    probe = ImageDraw.Draw(Image.new("RGBA", (1, 1)))
    prompt, gap, name = ">", " ", "init"
    width = probe.textlength(prompt + gap + name, font=text_font)
    canvas = Image.new("RGBA", (round(width + size), round(size * 2)), (0, 0, 0, 0))
    draw = ImageDraw.Draw(canvas)
    origin = (size / 2, size / 2)
    draw.text(origin, prompt, font=text_font, fill=prompt_color)
    draw.text((origin[0] + probe.textlength(prompt + gap, font=text_font), origin[1]), name, font=text_font, fill=text_color)
    return canvas.crop(canvas.getbbox())


def icon(font_dir: Path, size: int, inset: float) -> Image.Image:
    """Cuadrado redondeado Macchiato con el `>` en mauve. `inset` deja el margen que pide cada sistema."""
    canvas = size * SUPERSAMPLING
    margin = round(canvas * inset)
    side = canvas - 2 * margin
    image = Image.new("RGBA", (canvas, canvas), (0, 0, 0, 0))

    mask = Image.new("L", (side, side), 0)
    ImageDraw.Draw(mask).rounded_rectangle((0, 0, side - 1, side - 1), round(side * 0.225), fill=255)
    body = vertical_gradient((side, side), BASE, MANTLE)
    ImageDraw.Draw(body).rounded_rectangle(
        (0, 0, side - 1, side - 1), round(side * 0.225), outline=SURFACE0, width=max(round(side * 0.008), 1)
    )
    image.paste(body, (margin, margin), mask)

    draw = ImageDraw.Draw(image)
    draw_centered(draw, (canvas / 2, canvas / 2), ">", font(font_dir, "ExtraBold", side * 0.92), MAUVE)
    return image.resize((size, size), Image.LANCZOS)


def write_icons(font_dir: Path) -> None:
    out = PACKAGING / "icons"
    out.mkdir(exist_ok=True)
    # macOS dibuja el icono tal cual: la cuadrícula de Apple deja 100 px de margen sobre 1024.
    icon(font_dir, 1024, 100 / 1024).save(out / "init.icns")
    # Windows y Linux muestran el icono casi a sangre; a 16 px el margen solo restaría nitidez.
    full = {size: icon(font_dir, size, 0.03 if size >= 48 else 0.0) for size in (16, 24, 32, 48, 64, 128, 256)}
    full[256].save(out / "init.ico", sizes=[(s, s) for s in full], append_images=list(full.values()))
    icon(font_dir, 512, 0.03).save(out / "init.png")
    # Icono de la ventana (barra de tareas de Windows y Linux, también al ejecutar desde Gradle).
    icon(font_dir, 256, 0.03).save(PACKAGING.parent / "src" / "main" / "composeResources" / "drawable" / "app_icon.png")


def write_dmg_background(font_dir: Path) -> None:
    """Fondo de la ventana del DMG (660 × 400 puntos), a 144 ppp para pantallas Retina.

    Las posiciones coinciden con `macos/init-dmg-setup.scpt`: la app en (180, 250) y Aplicaciones en (480, 250).
    """
    scale = 2
    width, height = 660 * scale, 400 * scale
    background = vertical_gradient((width, height), BASE, MANTLE)

    mark = wordmark(font_dir, 46 * scale, TEXT, MAUVE)
    background.alpha_composite(mark, ((width - mark.width) // 2, round(105 * scale - mark.height / 2)))

    # Flecha de la app a Aplicaciones con el mismo `>` del logo, de tenue a mauve.
    draw = ImageDraw.Draw(background)
    chevron = font(font_dir, "ExtraBold", 30 * scale)
    steps = [(-36, OVERLAY0), (0, tuple(round((a + b) / 2) for a, b in zip(OVERLAY0, MAUVE))), (36, MAUVE)]
    for offset, color in steps:
        draw_centered(draw, ((330 + offset) * scale, 250 * scale), ">", chevron, color)

    (PACKAGING / "macos").mkdir(exist_ok=True)
    background.convert("RGB").save(PACKAGING / "macos" / "init-background.tiff", dpi=(144, 144), compression="tiff_lzw")


def write_msi_bitmaps(font_dir: Path) -> None:
    """Imágenes del asistente de WiX: banner superior (493 × 58) y lateral de bienvenida y final (493 × 312)."""
    out = PACKAGING / "windows"
    out.mkdir(exist_ok=True)

    banner = vertical_gradient((493, 58), LATTE_BASE, LATTE_MANTLE)
    badge = icon(font_dir, 44, 0.0)
    banner.alpha_composite(badge, (493 - 44 - 7, 7))
    banner.convert("RGB").save(out / "banner.bmp")

    # WiX escribe el título y el texto desde x ≈ 180 px: el panel de la izquierda es para el logo.
    dialog = vertical_gradient((493, 312), LATTE_BASE, LATTE_MANTLE)
    panel_width = 164
    dialog.paste(vertical_gradient((panel_width, 312), BASE, MANTLE), (0, 0))
    big = SUPERSAMPLING
    mark = wordmark(font_dir, 30 * big, TEXT, MAUVE)
    mark = mark.resize((mark.width // big, mark.height // big), Image.LANCZOS)
    dialog.alpha_composite(mark, ((panel_width - mark.width) // 2, 118 - mark.height // 2))
    dialog.convert("RGB").save(out / "dialog.bmp")


def main() -> None:
    if len(sys.argv) != 2:
        sys.exit(__doc__)
    font_dir = Path(sys.argv[1])
    write_icons(font_dir)
    write_dmg_background(font_dir)
    write_msi_bitmaps(font_dir)


if __name__ == "__main__":
    main()
