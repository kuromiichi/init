package dev.kuromiichi.damprep.ui.navigation

import kotlinx.serialization.Serializable

sealed interface AppRoute {
    @Serializable
    data object Onboarding : AppRoute

    @Serializable
    data object Home : AppRoute

    @Serializable
    data class Lesson(
        val lessonId: String,
        val resumeFromSavedStep: Boolean = false,
    ) : AppRoute

    @Serializable
    data object Lessons : AppRoute

    @Serializable
    data object Notes : AppRoute

    @Serializable
    data object Settings : AppRoute
}
