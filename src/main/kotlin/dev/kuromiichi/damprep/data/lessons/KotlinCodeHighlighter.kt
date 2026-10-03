package dev.kuromiichi.damprep.data.lessons

/**
 * Convierte un fragmento de Kotlin en líneas coloreadas para los ejemplos de las lecciones.
 *
 * Reproduce las distinciones que hace IntelliJ IDEA con los temas Catppuccin: palabras clave,
 * funciones, tipos y anotaciones, parámetros, variables mutables, argumentos con nombre,
 * cadenas con plantillas y escapes, comentarios y corchetes de colores según su nivel de anidamiento.
 * Es un análisis léxico con algo de contexto, pensado para fragmentos cortos, no un compilador.
 *
 * Para subrayar un error se encierra el texto entre [ERROR_START] y [ERROR_END]; las marcas no
 * forman parte del código mostrado.
 *
 * Los ejemplos suelen escribirse como `kotlinCode($$"""...""".trimIndent())` para que `$nombre`
 * se muestre tal cual en lugar de interpolarse al compilar la app.
 */
internal fun kotlinCode(source: String): List<LessonCodeLine> {
    val (code, errorRanges) = source.extractErrorMarks()
    val pieces = KotlinClassifier(code).classify().splitAt(errorRanges)

    val lines = mutableListOf<LessonCodeLine>()
    var currentLine = mutableListOf<LessonCodeToken>()
    pieces.forEach { piece ->
        val hasError = errorRanges.any { piece.start in it }
        code.substring(piece.start, piece.end).split('\n').forEachIndexed { index, part ->
            if (index > 0) {
                lines += LessonCodeLine(currentLine)
                currentLine = mutableListOf()
            }
            if (part.isNotEmpty()) currentLine.appendMerging(LessonCodeToken(part, piece.kind, hasError))
        }
    }
    lines += LessonCodeLine(currentLine)

    return lines
}

internal const val ERROR_START = '⟦'
internal const val ERROR_END = '⟧'

private data class Piece(
    val start: Int,
    val end: Int,
    val kind: LessonCodeTokenKind,
)

private fun String.extractErrorMarks(): Pair<String, List<IntRange>> {
    val clean = StringBuilder()
    val ranges = mutableListOf<IntRange>()
    var open = -1
    forEach { char ->
        when (char) {
            ERROR_START -> open = clean.length
            ERROR_END ->
                if (open >= 0) {
                    ranges += open until clean.length
                    open = -1
                }
            else -> clean.append(char)
        }
    }
    return clean.toString() to ranges
}

/** Parte las piezas en los límites de los rangos con error para poder marcarlas por separado. */
private fun List<Piece>.splitAt(ranges: List<IntRange>): List<Piece> {
    val cuts = ranges.flatMap { listOf(it.first, it.last + 1) }.toSet()
    return flatMap { piece ->
        val inside = cuts.filter { it > piece.start && it < piece.end }.sorted()
        val bounds = listOf(piece.start) + inside + piece.end
        bounds.zipWithNext { from, to -> piece.copy(start = from, end = to) }
    }
}

private enum class Group { Comment, String, Number, Annotation, Identifier, Operator, Punctuation, Other }

private data class Raw(
    val group: Group,
    val range: IntRange,
)

private enum class Role { Call, Group, FunParams, Header, WhenHeader, Block, Lambda, BlockMarker, StatementMarker }

private class Frame(
    val role: Role,
    val bodyOfMarker: Boolean = false,
    val isDoBlock: Boolean = false,
) {
    var seenToken = false
}

/**
 * Clasifica los tokens llevando una pila de corchetes abiertos. IntelliJ IDEA colorea cada corchete
 * según cuántos elementos con corchetes lo rodean; las cabeceras de `if`, `for`, `while` y `catch`
 * cuentan como un nivel más para su cuerpo, de ahí las pilas de marcadores.
 */
private class KotlinClassifier(
    private val source: String,
) {
    private val raw = tokenize()
    private val significant = raw.indices.filter { raw[it].group != Group.Comment && !raw[it].isBlank() }
    private val parameterNames = collectParameterNames()
    private val mutableNames = collectMutableNames()

    private val stack = mutableListOf<Frame>()
    private val pieces = mutableListOf<Piece>()
    private var pendingFun = false
    private var funParamsSeen = false
    private var pendingDeclaration = false
    private var lastClosedRole: Role? = null
    private var lastClosedWasDo = false
    private var doWhilePending = false

    fun classify(): List<Piece> {
        val positionOf = significant.withIndex().associate { (position, rawIndex) -> rawIndex to position }
        raw.forEachIndexed { index, token ->
            val text = textOf(token)
            val position = positionOf[index]
            when {
                token.group == Group.Comment -> add(token.range, LessonCodeTokenKind.Comment)
                position == null -> {
                    add(token.range, LessonCodeTokenKind.Plain)
                    if ('\n' in text) endOfLine()
                }
                else -> classifySignificant(token, text, position)
            }
        }
        return pieces
    }

    private fun classifySignificant(
        token: Raw,
        text: String,
        position: Int,
    ) {
        stack.lastOrNull()?.takeIf { it.role == Role.StatementMarker }?.seenToken = true
        val closedDoBlock = lastClosedWasDo
        if (text != "}") lastClosedWasDo = false
        val previous = significant.getOrNull(position - 1)?.let { textOf(raw[it]) }
        val next = significant.getOrNull(position + 1)?.let { textOf(raw[it]) }

        when (token.group) {
            Group.String -> addString(token.range)
            Group.Number -> add(token.range, LessonCodeTokenKind.Number)
            Group.Annotation -> add(token.range, LessonCodeTokenKind.ClassName)
            Group.Identifier -> {
                if (text == "while" && previous == "}" && closedDoBlock) doWhilePending = true
                add(token.range, identifierKind(text, previous, next))
            }
            Group.Operator ->
                add(
                    token.range,
                    when (text) {
                        "->" -> LessonCodeTokenKind.LambdaDelimiter
                        ":", "::" -> LessonCodeTokenKind.Punctuation
                        else -> LessonCodeTokenKind.Operator
                    },
                )
            Group.Punctuation -> add(token.range, punctuationKind(text, previous, next, position))
            Group.Comment, Group.Other -> add(token.range, LessonCodeTokenKind.Plain)
        }
        if (text == "=" && pendingFun) endFunHeader()
    }

    private fun identifierKind(
        text: String,
        previous: String?,
        next: String?,
    ): LessonCodeTokenKind {
        if (text in KotlinKeywords) {
            when (text) {
                "fun" -> pendingFun = true
                "class", "object", "interface" -> pendingDeclaration = true
            }
            return LessonCodeTokenKind.Keyword
        }
        val afterDot = previous == "."
        val isNamedArgument = next == "=" && stack.lastOrNull()?.role == Role.Call && (previous == "(" || previous == ",")
        val name = text.trim('`')
        return when {
            isNamedArgument -> LessonCodeTokenKind.Constant
            next == "(" && previous in DeclarationKeywords -> LessonCodeTokenKind.ClassName
            next == "(" && name.first().isUpperCase() && !afterDot -> LessonCodeTokenKind.Constant
            next == "(" -> LessonCodeTokenKind.Function
            previous in DeclarationKeywords -> LessonCodeTokenKind.ClassName
            name.length > 1 && name.all { it.isUpperCase() || it.isDigit() || it == '_' } -> LessonCodeTokenKind.Constant
            name.first().isUpperCase() -> LessonCodeTokenKind.ClassName
            afterDot -> LessonCodeTokenKind.Plain
            name in parameterNames -> LessonCodeTokenKind.Parameter
            name in mutableNames -> LessonCodeTokenKind.MutableVariable
            else -> LessonCodeTokenKind.Plain
        }
    }

    private fun punctuationKind(
        text: String,
        previous: String?,
        next: String?,
        position: Int,
    ): LessonCodeTokenKind =
        when (text) {
            "(", "[" -> {
                val kind = bracketKind(stack.size)
                val role = openingRole(text, previous)
                if (role == Role.FunParams) funParamsSeen = true
                if (previous == "while") doWhilePending = false
                stack += Frame(role)
                kind
            }
            "{" -> {
                val isBlock = isBlockBrace(previous)
                val kind = if (isBlock) bracketKind(stack.size) else LessonCodeTokenKind.LambdaDelimiter
                val marker = stack.lastOrNull()?.role == Role.BlockMarker
                stack += Frame(if (isBlock) Role.Block else Role.Lambda, bodyOfMarker = marker, isDoBlock = previous == "do")
                if (isBlock) {
                    endFunHeader()
                    pendingDeclaration = false
                }
                kind
            }
            ")", "]" -> {
                val closed = popUntil { it.role in ParenRoles }
                val kind = bracketKind(stack.size)
                lastClosedRole = closed?.role
                if (closed?.role == Role.Header) {
                    stack += Frame(if (next == "{") Role.BlockMarker else Role.StatementMarker)
                }
                kind
            }
            "}" -> {
                val closed = popUntil { it.role == Role.Block || it.role == Role.Lambda }
                val kind = if (closed?.role == Role.Lambda) LessonCodeTokenKind.LambdaDelimiter else bracketKind(stack.size)
                lastClosedRole = closed?.role
                lastClosedWasDo = closed?.isDoBlock == true
                if (closed?.bodyOfMarker == true && next != "else") popUntil { it.role == Role.BlockMarker }
                kind
            }
            else -> LessonCodeTokenKind.Punctuation
        }

    private fun openingRole(
        bracket: String,
        previous: String?,
    ): Role {
        if (bracket == "[") return Role.Group
        return when {
            previous == "when" || (previous == "while" && doWhilePending) -> Role.WhenHeader
            previous in ControlKeywords -> Role.Header
            pendingFun && !funParamsSeen -> Role.FunParams
            pendingDeclaration -> Role.Group
            previous != null && (previous.first().isLetterOrDigit() || previous.first() == '_' || previous.first() == '`' || previous == ">") -> Role.Call
            else -> Role.Group
        }
    }

    private fun isBlockBrace(previous: String?): Boolean =
        previous in BlockKeywords ||
            pendingFun ||
            pendingDeclaration ||
            (previous == ")" && lastClosedRole in BlockHeaderRoles) ||
            previous == "when"

    private fun endFunHeader() {
        pendingFun = false
        funParamsSeen = false
    }

    private fun endOfLine() {
        pendingDeclaration = false
        while (stack.lastOrNull()?.let { it.role == Role.StatementMarker && it.seenToken } == true) {
            stack.removeAt(stack.lastIndex)
        }
    }

    private fun popUntil(predicate: (Frame) -> Boolean): Frame? {
        val index = stack.indexOfLast(predicate)
        if (index < 0) return null
        val frame = stack[index]
        while (stack.size > index) stack.removeAt(stack.lastIndex)
        return frame
    }

    private fun bracketKind(depth: Int): LessonCodeTokenKind = BracketKinds[depth % BracketKinds.size]

    private fun add(
        range: IntRange,
        kind: LessonCodeTokenKind,
    ) {
        pieces += Piece(range.first, range.last + 1, kind)
    }

    private fun addString(range: IntRange) {
        val text = source.substring(range.first, range.last + 1)
        if (text.startsWith('\'')) {
            add(range, LessonCodeTokenKind.StringLiteral)
            return
        }
        var index = 0
        StringPartPattern.findAll(text).forEach { part ->
            if (part.range.first > index) add(range.first + index until range.first + part.range.first, LessonCodeTokenKind.StringLiteral)
            add(range.first + part.range.first..range.first + part.range.last, LessonCodeTokenKind.Interpolation)
            index = part.range.last + 1
        }
        if (index < text.length) add(range.first + index..range.last, LessonCodeTokenKind.StringLiteral)
    }

    private fun tokenize(): List<Raw> =
        KotlinTokenPattern
            .findAll(source)
            .map { match ->
                val group =
                    when {
                        match.groups["comment"] != null -> Group.Comment
                        match.groups["string"] != null -> Group.String
                        match.groups["number"] != null -> Group.Number
                        match.groups["annotation"] != null -> Group.Annotation
                        match.groups["identifier"] != null -> Group.Identifier
                        match.groups["operator"] != null -> Group.Operator
                        match.groups["punctuation"] != null -> Group.Punctuation
                        else -> Group.Other
                    }
                Raw(group, match.range)
            }.toList()

    private fun collectParameterNames(): Set<String> {
        val names = mutableSetOf<String>()
        significant.forEachIndexed { position, rawIndex ->
            if (textOf(raw[rawIndex]) != "fun") return@forEachIndexed
            var cursor = position + 1
            while (cursor < significant.size && textOf(raw[significant[cursor]]) !in setOf("(", "{", "=")) cursor++
            if (cursor >= significant.size || textOf(raw[significant[cursor]]) != "(") return@forEachIndexed
            var depth = 0
            while (cursor < significant.size) {
                val text = textOf(raw[significant[cursor]])
                if (text == "(") depth++
                if (text == ")") depth--
                if (depth == 0) break
                val previous = textOf(raw[significant[cursor - 1]])
                val following = significant.getOrNull(cursor + 1)?.let { textOf(raw[it]) }
                if (depth == 1 &&
                    raw[significant[cursor]].group == Group.Identifier &&
                    following == ":" &&
                    previous in setOf("(", ",", "vararg")
                ) {
                    names += text.trim('`')
                }
                cursor++
            }
        }
        return names
    }

    private fun collectMutableNames(): Set<String> =
        significant
            .mapIndexedNotNull { position, rawIndex ->
                val next = significant.getOrNull(position + 1)?.let { raw[it] }
                if (textOf(raw[rawIndex]) == "var" && next?.group == Group.Identifier) textOf(next).trim('`') else null
            }.toSet()

    private fun textOf(token: Raw): String = source.substring(token.range.first, token.range.last + 1)

    private fun Raw.isBlank(): Boolean = group == Group.Other && textOf(this).isBlank()
}

private val BracketKinds =
    listOf(
        LessonCodeTokenKind.Bracket0,
        LessonCodeTokenKind.Bracket1,
        LessonCodeTokenKind.Bracket2,
        LessonCodeTokenKind.Bracket3,
        LessonCodeTokenKind.Bracket4,
    )

private val KotlinTokenPattern =
    Regex(
        listOf(
            """(?<comment>//[^\n]*|/\*[\s\S]*?(?:\*/|$))""",
            """(?<string>"(?:\\.|[^"\\\n])*"?|'(?:\\.|[^'\\\n])*'?)""",
            """(?<number>\b\d+(?:\.\d+)?[LfF]?\b)""",
            """(?<annotation>@[A-Za-z_][A-Za-z0-9_]*)""",
            """(?<identifier>`[^`\n]+`|[A-Za-z_][A-Za-z0-9_]*)""",
            """(?<operator>->|::|\?\.|\?:|!!|\.\.<?|==|!=|>=|<=|&&|\|\||\+\+|--|[+\-*/%=<>!?:])""",
            """(?<punctuation>[(){}\[\],;.])""",
            """(?<other>\s+|.)""",
        ).joinToString("|"),
    )

/** Plantillas `$nombre` y `${expresión}` y escapes como `\n` dentro de una cadena. */
private val StringPartPattern = Regex("""\$\{[^}]*}|\$[A-Za-z_][A-Za-z0-9_]*|\\.""")

private val ParenRoles = setOf(Role.Call, Role.Group, Role.FunParams, Role.Header, Role.WhenHeader)

private val BlockHeaderRoles = setOf(Role.Header, Role.WhenHeader, Role.FunParams)

private val ControlKeywords = setOf("if", "for", "while", "catch")

private val BlockKeywords = setOf("else", "try", "finally", "do", "init")

private val DeclarationKeywords = setOf("class", "object", "interface")

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
        "init",
        "by",
        "constructor",
        "suspend",
        "inline",
    )

private fun MutableList<LessonCodeToken>.appendMerging(token: LessonCodeToken) {
    val previous = lastOrNull()
    if (previous != null && previous.kind == token.kind && previous.hasErrorUnderline == token.hasErrorUnderline) {
        this[lastIndex] = previous.copy(text = previous.text + token.text)
    } else {
        add(token)
    }
}
