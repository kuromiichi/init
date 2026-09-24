package dev.kuromiichi.damprep.data.lessons

/**
 * Convierte un fragmento de Kotlin en líneas coloreadas para los ejemplos de las lecciones.
 *
 * Es un resaltado léxico sencillo pensado para fragmentos cortos: reconoce comentarios, cadenas
 * con interpolación, números, palabras clave, llamadas a funciones, operadores y signos de
 * puntuación. Para marcar un error con subrayado se siguen usando los tokens manuales.
 *
 * Los ejemplos suelen escribirse como `kotlinCode($$"""...""".trimIndent())` para que `$nombre`
 * se muestre tal cual en lugar de interpolarse al compilar la app.
 */
internal fun kotlinCode(source: String): List<LessonCodeLine> {
    val lines = mutableListOf<LessonCodeLine>()
    var currentLine = mutableListOf<LessonCodeToken>()

    fun addToken(
        text: String,
        kind: LessonCodeTokenKind,
    ) {
        text.split('\n').forEachIndexed { index, part ->
            if (index > 0) {
                lines += LessonCodeLine(currentLine)
                currentLine = mutableListOf()
            }
            if (part.isNotEmpty()) currentLine.appendMerging(LessonCodeToken(part, kind))
        }
    }

    KotlinTokenPattern.findAll(source).forEach { match ->
        val text = match.value
        when {
            match.groups["comment"] != null -> addToken(text, LessonCodeTokenKind.Comment)
            match.groups["string"] != null -> text.stringTokens().forEach { (part, kind) -> addToken(part, kind) }
            match.groups["number"] != null -> addToken(text, LessonCodeTokenKind.Number)
            match.groups["identifier"] != null -> addToken(text, text.identifierKind(source, match.range.last + 1))
            match.groups["operator"] != null -> addToken(text, LessonCodeTokenKind.Operator)
            match.groups["punctuation"] != null -> addToken(text, LessonCodeTokenKind.Punctuation)
            else -> addToken(text, LessonCodeTokenKind.Plain)
        }
    }
    lines += LessonCodeLine(currentLine)

    return lines
}

private val KotlinTokenPattern =
    Regex(
        listOf(
            """(?<comment>//[^\n]*|/\*[\s\S]*?(?:\*/|$))""",
            """(?<string>"(?:\\.|[^"\\\n])*"?|'(?:\\.|[^'\\\n])*'?)""",
            """(?<number>\b\d+(?:\.\d+)?[LfF]?\b)""",
            """(?<identifier>[A-Za-z_][A-Za-z0-9_]*)""",
            """(?<operator>->|\.\.<?|==|!=|>=|<=|&&|\|\||\+\+|--|[+\-*/%=<>!?:])""",
            """(?<punctuation>[(){}\[\],;])""",
            """(?<other>\s+|.)""",
        ).joinToString("|"),
    )

private val StringTemplatePattern = Regex("""\$\{[^}]*}|\$[A-Za-z_][A-Za-z0-9_]*""")

private val KotlinKeywords =
    setOf(
        "as",
        "break",
        "class",
        "continue",
        "do",
        "else",
        "false",
        "for",
        "fun",
        "if",
        "in",
        "interface",
        "is",
        "null",
        "object",
        "package",
        "return",
        "super",
        "this",
        "throw",
        "true",
        "try",
        "typealias",
        "val",
        "var",
        "when",
        "while",
        "catch",
        "finally",
        "import",
        "abstract",
        "data",
        "enum",
        "override",
        "open",
        "private",
        "protected",
        "public",
        "internal",
        "sealed",
        "companion",
        "const",
        "lateinit",
        "vararg",
    )

private fun String.identifierKind(
    source: String,
    nextIndex: Int,
): LessonCodeTokenKind {
    if (this in KotlinKeywords) return LessonCodeTokenKind.Keyword

    val nextSymbol = source.drop(nextIndex).firstOrNull { !it.isWhitespace() || it == '\n' }
    return if (nextSymbol == '(') LessonCodeTokenKind.Function else LessonCodeTokenKind.Plain
}

/** Separa una cadena en texto literal y plantillas `$nombre` o `${expresión}`. */
private fun String.stringTokens(): List<Pair<String, LessonCodeTokenKind>> {
    if (startsWith('\'')) return listOf(this to LessonCodeTokenKind.StringLiteral)

    val tokens = mutableListOf<Pair<String, LessonCodeTokenKind>>()
    var index = 0
    StringTemplatePattern.findAll(this).forEach { template ->
        if (template.range.first > index) {
            tokens += substring(index, template.range.first) to LessonCodeTokenKind.StringLiteral
        }
        tokens += template.value to LessonCodeTokenKind.Interpolation
        index = template.range.last + 1
    }
    if (index < length) tokens += substring(index) to LessonCodeTokenKind.StringLiteral

    return tokens
}

private fun MutableList<LessonCodeToken>.appendMerging(token: LessonCodeToken) {
    val previous = lastOrNull()
    if (previous != null && previous.kind == token.kind && !previous.hasErrorUnderline) {
        this[lastIndex] = previous.copy(text = previous.text + token.text)
    } else {
        add(token)
    }
}
