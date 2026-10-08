package dev.kuromiichi.damprep.data.lessons.contents

import dev.kuromiichi.damprep.data.lessons.LessonCalloutTone
import dev.kuromiichi.damprep.data.lessons.LessonCodeExample
import dev.kuromiichi.damprep.data.lessons.LessonContent
import dev.kuromiichi.damprep.data.lessons.LessonLink
import dev.kuromiichi.damprep.data.lessons.LessonPractice
import dev.kuromiichi.damprep.data.lessons.LessonScreenshot
import dev.kuromiichi.damprep.data.lessons.LessonStep
import dev.kuromiichi.damprep.data.lessons.LessonVisualContent
import dev.kuromiichi.damprep.data.lessons.UmlNode
import dev.kuromiichi.damprep.data.lessons.UmlRelation
import dev.kuromiichi.damprep.data.lessons.UmlRelationKind
import dev.kuromiichi.damprep.data.lessons.callout
import dev.kuromiichi.damprep.data.lessons.cell
import dev.kuromiichi.damprep.data.lessons.consoleInput
import dev.kuromiichi.damprep.data.lessons.consoleOutput
import dev.kuromiichi.damprep.data.lessons.consoleRun
import dev.kuromiichi.damprep.data.lessons.emphasis
import dev.kuromiichi.damprep.data.lessons.explanationCode
import dev.kuromiichi.damprep.data.lessons.highlight
import dev.kuromiichi.damprep.data.lessons.inlineCode
import dev.kuromiichi.damprep.data.lessons.kotlinCode
import dev.kuromiichi.damprep.data.lessons.paragraph
import dev.kuromiichi.damprep.data.lessons.regular
import dev.kuromiichi.damprep.data.lessons.richText
import dev.kuromiichi.damprep.data.lessons.steps
import dev.kuromiichi.damprep.data.lessons.umlClass
import dev.kuromiichi.damprep.data.lessons.umlClassName

val basicUmlLessonContent =
    LessonContent(
        lessonId = "basic-uml",
        steps =
            listOf(
                LessonStep(
                    title = "Dibujar antes de programar",
                    explanation =
                        listOf(
                            paragraph(regular("Con dos o tres clases, el código se entiende de un vistazo. Con diez, ya no: cuesta ver qué clase conoce a cuál o quién crea a quién. Un dibujo de las clases y sus relaciones da esa visión de conjunto antes de escribir una línea, y sirve para hablar del diseño con otras personas.")),
                            paragraph(regular("Para que todos dibujen igual existe "), highlight("UML"), regular(" (Unified Modeling Language), una notación estándar para describir software. No es un lenguaje de programación: es un conjunto de diagramas con reglas comunes que entiende cualquier equipo, programe en Kotlin, Java o C#.")),
                            paragraph(regular("Hay diagramas de "), emphasis("estructura"), regular(", que muestran de qué piezas está hecho el programa, y de "), emphasis("comportamiento"), regular(", que muestran qué ocurre mientras se ejecuta. Esta lección trata el más usado de estructura, el "), highlight("diagrama de clases"), regular("; los de comportamiento llegan en la siguiente.")),
                        ),
                    visual = LessonVisualContent.Flow(listOf("Leer el enunciado", "Dibujar las clases", "Escribir el código", "Revisar el diagrama")),
                ),
                LessonStep(
                    title = "Una clase en UML",
                    explanation =
                        listOf(
                            paragraph(regular("Una clase se dibuja como una caja con tres compartimentos: arriba el nombre, en medio los "), highlight("atributos"), regular(" (las propiedades) y abajo las "), highlight("operaciones"), regular(" (los métodos). Es la misma clase que escribirías en Kotlin, sin el cuerpo de los métodos.")),
                            paragraph(regular("Los tipos van detrás de dos puntos, como en Kotlin: "), inlineCode("nombre: String"), regular(", "), inlineCode("esMayorDeEdad(): Boolean"), regular(". Si un método no devuelve nada, no se escribe el tipo. El constructor no se dibuja: sus parámetros con "), inlineCode("val"), regular(" o "), inlineCode("var"), regular(" ya aparecen como atributos.")),
                            explanationCode(
                                "Alumno.kt",
                                richText(regular("La clase que dibuja el diagrama del ejemplo. UML no distingue "), inlineCode("val"), regular(" de "), inlineCode("var"), regular(": eso lo decides al programar.")),
                                "class Alumno(val nombre: String, var edad: Int) {",
                                "    fun esMayorDeEdad(): Boolean {",
                                "        return edad >= 18",
                                "    }",
                                "",
                                "    fun saludar(otro: Alumno) {",
                                "        println(\"Hola, \${otro.nombre}, soy \$nombre\")",
                                "    }",
                                "}",
                            ),
                            callout("A mano o con herramienta", "Un diagrama no tiene que ser bonito: un boceto en papel para pensar vale tanto como uno hecho con una herramienta. Lo que importa es que la notación sea la correcta, para que cualquiera lo lea igual."),
                        ),
                    visual =
                        LessonVisualContent.UmlDiagram(
                            nodes =
                                listOf(
                                    umlClass(
                                        id = "Alumno",
                                        cell = cell(0, 0),
                                        attributes = listOf("+ nombre: String", "+ edad: Int"),
                                        operations = listOf("+ esMayorDeEdad(): Boolean", "+ saludar(otro: Alumno)"),
                                    ),
                                ),
                            caption = "Nombre, atributos y operaciones",
                        ),
                ),
                LessonStep(
                    title = "Visibilidad y miembros de clase",
                    explanation =
                        listOf(
                            paragraph(regular("Delante de cada miembro, un símbolo indica quién puede usarlo. En Kotlin todo es público si no dices lo contrario, pero en el diagrama la visibilidad se escribe siempre:")),
                            steps(
                                richText(inlineCode("+"), regular(" público: "), inlineCode("public"), regular(", lo que no lleva modificador.")),
                                richText(inlineCode("-"), regular(" privado: "), inlineCode("private"), regular(", solo desde la propia clase.")),
                                richText(inlineCode("#"), regular(" protegido: "), inlineCode("protected"), regular(", la clase y sus hijas.")),
                                richText(inlineCode("~"), regular(" de paquete. Kotlin no lo tiene; lo más parecido es "), inlineCode("internal"), regular(", visible en todo el módulo.")),
                            ),
                            paragraph(regular("Los miembros de clase, los que en Kotlin van en el "), inlineCode("companion object"), regular(", se "), highlight("subrayan"), regular(". En el ejemplo, la nota de aprobado es la misma para todos los alumnos, así que pertenece a la clase y no a cada objeto.")),
                            explanationCode(
                                "Alumno.kt",
                                richText(regular("Las notas son privadas: desde fuera solo se añaden y se consulta la media, como en la lección de clases y objetos.")),
                                "class Alumno(val nombre: String) {",
                                "    private val notas = mutableListOf<Double>()",
                                "",
                                "    fun agregarNota(nota: Double) {",
                                "        notas.add(nota)",
                                "    }",
                                "",
                                "    fun media(): Double {",
                                "        var suma = 0.0",
                                "        for (nota in notas) {",
                                "            suma += nota",
                                "        }",
                                "        return if (notas.isEmpty()) 0.0 else suma / notas.size",
                                "    }",
                                "",
                                "    fun aprobado(): Boolean {",
                                "        return media() >= NOTA_APROBADO",
                                "    }",
                                "",
                                "    companion object {",
                                "        const val NOTA_APROBADO = 5.0",
                                "    }",
                                "}",
                            ),
                        ),
                    visual =
                        LessonVisualContent.UmlDiagram(
                            nodes =
                                listOf(
                                    umlClass(
                                        id = "Alumno",
                                        cell = cell(0, 0),
                                        attributes = listOf("+ nombre: String", "- notas: MutableList<Double>", "+ NOTA_APROBADO: Double = 5.0$"),
                                        operations = listOf("+ agregarNota(nota: Double)", "+ media(): Double", "+ aprobado(): Boolean"),
                                    ),
                                ),
                            caption = "Lo privado lleva «-» y lo de la clase va subrayado",
                        ),
                ),
                LessonStep(
                    title = "Objetos en un momento dado",
                    explanation =
                        listOf(
                            paragraph(regular("El diagrama de clases describe todos los objetos posibles. A veces ayuda más ver unos objetos concretos: un "), highlight("diagrama de objetos"), regular(" es una foto de la memoria en un instante, con el valor de cada atributo.")),
                            paragraph(regular("Cada objeto se escribe "), inlineCode("nombre: Clase"), regular(" y subrayado, para no confundirlo con una clase. Las líneas entre objetos son "), emphasis("enlaces"), regular(": el objeto de la izquierda guarda una referencia al otro. En el ejemplo, la foto se toma justo al acabar "), inlineCode("main"), regular(".")),
                            explanationCode(
                                "Main.kt",
                                richText(regular("Dos alumnos matriculados en el mismo curso. Aquí "), inlineCode("Curso"), regular(" solo tiene nombre y "), inlineCode("matricular"), regular(".")),
                                "fun main() {",
                                "    val dam = Curso(\"1º DAM\")",
                                "    val ana = Alumno(\"Ana\", 19)",
                                "    val luis = Alumno(\"Luis\", 17)",
                                "    dam.matricular(ana)",
                                "    dam.matricular(luis)",
                                "}",
                            ),
                        ),
                    visual =
                        LessonVisualContent.UmlDiagram(
                            nodes =
                                listOf(
                                    UmlNode.ObjectBox(id = "dam", cell = cell(0, 0, columnSpan = 2), name = "dam: Curso", values = listOf("nombre = \"1º DAM\"")),
                                    UmlNode.ObjectBox(id = "ana", cell = cell(1, 0), name = "ana: Alumno", values = listOf("nombre = \"Ana\"", "edad = 19")),
                                    UmlNode.ObjectBox(id = "luis", cell = cell(1, 1), name = "luis: Alumno", values = listOf("nombre = \"Luis\"", "edad = 17")),
                                ),
                            relations =
                                listOf(
                                    UmlRelation(from = "dam", to = "ana", kind = UmlRelationKind.Link),
                                    UmlRelation(from = "dam", to = "luis", kind = UmlRelationKind.Link),
                                ),
                            caption = "Un curso con dos alumnos al acabar main",
                        ),
                ),
                LessonStep(
                    title = "Asociaciones y multiplicidad",
                    explanation =
                        listOf(
                            paragraph(regular("Cuando una clase guarda una referencia a otra hay una "), highlight("asociación"), regular(", una línea entre las dos cajas. Si solo una conoce a la otra, la línea lleva una flecha hacia la conocida: en el ejemplo, el curso conoce a su tutor, pero el profesor no sabe de qué cursos es tutor.")),
                            paragraph(regular("Junto a la flecha va el "), emphasis("rol"), regular(", el nombre de la propiedad, y la "), highlight("multiplicidad"), regular(", cuántos objetos hay en ese extremo. La propiedad no se repite como atributo: la línea ya la representa. Así se traduce cada multiplicidad:")),
                            steps(
                                richText(inlineCode("1"), regular(": exactamente uno, "), inlineCode("val tutor: Profesor"), regular(".")),
                                richText(inlineCode("0..1"), regular(": uno o ninguno, "), inlineCode("var aula: Aula?"), regular(".")),
                                richText(inlineCode("*"), regular(": cualquier cantidad, también cero, "), inlineCode("List<Alumno>"), regular(".")),
                                richText(inlineCode("1..*"), regular(" o "), inlineCode("0..30"), regular(": una lista con límites que la propia clase hace cumplir.")),
                            ),
                            explanationCode(
                                "Curso.kt",
                                richText(regular("El código del diagrama: "), inlineCode("matricular"), regular(" rechaza al alumno 31 para respetar el "), inlineCode("0..30"), regular(".")),
                                "class Curso(val nombre: String, val tutor: Profesor) {",
                                "    var aula: Aula? = null",
                                "    private val alumnos = mutableListOf<Alumno>()",
                                "",
                                "    fun matricular(alumno: Alumno): Boolean {",
                                "        if (alumnos.size == 30) {",
                                "            return false",
                                "        }",
                                "        alumnos.add(alumno)",
                                "        return true",
                                "    }",
                                "}",
                            ),
                            callout("Sin flecha", richText(regular("Una línea sin flechas indica que cada clase conoce a la otra, como en las relaciones en las dos direcciones de la lección de relaciones y composición. Al empezar un diseño se suele dejar sin flechas y decidir la dirección después.")), LessonCalloutTone.Tip),
                        ),
                    visual =
                        LessonVisualContent.UmlDiagram(
                            nodes =
                                listOf(
                                    umlClass(id = "Curso", cell = cell(0, 0), attributes = listOf("+ nombre: String"), operations = listOf("+ matricular(alumno: Alumno): Boolean")),
                                    umlClassName(id = "Profesor", cell = cell(0, 1)),
                                    umlClassName(id = "Alumno", cell = cell(1, 0)),
                                    umlClassName(id = "Aula", cell = cell(1, 1)),
                                ),
                            relations =
                                listOf(
                                    UmlRelation(from = "Curso", to = "Profesor", kind = UmlRelationKind.NavigableAssociation, toLabel = "1", toRole = "tutor"),
                                    UmlRelation(from = "Curso", to = "Alumno", kind = UmlRelationKind.NavigableAssociation, toLabel = "0..30", toRole = "alumnos"),
                                    UmlRelation(from = "Curso", to = "Aula", kind = UmlRelationKind.NavigableAssociation, toLabel = "0..1", toRole = "aula"),
                                ),
                            caption = "Un tutor, de 0 a 30 alumnos y quizá un aula",
                        ),
                ),
                LessonStep(
                    title = "Agregación y composición",
                    explanation =
                        listOf(
                            paragraph(regular("Dos asociaciones especiales distinguen las relaciones «todo-parte» de la lección de relaciones y composición. Las dos llevan un rombo en el lado del todo.")),
                            paragraph(regular("En la "), highlight("composición"), regular(" (rombo relleno) el todo crea sus partes, que no tienen sentido fuera de él y desaparecen con él: un pedido y sus líneas. Una parte pertenece a un solo todo, así que en ese extremo la multiplicidad es "), inlineCode("1"), regular(".")),
                            paragraph(regular("En la "), highlight("agregación"), regular(" (rombo hueco) el todo agrupa objetos que ya existían y que pueden estar en varios todos a la vez: una alumna en dos cursos. El todo los recibe, no los crea.")),
                            explanationCode(
                                "Main.kt",
                                richText(regular("El pedido crea sus líneas con "), inlineCode("agregarLinea"), regular("; el curso recibe alumnos que se crearon fuera.")),
                                "class LineaPedido(val producto: String, val unidades: Int)",
                                "",
                                "class Pedido {",
                                "    private val lineas = mutableListOf<LineaPedido>()",
                                "",
                                "    fun agregarLinea(producto: String, unidades: Int) {",
                                "        lineas.add(LineaPedido(producto, unidades))",
                                "    }",
                                "}",
                                "",
                                "class Curso(val nombre: String) {",
                                "    private val alumnos = mutableListOf<Alumno>()",
                                "",
                                "    fun matricular(alumno: Alumno) {",
                                "        alumnos.add(alumno)",
                                "    }",
                                "}",
                            ),
                            callout("En caso de duda, asociación", "Muchos equipos solo usan la composición y dibujan el resto como asociaciones normales, porque la agregación no añade nada al código. Si no tienes claro cuál poner, una asociación con su multiplicidad nunca es incorrecta.", LessonCalloutTone.Tip),
                        ),
                    visual =
                        LessonVisualContent.UmlDiagram(
                            nodes =
                                listOf(
                                    umlClassName(id = "Pedido", cell = cell(0, 0)),
                                    umlClassName(id = "LineaPedido", cell = cell(1, 0)),
                                    umlClassName(id = "Curso", cell = cell(0, 1)),
                                    umlClassName(id = "Alumno", cell = cell(1, 1)),
                                ),
                            relations =
                                listOf(
                                    UmlRelation(from = "Pedido", to = "LineaPedido", kind = UmlRelationKind.Composition, fromLabel = "1", toLabel = "1..*", toRole = "lineas"),
                                    UmlRelation(from = "Curso", to = "Alumno", kind = UmlRelationKind.Aggregation, fromLabel = "*", toLabel = "*", toRole = "alumnos"),
                                ),
                            caption = "Composición a la izquierda, agregación a la derecha",
                        ),
                ),
                LessonStep(
                    title = "Dependencias",
                    explanation =
                        listOf(
                            paragraph(regular("A veces una clase usa otra solo un momento: la recibe como parámetro, la crea en una variable local o la devuelve, pero no la guarda. No hay asociación, porque no hay propiedad; hay una "), highlight("dependencia"), regular(", una flecha discontinua hacia la clase usada.")),
                            paragraph(regular("En el ejemplo, el boletín necesita un curso para escribir el informe, pero no se queda con él. La dependencia avisa de que, si "), inlineCode("Curso"), regular(" cambia, "), inlineCode("Boletin"), regular(" puede verse afectado.")),
                            explanationCode(
                                "Boletin.kt",
                                richText(inlineCode("curso"), regular(" solo vive mientras se ejecuta "), inlineCode("informe"), regular(". Si "), inlineCode("Boletin"), regular(" lo guardara en una propiedad, sería una asociación.")),
                                "class Boletin(val trimestre: Int) {",
                                "    fun informe(curso: Curso): String {",
                                "        return \"Trimestre \$trimestre: \${curso.nombre}\"",
                                "    }",
                                "}",
                            ),
                        ),
                    visual =
                        LessonVisualContent.UmlDiagram(
                            nodes =
                                listOf(
                                    umlClass(id = "Boletin", cell = cell(0, 0), attributes = listOf("+ trimestre: Int"), operations = listOf("+ informe(curso: Curso): String")),
                                    umlClass(id = "Curso", cell = cell(0, 1), attributes = listOf("+ nombre: String")),
                                ),
                            relations = listOf(UmlRelation(from = "Boletin", to = "Curso", kind = UmlRelationKind.Dependency)),
                            caption = "Boletin usa Curso sin guardarlo",
                        ),
                ),
                LessonStep(
                    title = "Herencia, abstractas e interfaces",
                    explanation =
                        listOf(
                            paragraph(regular("La "), highlight("herencia"), regular(" se dibuja con una línea continua y un triángulo hueco que apunta a la clase base. Una clase o una operación abstracta se escribe en "), emphasis("cursiva"), regular("; como la cursiva se pierde al dibujar a mano, se suele añadir «abstract».")),
                            paragraph(regular("Una interfaz es una caja con «interface» encima del nombre. La clase que la implementa se une a ella con una línea "), highlight("discontinua"), regular(" y el mismo triángulo: es una "), emphasis("realización"), regular(". Un "), inlineCode("enum class"), regular(" lleva «enumeration» y sus valores en el compartimento de atributos.")),
                            explanationCode(
                                "Main.kt",
                                richText(regular("El código del diagrama, salvo el cuerpo de los métodos, que el diagrama no dice.")),
                                "enum class Modalidad { PRESENCIAL, DISTANCIA }",
                                "",
                                "interface Evaluable {",
                                "    fun nota(): Double",
                                "}",
                                "",
                                "abstract class Persona(protected val nombre: String) {",
                                "    abstract fun presentarse(): String",
                                "}",
                                "",
                                "class Profesor(nombre: String) : Persona(nombre) {",
                                "    override fun presentarse(): String = \"Profesor \$nombre\"",
                                "}",
                                "",
                                "class Alumno(nombre: String, val modalidad: Modalidad) : Persona(nombre), Evaluable {",
                                "    override fun presentarse(): String = \"Alumno \$nombre\"",
                                "",
                                "    override fun nota(): Double = 7.5",
                                "}",
                            ),
                        ),
                    visual =
                        LessonVisualContent.UmlDiagram(
                            nodes =
                                listOf(
                                    umlClass(id = "Persona", cell = cell(0, 0, columnSpan = 2), attributes = listOf("# nombre: String"), operations = listOf("+ presentarse(): String*"), stereotype = "abstract", isAbstract = true),
                                    umlClass(id = "Evaluable", cell = cell(0, 2), operations = listOf("+ nota(): Double*"), stereotype = "interface"),
                                    umlClass(id = "Profesor", cell = cell(1, 0), operations = listOf("+ presentarse(): String")),
                                    umlClass(id = "Alumno", cell = cell(1, 1), operations = listOf("+ presentarse(): String", "+ nota(): Double")),
                                    umlClass(id = "Modalidad", cell = cell(2, 1), attributes = listOf("PRESENCIAL", "DISTANCIA"), stereotype = "enumeration"),
                                ),
                            relations =
                                listOf(
                                    UmlRelation(from = "Profesor", to = "Persona", kind = UmlRelationKind.Inheritance),
                                    UmlRelation(from = "Alumno", to = "Persona", kind = UmlRelationKind.Inheritance),
                                    UmlRelation(from = "Alumno", to = "Evaluable", kind = UmlRelationKind.Realization),
                                    UmlRelation(from = "Alumno", to = "Modalidad", kind = UmlRelationKind.NavigableAssociation, toLabel = "1", toRole = "modalidad"),
                                ),
                            caption = "Herencia continua, realización discontinua",
                        ),
                ),
                LessonStep(
                    title = "Del diagrama al código",
                    explanation =
                        listOf(
                            paragraph(regular("Con un diagrama delante, el código sale casi solo. Este es el orden para traducirlo, aplicado en el ejemplo a una lista de reproducción:")),
                            steps(
                                richText(regular("Cada caja es una "), inlineCode("class"), regular(", "), inlineCode("interface"), regular(", "), inlineCode("abstract class"), regular(" o "), inlineCode("enum class"), regular(".")),
                                richText(regular("Cada atributo es una propiedad con su visibilidad. Si es "), inlineCode("val"), regular(" o "), inlineCode("var"), regular(" lo decides tú.")),
                                richText(regular("Cada asociación navegable es una propiedad en la clase de la que sale la flecha, con el rol como nombre y el tipo según la multiplicidad.")),
                                richText(regular("Con composición, el todo crea las partes; con agregación, las recibe ya creadas.")),
                                richText(regular("Cada operación es una función con su firma. El cuerpo no está en el diagrama: lo escribes tú.")),
                            ),
                            explanationCode(
                                "Playlist.kt",
                                richText(regular("Las canciones llegan creadas: "), inlineCode("agregar"), regular(" las recibe, como pide el rombo hueco.")),
                                "class Artista(val nombre: String)",
                                "",
                                "class Cancion(val titulo: String, val segundos: Int, val artista: Artista)",
                                "",
                                "class Playlist(private val nombre: String) {",
                                "    private val canciones = mutableListOf<Cancion>()",
                                "",
                                "    fun agregar(cancion: Cancion) {",
                                "        canciones.add(cancion)",
                                "    }",
                                "",
                                "    fun duracionTotal(): Int {",
                                "        var total = 0",
                                "        for (cancion in canciones) {",
                                "            total += cancion.segundos",
                                "        }",
                                "        return total",
                                "    }",
                                "}",
                            ),
                            callout("Generar código", "Algunas herramientas UML generan el esqueleto de las clases a partir del diagrama. Ahorran escribir, pero las decisiones siguen siendo tuyas: qué es val, qué se valida y qué hace cada método.", LessonCalloutTone.Information),
                        ),
                    visual =
                        LessonVisualContent.UmlDiagram(
                            nodes =
                                listOf(
                                    umlClass(id = "Playlist", cell = cell(0, 0), attributes = listOf("- nombre: String"), operations = listOf("+ agregar(cancion: Cancion)", "+ duracionTotal(): Int")),
                                    umlClass(id = "Cancion", cell = cell(1, 0), attributes = listOf("+ titulo: String", "+ segundos: Int")),
                                    umlClass(id = "Artista", cell = cell(1, 1), attributes = listOf("+ nombre: String")),
                                ),
                            relations =
                                listOf(
                                    UmlRelation(from = "Playlist", to = "Cancion", kind = UmlRelationKind.Aggregation, fromLabel = "*", toLabel = "*", toRole = "canciones"),
                                    UmlRelation(from = "Cancion", to = "Artista", kind = UmlRelationKind.NavigableAssociation, toLabel = "1", toRole = "artista"),
                                ),
                            caption = "El diagrama que se traduce a Kotlin",
                        ),
                ),
                LessonStep(
                    title = "Del código al diagrama",
                    explanation =
                        listOf(
                            paragraph(regular("También se hace al revés: leer un código que no conoces y dibujar su diagrama. Se llama "), highlight("ingeniería inversa"), regular(" y es la forma más rápida de entender un proyecto ajeno. Basta con recorrer cada clase haciéndose estas preguntas:")),
                            steps(
                                richText(regular("¿Propiedad de un tipo básico ("), inlineCode("String"), regular(", "), inlineCode("Int"), regular("…)? Atributo.")),
                                richText(regular("¿Propiedad de otra clase del proyecto? Asociación: "), inlineCode("?"), regular(" da "), inlineCode("0..1"), regular(" y una lista da "), inlineCode("*"), regular(".")),
                                richText(regular("¿La clase crea esos objetos dentro y no los comparte? Composición.")),
                                richText(regular("¿"), inlineCode(": Base()"), regular(" o "), inlineCode(": Interfaz"), regular("? Herencia o realización.")),
                                richText(regular("¿Solo aparece como parámetro o variable local? Dependencia.")),
                            ),
                            explanationCode(
                                "Veterinaria.kt",
                                richText(regular("El código del que sale el diagrama del ejemplo.")),
                                "interface Vacunable {",
                                "    fun vacunar(fecha: String)",
                                "}",
                                "",
                                "open class Mascota(val nombre: String)",
                                "",
                                "class Perro(nombre: String) : Mascota(nombre), Vacunable {",
                                "    private val vacunas = mutableListOf<String>()",
                                "",
                                "    override fun vacunar(fecha: String) {",
                                "        vacunas.add(fecha)",
                                "    }",
                                "}",
                                "",
                                "class Cliente(val nombre: String) {",
                                "    private val mascotas = mutableListOf<Mascota>()",
                                "",
                                "    fun registrar(mascota: Mascota) {",
                                "        mascotas.add(mascota)",
                                "    }",
                                "}",
                                "",
                                "class Recepcion {",
                                "    fun avisar(cliente: Cliente, mensaje: String) {",
                                "        println(\"\${cliente.nombre}: \$mensaje\")",
                                "    }",
                                "}",
                            ),
                            callout("No todo cabe", "Un diagrama de un proyecto real con todos los detalles es ilegible. Al hacer ingeniería inversa, dibuja solo las clases y los miembros que necesitas para entender lo que buscas.", LessonCalloutTone.Tip),
                        ),
                    visual =
                        LessonVisualContent.UmlDiagram(
                            nodes =
                                listOf(
                                    umlClass(id = "Recepcion", cell = cell(0, 0), operations = listOf("+ avisar(cliente: Cliente, mensaje: String)")),
                                    umlClass(id = "Cliente", cell = cell(0, 1), attributes = listOf("+ nombre: String"), operations = listOf("+ registrar(mascota: Mascota)")),
                                    umlClass(id = "Vacunable", cell = cell(1, 0), operations = listOf("+ vacunar(fecha: String)*"), stereotype = "interface"),
                                    umlClass(id = "Mascota", cell = cell(1, 1), attributes = listOf("+ nombre: String")),
                                    umlClass(id = "Perro", cell = cell(2, 0, columnSpan = 2), attributes = listOf("- vacunas: MutableList<String>"), operations = listOf("+ vacunar(fecha: String)")),
                                ),
                            relations =
                                listOf(
                                    UmlRelation(from = "Recepcion", to = "Cliente", kind = UmlRelationKind.Dependency),
                                    UmlRelation(from = "Cliente", to = "Mascota", kind = UmlRelationKind.NavigableAssociation, toLabel = "*", toRole = "mascotas"),
                                    UmlRelation(from = "Perro", to = "Mascota", kind = UmlRelationKind.Inheritance),
                                    UmlRelation(from = "Perro", to = "Vacunable", kind = UmlRelationKind.Realization),
                                ),
                            caption = "El diagrama reconstruido desde el código",
                        ),
                ),
                LessonStep(
                    title = "Diseñar desde un enunciado",
                    explanation =
                        listOf(
                            paragraph(regular("Lo normal es empezar por un texto que describe el problema. El ejemplo es este: "), emphasis("«Una academia ofrece cursos de idiomas. Cada curso tiene un idioma, un nivel y un profesor, y admite hasta 12 alumnos. Un alumno puede apuntarse a varios cursos. Los alumnos con carné joven pagan menos»."), regular(" Para convertirlo en diagrama:")),
                            steps(
                                richText(regular("Subraya los "), highlight("sustantivos"), regular(". Los que tienen datos o comportamiento propios son clases (curso, profesor, alumno); los que son un simple valor, atributos (idioma, nivel).")),
                                richText(regular("Busca los "), highlight("verbos"), regular(": suelen ser operaciones. «Apuntarse a un curso» es "), inlineCode("matricular(alumno)"), regular(" en la clase que guarda la lista.")),
                                richText(regular("Las frases con «tiene», «pertenece a» o «ofrece» son asociaciones; los números del texto, multiplicidades.")),
                                richText(regular("Las frases con «es un» son herencia, pero solo si cambia el comportamiento. El carné joven solo cambia un dato: es un atributo, no una subclase.")),
                                richText(regular("Revisa que cada clase tenga una responsabilidad clara y que cada multiplicidad salga del texto.")),
                            ),
                        ),
                    visual =
                        LessonVisualContent.UmlDiagram(
                            nodes =
                                listOf(
                                    umlClassName(id = "Academia", cell = cell(0, 0)),
                                    umlClass(id = "Curso", cell = cell(1, 0), attributes = listOf("+ idioma: String", "+ nivel: String"), operations = listOf("+ matricular(alumno: Alumno): Boolean")),
                                    umlClassName(id = "Profesor", cell = cell(1, 1)),
                                    umlClass(id = "Alumno", cell = cell(2, 0), attributes = listOf("+ nombre: String", "+ carneJoven: Boolean"), operations = listOf("+ cuota(): Double")),
                                ),
                            relations =
                                listOf(
                                    UmlRelation(from = "Academia", to = "Curso", kind = UmlRelationKind.Composition, fromLabel = "1", toLabel = "*", toRole = "cursos"),
                                    UmlRelation(from = "Curso", to = "Profesor", kind = UmlRelationKind.NavigableAssociation, toLabel = "1", toRole = "profesor"),
                                    UmlRelation(from = "Curso", to = "Alumno", kind = UmlRelationKind.Aggregation, fromLabel = "*", toLabel = "0..12", toRole = "alumnos"),
                                ),
                            caption = "Una propuesta para la academia",
                        ),
                ),
                LessonStep(
                    title = "Diagramas como texto con Mermaid",
                    explanation =
                        listOf(
                            paragraph(regular("Dibujar cajas a mano en una herramienta gráfica cuesta, y el dibujo se queda viejo en cuanto cambia el código. Con "), highlight("Mermaid"), regular(" el diagrama se escribe como texto dentro de un archivo Markdown y la herramienta lo dibuja.")),
                            paragraph(regular("IntelliJ IDEA trae Mermaid de serie: crea "), inlineCode("docs/diseno.md"), regular(" en la raíz del proyecto, escribe un bloque "), inlineCode("mermaid"), regular(" y abre la vista previa con el botón de la esquina superior derecha del editor. GitHub también lo dibuja al mostrar el archivo.")),
                            explanationCode(
                                "diseno.md",
                                richText(regular("El diagrama de asociaciones y multiplicidad, en Mermaid. Las multiplicidades van entre comillas, y el texto tras los dos puntos se dibuja en el centro de la línea.")),
                                "# Diseño de la escuela",
                                "",
                                "```mermaid",
                                "classDiagram",
                                "    class Curso {",
                                "        +nombre: String",
                                "        +matricular(alumno: Alumno) Boolean",
                                "    }",
                                "    Curso --> \"1\" Profesor : tutor",
                                "    Curso --> \"0..30\" Alumno : alumnos",
                                "    Curso --> \"0..1\" Aula : aula",
                                "```",
                            ),
                            steps(
                                richText(inlineCode("-->"), regular(" asociación, "), inlineCode("--"), regular(" sin flecha y "), inlineCode("..>"), regular(" dependencia.")),
                                richText(inlineCode("*--"), regular(" composición y "), inlineCode("o--"), regular(" agregación, con el rombo en el lado del todo.")),
                                richText(inlineCode("<|--"), regular(" herencia y "), inlineCode("<|.."), regular(" realización, con el triángulo en la base.")),
                                richText(regular("Un "), inlineCode("$"), regular(" al final subraya el miembro, un "), inlineCode("*"), regular(" lo pone en cursiva y "), inlineCode("<<interface>>"), regular(" añade el estereotipo.")),
                            ),
                            callout("Un diagrama que vive con el código", "Al ser texto, el diagrama se guarda en Git junto al código y los cambios se ven en cada commit como los de un archivo Kotlin. Si cambias una clase, cambia también su diagrama en el mismo commit.", LessonCalloutTone.Tip),
                        ),
                    visual =
                        LessonVisualContent.Screenshot(
                            image = LessonScreenshot.IntellijMermaidClass,
                            description = "Vista previa de diseno.md en IntelliJ IDEA: Curso con su nombre y matricular, y flechas a Profesor (tutor, 1), Alumno (alumnos, 0..30) y Aula (aula, 0..1). Arriba a la derecha, los botones para ver solo el editor, editor y vista previa o solo la vista previa.",
                        ),
                    links =
                        listOf(
                            LessonLink("Diagramas de clases en Mermaid", "https://mermaid.js.org/syntax/classDiagram.html"),
                            LessonLink("Diagramas en Markdown con IntelliJ IDEA", "https://www.jetbrains.com/help/idea/markdown.html"),
                        ),
                ),
                LessonStep(
                    title = "Ahora te toca: el taller",
                    explanation =
                        listOf(
                            paragraph(regular("Un taller mecánico registra con este programa las reparaciones de cada vehículo. Un cliente puede traer varios vehículos y un vehículo puede volver otro día. Cada reparación tiene líneas de dos tipos: piezas, con su precio y unidades, y mano de obra, a 40 la hora.")),
                            paragraph(regular("Todo está en un solo "), inlineCode("main"), regular(" y no sabe qué vehículos son de quién. "), highlight("Diseña primero el diagrama de clases"), regular(" y después el código. Además, al terminar el programa debe mostrar cuánto ha gastado cada cliente.")),
                            callout("Guarda el proyecto", "En la lección siguiente ampliarás este mismo taller: las reparaciones tendrán un ciclo de vida y dibujarás cómo se comporta el programa.", LessonCalloutTone.Warning),
                        ),
                    visual =
                        LessonVisualContent.CodeExamples(
                            listOf(
                                LessonCodeExample(
                                    title = "Taller",
                                    lines =
                                        kotlinCode(
                                            $$"""
                                            fun main() {
                                                val clientes = mutableMapOf<String, String>()
                                                var reparaciones = 0
                                                var facturado = 0
                                                println("Matrícula (vacía para terminar):")
                                                var matricula = readln()
                                                while (matricula != "") {
                                                    var cliente = clientes[matricula]
                                                    if (cliente == null) {
                                                        println("Cliente:")
                                                        cliente = readln()
                                                        clientes[matricula] = cliente
                                                    } else {
                                                        println("Vehículo de $cliente")
                                                    }
                                                    var importe = 0
                                                    println("Línea (1 pieza, 2 mano de obra, 0 terminar):")
                                                    var tipo = readln().toInt()
                                                    while (tipo != 0) {
                                                        if (tipo == 1) {
                                                            println("Pieza:")
                                                            val pieza = readln()
                                                            println("Precio:")
                                                            val precio = readln().toInt()
                                                            println("Unidades:")
                                                            val unidades = readln().toInt()
                                                            println("$pieza: ${precio * unidades}")
                                                            importe += precio * unidades
                                                        } else if (tipo == 2) {
                                                            println("Horas:")
                                                            val horas = readln().toInt()
                                                            println("Mano de obra: ${horas * 40}")
                                                            importe += horas * 40
                                                        } else {
                                                            println("Tipo desconocido: $tipo")
                                                        }
                                                        println("Línea (1 pieza, 2 mano de obra, 0 terminar):")
                                                        tipo = readln().toInt()
                                                    }
                                                    println("Reparación de $matricula: $importe")
                                                    reparaciones++
                                                    facturado += importe
                                                    println("Matrícula (vacía para terminar):")
                                                    matricula = readln()
                                                }
                                                println("Reparaciones: $reparaciones")
                                                println("Facturado: $facturado")
                                            }
                                            """.trimIndent(),
                                        ),
                                    note = richText(regular("Un filtro de 15 por 2 y 3 horas de mano de obra suman "), inlineCode("150"), regular(". Con dos vehículos de un mismo cliente, el programa pregunta su nombre dos veces y no los relaciona.")),
                                    isCopyable = true,
                                ),
                            ),
                        ),
                    links =
                        listOf(
                            LessonLink("Diagramas de clases en Mermaid", "https://mermaid.js.org/syntax/classDiagram.html"),
                            LessonLink("Especificación de UML", "https://www.omg.org/spec/UML/"),
                        ),
                    practice =
                        LessonPractice(
                            id = "basic-uml-garage",
                            objective = richText(highlight("Diseñar con un diagrama de clases antes de programar y mantener diagrama y código de acuerdo.")),
                            statement = richText(regular("Dibuja en "), inlineCode("docs/diseno.md"), regular(" el diagrama de clases del taller con Mermaid, reorganiza el programa en clases según ese diagrama y compruébalo con pruebas.")),
                            requirements =
                                listOf(
                                    richText(regular("El diagrama recoge las clases del taller con sus atributos, operaciones, relaciones y multiplicidades, y se ve en la vista previa de IntelliJ IDEA.")),
                                    richText(regular("Si una matrícula ya existe, el programa avisa con "), inlineCode("Vehículo de Ana"), regular(". Si se escribe el nombre de un cliente que ya existe, es el mismo cliente aunque el vehículo sea otro.")),
                                    richText(inlineCode("Taller"), regular(": "), inlineCode("nuevaReparacion(matricula: String, cliente: String): Reparacion"), regular(", "), inlineCode("facturado(): Int"), regular(" y "), inlineCode("gastoDe(cliente: String): Int"), regular(".")),
                                    richText(inlineCode("Reparacion"), regular(": "), inlineCode("agregarPieza(nombre: String, precio: Int, unidades: Int)"), regular(", "), inlineCode("agregarManoDeObra(horas: Int)"), regular(" e "), inlineCode("importe(): Int"), regular(".")),
                                    richText(inlineCode("main"), regular(" lee y muestra lo mismo que antes y, al final, una línea "), inlineCode("Gasto de Ana: 430"), regular(" por cliente, en el orden en que llegaron.")),
                                    richText(regular("Cada clase va en su archivo y el diagrama coincide con el código. Crea "), inlineCode("TallerTest"), regular(" y decide qué casos necesitas para fiarte de los importes.")),
                                ),
                            expectedOutput =
                                listOf(
                                    consoleRun(
                                        consoleOutput("Matrícula (vacía para terminar):"),
                                        consoleInput("1234ABC"),
                                        consoleOutput("Cliente:"),
                                        consoleInput("Ana"),
                                        consoleOutput("Línea (1 pieza, 2 mano de obra, 0 terminar):"),
                                        consoleInput("1"),
                                        consoleOutput("Pieza:"),
                                        consoleInput("Filtro"),
                                        consoleOutput("Precio:"),
                                        consoleInput("15"),
                                        consoleOutput("Unidades:"),
                                        consoleInput("2"),
                                        consoleOutput("Filtro: 30"),
                                        consoleOutput("Línea (1 pieza, 2 mano de obra, 0 terminar):"),
                                        consoleInput("2"),
                                        consoleOutput("Horas:"),
                                        consoleInput("3"),
                                        consoleOutput("Mano de obra: 120"),
                                        consoleOutput("Línea (1 pieza, 2 mano de obra, 0 terminar):"),
                                        consoleInput("0"),
                                        consoleOutput("Reparación de 1234ABC: 150"),
                                        consoleOutput("Matrícula (vacía para terminar):"),
                                        consoleInput("5678DEF"),
                                        consoleOutput("Cliente:"),
                                        consoleInput("Luis"),
                                        consoleOutput("Línea (1 pieza, 2 mano de obra, 0 terminar):"),
                                        consoleInput("1"),
                                        consoleOutput("Pieza:"),
                                        consoleInput("Batería"),
                                        consoleOutput("Precio:"),
                                        consoleInput("90"),
                                        consoleOutput("Unidades:"),
                                        consoleInput("1"),
                                        consoleOutput("Batería: 90"),
                                        consoleOutput("Línea (1 pieza, 2 mano de obra, 0 terminar):"),
                                        consoleInput("7"),
                                        consoleOutput("Tipo desconocido: 7"),
                                        consoleOutput("Línea (1 pieza, 2 mano de obra, 0 terminar):"),
                                        consoleInput("0"),
                                        consoleOutput("Reparación de 5678DEF: 90"),
                                        consoleOutput("Matrícula (vacía para terminar):"),
                                        consoleInput("1234ABC"),
                                        consoleOutput("Vehículo de Ana"),
                                        consoleOutput("Línea (1 pieza, 2 mano de obra, 0 terminar):"),
                                        consoleInput("2"),
                                        consoleOutput("Horas:"),
                                        consoleInput("1"),
                                        consoleOutput("Mano de obra: 40"),
                                        consoleOutput("Línea (1 pieza, 2 mano de obra, 0 terminar):"),
                                        consoleInput("0"),
                                        consoleOutput("Reparación de 1234ABC: 40"),
                                        consoleOutput("Matrícula (vacía para terminar):"),
                                        consoleInput("9012GHI"),
                                        consoleOutput("Cliente:"),
                                        consoleInput("Ana"),
                                        consoleOutput("Línea (1 pieza, 2 mano de obra, 0 terminar):"),
                                        consoleInput("1"),
                                        consoleOutput("Pieza:"),
                                        consoleInput("Rueda"),
                                        consoleOutput("Precio:"),
                                        consoleInput("60"),
                                        consoleOutput("Unidades:"),
                                        consoleInput("4"),
                                        consoleOutput("Rueda: 240"),
                                        consoleOutput("Línea (1 pieza, 2 mano de obra, 0 terminar):"),
                                        consoleInput("0"),
                                        consoleOutput("Reparación de 9012GHI: 240"),
                                        consoleOutput("Matrícula (vacía para terminar):"),
                                        consoleInput(""),
                                        consoleOutput("Reparaciones: 4"),
                                        consoleOutput("Facturado: 520"),
                                        consoleOutput("Gasto de Ana: 430"),
                                        consoleOutput("Gasto de Luis: 90"),
                                    ),
                                ),
                            hints =
                                listOf(
                                    richText(regular("Subraya los sustantivos del enunciado: cliente, vehículo, reparación, línea, pieza, mano de obra y taller. Decide cuáles son clases y cuáles atributos, y dibújalas antes de escribir código.")),
                                    richText(regular("Pregúntate quién crea a quién y quién vive sin quién. Una línea de reparación no tiene sentido fuera de su reparación; un cliente existe antes de traer un coche y puede tener varios.")),
                                    richText(regular("Pieza y mano de obra son dos clases de línea que calculan su importe de forma distinta: es la situación de la lección de herencia e interfaces. El precio por hora es el mismo para todas: va subrayado.")),
                                    richText(regular("Para encontrar un cliente o un vehículo por su nombre o su matrícula, un mapa como el de la lección de conjuntos y mapas te lo da en un paso. Con un "), inlineCode("mutableMapOf"), regular(" el orden de llegada se conserva.")),
                                ),
                            extension = richText(regular("Dibuja a mano el diagrama de objetos de la ejecución de ejemplo al terminar: qué objetos hay, con qué valores y cómo se enlazan. Comprueba que encaja con tu diagrama de clases.")),
                            confirmation = richText(regular("El diagrama de "), inlineCode("docs/diseno.md"), regular(" se ve en la vista previa y coincide con el código, el programa da la salida esperada y "), inlineCode("TallerTest"), regular(" pasa.")),
                        ),
                ),
            ),
    )
