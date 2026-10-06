package dev.kuromiichi.damprep.ui.onboarding

data class OnboardingStep(
    val kind: OnboardingStepKind = OnboardingStepKind.Info,
    val title: String,
    val body: String,
)

enum class OnboardingStepKind {
    Info,
    Import,
    Theme,
    OperatingSystem,
    Personalization,
}

val onboardingSteps =
    listOf(
        OnboardingStep(
            title = "Te doy la bienvenida a DAM Prep",
            body = "Esta app te acompañará mientras aprendes los fundamentos de programación desde cero, paso a paso y con calma.",
        ),
        OnboardingStep(
            kind = OnboardingStepKind.Import,
            title = "¿Ya usabas DAM Prep?",
            body = "Si tienes una copia de seguridad de otra instalación, impórtala y entrarás directamente con tu nombre, tus ajustes y tu progreso. Si no, pulsa «Siguiente» para empezar desde cero.",
        ),
        OnboardingStep(
            title = "Teoría guiada por pantallas",
            body = "Los contenidos estarán organizados en lecciones con pasos breves. La idea es que puedas avanzar a tu ritmo sin tener que leer apuntes aburridos.",
        ),
        OnboardingStep(
            title = "Avance progresivo",
            body = "Más adelante, cada lección podrá terminar con ejercicios para desbloquear la siguiente. La app podrá revisar entregas automáticamente o solicitar verificación externa.",
        ),
        OnboardingStep(
            kind = OnboardingStepKind.Theme,
            title = "Elige cómo quieres ver la app",
            body = "Puedes usar un tema claro, uno oscuro o dejar que DAM Prep siga la configuración de tu sistema.",
        ),
        OnboardingStep(
            kind = OnboardingStepKind.OperatingSystem,
            title = "Elige el sistema que utilizarás",
            body = "Adaptaremos las instrucciones y los comandos al sistema operativo con el que quieras trabajar. Si reconocemos el sistema actual, aparecerá marcado como sugerencia, pero debes confirmar tu elección para continuar.",
        ),
        OnboardingStep(
            kind = OnboardingStepKind.Personalization,
            title = "Antes de empezar",
            body = "Dime cómo quieres que te llame y qué tratamiento prefieres.",
        ),
        OnboardingStep(
            title = "Aquí comienza tu viaje",
            body = "Ya está todo listo para empezar a adentrarte en el mundo de la programación. ¡Mucho ánimo!",
        ),
    )
