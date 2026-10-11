package dev.kuromiichi.init.data.lessons.contents

import dev.kuromiichi.init.data.lessons.LessonStepContent
import dev.kuromiichi.init.data.preferences.TargetOperatingSystem

internal data class DevelopmentEnvironmentPlatformContent(
    val jdkInstallation: LessonStepContent,
    val intellijInstallation: LessonStepContent,
    val gitInstallation: LessonStepContent,
)

internal val developmentEnvironmentPlatformContent =
    mapOf(
        TargetOperatingSystem.Windows to windowsDevelopmentEnvironmentContent,
        TargetOperatingSystem.MacOS to macOsDevelopmentEnvironmentContent,
        TargetOperatingSystem.Linux to linuxDevelopmentEnvironmentContent,
    )
