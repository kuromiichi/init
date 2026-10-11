package dev.kuromiichi.damprep.ui.lessons

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/** Las dos zonas de un paso que se desplazan por separado en el diseño ancho. */
internal enum class LessonScrollSection { Text, Visual }

/** Cómo se desplaza un paso: entero, en dos columnas independientes o entero con el visual desplazable por dentro. */
internal enum class LessonStepScrollLayout { Page, Split, PageWithVisual }

/**
 * Zona que recibe la siguiente pulsación de Tab (o de Mayús+Tab) entre las que se pueden desplazar. Las recorre en
 * bucle: con una sola se queda en ella y sin ninguna no hay nada que enfocar.
 */
internal fun cycleLessonScrollSection(
    sections: List<LessonScrollSection>,
    current: LessonScrollSection?,
    backwards: Boolean,
): LessonScrollSection? {
    if (sections.isEmpty()) return null
    val index = sections.indexOf(current)
    return when {
        index < 0 -> if (backwards) sections.last() else sections.first()
        backwards -> sections[(index - 1 + sections.size) % sections.size]
        else -> sections[(index + 1) % sections.size]
    }
}

/**
 * Une el paso que se muestra con las teclas que lo manejan: el contenido registra sus zonas desplazables y la acción
 * de pedir una pista, y la pantalla, que es quien recibe las teclas, decide cuál se mueve y cuál se resalta.
 */
@Stable
internal class LessonStepScrollController {
    private var owner: Any? = null
    private var text: ScrollState? = null
    private var visual: ScrollState? = null
    private var layout = LessonStepScrollLayout.Page
    private var hintOwner: Any? = null
    private var hintAction: (() -> Boolean)? = null

    /** Zona resaltada, o `null` mientras no se ha usado el teclado en el paso actual. */
    var focusedSection: LessonScrollSection? by mutableStateOf(null)
        private set

    fun register(
        owner: Any,
        text: ScrollState,
        visual: ScrollState,
    ) {
        this.owner = owner
        this.text = text
        this.visual = visual
        layout = LessonStepScrollLayout.Page
        focusedSection = null
    }

    fun unregister(owner: Any) {
        if (this.owner === owner) {
            this.owner = null
            text = null
            visual = null
            layout = LessonStepScrollLayout.Page
            focusedSection = null
        }
    }

    /** Cómo se desplaza el paso: en columnas propias en el diseño ancho, entero en el compacto o con práctica. */
    fun updateLayout(
        owner: Any,
        layout: LessonStepScrollLayout,
    ) {
        if (this.owner === owner && this.layout != layout) {
            this.layout = layout
            focusedSection = null
        }
    }

    /** La tarjeta de práctica ofrece su acción de «otra pista»; devuelve `false` cuando ya no queda ninguna por mostrar. */
    fun registerHintAction(
        owner: Any,
        action: () -> Boolean,
    ) {
        hintOwner = owner
        hintAction = action
    }

    fun unregisterHintAction(owner: Any) {
        if (hintOwner === owner) {
            hintOwner = null
            hintAction = null
        }
    }

    /** Muestra la siguiente pista de la práctica. Devuelve `false` si no hay práctica o ya están todas. */
    fun revealNextHint(): Boolean = hintAction?.invoke() ?: false

    private fun scrollableSections(): List<LessonScrollSection> =
        when (layout) {
            LessonStepScrollLayout.Page -> emptyList()
            LessonStepScrollLayout.Split ->
                listOfNotNull(
                    LessonScrollSection.Text.takeIf { text.canScroll() },
                    LessonScrollSection.Visual.takeIf { visual.canScroll() },
                )
            LessonStepScrollLayout.PageWithVisual -> listOfNotNull(LessonScrollSection.Visual.takeIf { visual.canScroll() })
        }

    /**
     * Pasa a la zona siguiente. Sin zonas desplazables no hace nada. Con la página entera y el visual desplazable,
     * alterna entre resaltar el visual y quitar el resaltado para volver a mover la página.
     */
    fun cycle(backwards: Boolean) {
        val sections = scrollableSections()
        focusedSection =
            if (layout == LessonStepScrollLayout.PageWithVisual) {
                sections.firstOrNull()?.takeIf { focusedSection == null }
            } else {
                cycleLessonScrollSection(sections, focusedSection, backwards)
            }
    }

    /**
     * La zona que se desplaza con las flechas. En los pasos con columnas independientes, la primera pulsación solo
     * resalta la zona (la primera que se pueda mover) y devuelve `null`; las siguientes la desplazan. En los pasos sin
     * columnas se mueve la página entera, sin resaltar nada, salvo que el visual esté resaltado y se pueda desplazar.
     */
    fun scrollTarget(page: ScrollState): ScrollState? {
        if (layout == LessonStepScrollLayout.Page) return page.takeIf { it.canScroll() }
        if (layout == LessonStepScrollLayout.PageWithVisual) {
            if (focusedSection == LessonScrollSection.Visual && visual.canScroll()) return visual
            focusedSection = null
            return page.takeIf { it.canScroll() }
        }
        val sections = scrollableSections()
        val section = focusedSection?.takeIf { it in sections }
        if (section == null) {
            focusedSection = sections.firstOrNull()
            return null
        }
        return when (section) {
            LessonScrollSection.Text -> text
            LessonScrollSection.Visual -> visual
        }
    }
}

/** Menos recorrido que esto (en píxeles) es un redondeo de la medida, no contenido oculto: no cuenta como desplazable. */
private const val MIN_SCROLL_RANGE = 24

private fun ScrollState?.canScroll(): Boolean = this != null && maxValue in MIN_SCROLL_RANGE until Int.MAX_VALUE
