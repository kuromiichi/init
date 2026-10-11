package dev.kuromiichi.init.data.preferences

enum class PronounPreference(
    val label: String,
    val welcomeWord: String,
) {
    Masculine("Masculino", "Bienvenido"),
    Feminine("Femenino", "Bienvenida"),
    Neutral("Neutro", "Bienvenide"),
}
