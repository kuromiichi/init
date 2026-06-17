package dev.kuromiichi.damprep.ui.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.kuromiichi.damprep.data.preferences.PronounPreference
import dev.kuromiichi.damprep.ui.theme.ThemePreference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    themePreference: ThemePreference,
    onThemePreferenceChanged: (ThemePreference) -> Unit,
    onFinished: (studentName: String, pronounPreference: PronounPreference) -> Unit,
) {
    var currentStepIndex by remember { mutableIntStateOf(0) }
    var studentName by remember { mutableStateOf("") }
    var pronounPreference by remember { mutableStateOf(PronounPreference.Neutral) }

    val currentStep = onboardingSteps[currentStepIndex]
    val isLastStep = currentStepIndex == onboardingSteps.lastIndex
    val isPersonalizationStep = currentStep.kind == OnboardingStepKind.Personalization
    val normalizedName = studentName.trim()
    val canContinue = !isPersonalizationStep || normalizedName.isNotEmpty()

    fun continueOnboarding() {
        if (!canContinue) {
            return
        }

        if (isLastStep) {
            onFinished(normalizedName, pronounPreference)
        } else {
            currentStepIndex += 1
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .widthIn(max = 720.dp)
                    .fillMaxWidth()
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 260,
                            easing = FastOutSlowInEasing,
                        ),
                    ),
                verticalArrangement = Arrangement.Center,
            ) {
                AnimatedContent(
                    targetState = currentStepIndex,
                    transitionSpec = {
                        val direction = if (targetState > initialState) 1 else -1

                        (
                            fadeIn(animationSpec = tween(durationMillis = 220)) +
                                slideInVertically(
                                    animationSpec = tween(
                                        durationMillis = 260,
                                        easing = FastOutSlowInEasing,
                                    ),
                                    initialOffsetY = { it / 10 * direction },
                                )
                            ).togetherWith(
                            fadeOut(animationSpec = tween(durationMillis = 160)) +
                                slideOutVertically(
                                    animationSpec = tween(
                                        durationMillis = 180,
                                        easing = FastOutSlowInEasing,
                                    ),
                                    targetOffsetY = { -it / 10 * direction },
                                ),
                        ).using(
                            SizeTransform(clip = false),
                        )
                    },
                    label = "Onboarding step",
                ) { animatedStepIndex ->
                    val animatedStep = onboardingSteps[animatedStepIndex]

                    OnboardingStepContent(
                        step = animatedStep,
                        themePreference = themePreference,
                        pronounPreference = pronounPreference,
                        studentName = studentName,
                        onThemePreferenceChanged = onThemePreferenceChanged,
                        onPronounPreferenceChanged = { pronounPreference = it },
                        onStudentNameChanged = { studentName = it },
                        onDone = {
                            continueOnboarding()
                        },
                    )
                }
            }

            Row(
                modifier = Modifier
                    .widthIn(max = 720.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (currentStepIndex > 0) {
                    TextButton(
                        onClick = {
                            currentStepIndex -= 1
                        },
                    ) {
                        Text("Atrás")
                    }
                } else {
                    Spacer(modifier = Modifier)
                }

                Button(
                    onClick = {
                        continueOnboarding()
                    },
                    enabled = canContinue,
                ) {
                    Text(if (isLastStep) "Entrar" else "Siguiente")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            OnboardingProgressDots(currentStepIndex = currentStepIndex)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OnboardingStepContent(
    step: OnboardingStep,
    themePreference: ThemePreference,
    pronounPreference: PronounPreference,
    studentName: String,
    onThemePreferenceChanged: (ThemePreference) -> Unit,
    onPronounPreferenceChanged: (PronounPreference) -> Unit,
    onStudentNameChanged: (String) -> Unit,
    onDone: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = step.title,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = step.body,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        AnimatedVisibility(visible = step.kind == OnboardingStepKind.Theme) {
            Column {
                Spacer(modifier = Modifier.height(28.dp))
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    ThemePreference.entries.forEachIndexed { index, preference ->
                        SegmentedButton(
                            selected = preference == themePreference,
                            onClick = {
                                onThemePreferenceChanged(preference)
                            },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = ThemePreference.entries.size,
                            ),
                        ) {
                            Text(preference.label)
                        }
                    }
                }
            }
        }

        AnimatedVisibility(visible = step.kind == OnboardingStepKind.Personalization) {
            Column {
                Spacer(modifier = Modifier.height(28.dp))
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    PronounPreference.entries.forEachIndexed { index, preference ->
                        SegmentedButton(
                            selected = preference == pronounPreference,
                            onClick = {
                                onPronounPreferenceChanged(preference)
                            },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = PronounPreference.entries.size,
                            ),
                        ) {
                            Text(preference.label)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))
                OutlinedTextField(
                    value = studentName,
                    onValueChange = onStudentNameChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nombre") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onDone()
                        },
                    ),
                )
            }
        }
    }
}

@Composable
private fun OnboardingProgressDots(currentStepIndex: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        onboardingSteps.forEachIndexed { index, _ ->
            val isCurrent = index == currentStepIndex
            val dotColor by animateColorAsState(
                targetValue = if (isCurrent) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                },
                animationSpec = tween(durationMillis = 240),
                label = "Onboarding dot color",
            )
            val dotWidth by animateDpAsState(
                targetValue = if (isCurrent) 24.dp else 8.dp,
                animationSpec = tween(
                    durationMillis = 260,
                    easing = FastOutSlowInEasing,
                ),
                label = "Onboarding dot width",
            )

            Box(
                modifier = Modifier
                    .size(height = 8.dp, width = dotWidth)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(dotColor),
            )
        }
    }
}
