package dev.kuromiichi.damprep.ui.onboarding

data class OnboardingStep(
    val kind: OnboardingStepKind = OnboardingStepKind.Info,
    val title: String,
    val body: String,
)

enum class OnboardingStepKind {
    Info,
    Theme,
    Pronouns,
    Name,
}

val onboardingSteps = listOf(
    OnboardingStep(
        title = "Te doy la bienvenida a DAM Prep",
        body = "Esta app te acompañará mientras aprendes los fundamentos de programación desde cero, paso a paso y con calma.",
    ),
    OnboardingStep(
        title = "Teoría guiada por pantallas",
        body = "Los contenidos estarán organizados por temas y secciones breves. La idea es que puedas avanzar sin abrir apuntes ni manejar archivos directamente.",
    ),
    OnboardingStep(
        title = "Avance progresivo",
        body = "Más adelante, cada tema podrá terminar con ejercicios para desbloquear el siguiente. La app podrá revisar entregas, compilar proyectos y darte feedback.",
    ),
    OnboardingStep(
        kind = OnboardingStepKind.Theme,
        title = "Elige cómo quieres ver la app",
        body = "Puedes usar un tema claro, uno oscuro o dejar que DAM Prep siga la configuración de tu sistema.",
    ),
    OnboardingStep(
        kind = OnboardingStepKind.Pronouns,
        title = "Elige cómo quieres que te hable",
        body = "Usaré esta preferencia para adaptar algunos textos y dirigirme a ti de forma cómoda.",
    ),
    OnboardingStep(
        kind = OnboardingStepKind.Name,
        title = "Antes de empezar",
        body = "Dime cómo quieres que te llame durante esta sesión.",
    ),
)
