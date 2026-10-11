---
name: publicar-version
description: Publicar una versión de init - elegir el número SemVer 0.x, commits previos, tag anotado, push que dispara el workflow de releases, comprobación de la release y recuperación si falla. Usar al preparar una release o crear un tag.
---

# Publicar una versión

Subir un tag `vMAJOR.MINOR.PATCH` lanza `.github/workflows/release.yml`: verifica, empaqueta en cuatro runners y publica la release que la app instalada descarga sola. Un tag subido llega a todos los usuarios, así que **pedir confirmación antes de cada push**.

## 1. Elegir la versión

- Formato estricto `vMAJOR.MINOR.PATCH`, sin sufijos: el workflow rechaza `-beta.1` y similares (el MSI repetiría versión con la final).
- Etapa beta (mayor 0): una lección nueva desde el último tag → subir MINOR (`v0.2.0`); cualquier otro cambio, `feat` incluido → subir PATCH (`v0.2.1`). `v1.0.0` solo cuando el usuario lo decida; entonces se revisa esta regla.
- Último tag y cambios desde él:

```sh
git describe --tags --abbrev=0
git log --oneline $(git describe --tags --abbrev=0)..HEAD
```

Si no hay ningún `feat` ni `fix`, la release saldrá con «Correcciones y mejoras internas»; valorar si merece la pena publicar.

## 2. Antes del tag

- Rama `main` y árbol limpio salvo archivos ajenos (`.idea/` no se incluye en commits).
- Build limpia con la skill `verificacion` (`./gradlew --stop` y `./gradlew --no-daemon clean build`).
- Commits en Conventional Commits en español: las notas de la release se generan de los asuntos `feat` y `fix`, sin el prefijo y con la primera letra en mayúscula, así que el asunto debe leerse bien como frase para el alumno.
- README al día si cambian funciones visibles, número de lecciones con contenido o instrucciones de instalación.

## 3. Tag y push

```sh
git tag -a vX.Y.Z -m "init X.Y.Z"
git push origin main
git push origin vX.Y.Z
```

El tag va en el último commit de `main` ya subido. No usar `git push --tags` (sube cualquier tag local olvidado).

## 4. Comprobar la release

- Workflow `Release` en GitHub Actions: jobs `verify`, cuatro `package` y `publish`.
- La release debe tener cinco archivos: `init-X.Y.Z-macos-arm64.dmg`, `-macos-x64.dmg`, `-windows-x64.msi`, `-linux-x64.deb` y `SHA256SUMS`. Sin `SHA256SUMS` la app se niega a instalar.
- Si hay una versión anterior instalada, abrirla: la portada debe mostrar el botón de actualizar y el diálogo, la versión nueva.

## 5. Si algo falla

- **El workflow falla antes de `publish`** (no hay release): corregir con un commit, borrar el tag y volver a crearlo en el commit nuevo. Pedir confirmación para borrar el tag remoto:

```sh
git tag -d vX.Y.Z
git push origin :refs/tags/vX.Y.Z
```

- **La release ya está publicada**: no mover ni reutilizar el tag; las apps pueden haberla descargado. Publicar la corrección como el siguiente PATCH.
