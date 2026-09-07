# DAM Prep

DAM Prep es una aplicación de escritorio de aprendizaje guiado para preparar contenidos de **Desarrollo de Aplicaciones Multiplataforma (DAM)** desde cero.

El proyecto se centra inicialmente en los módulos de **Programación** y **Entornos de Desarrollo**. Las explicaciones se presentan en pasos breves, con ejemplos y apoyos visuales, para construir una base sólida antes de avanzar hacia ejercicios y proyectos más completos.

## Objetivos

- Introducir la programación sin asumir conocimientos previos.
- Relacionar cada concepto con las herramientas utilizadas en un entorno de desarrollo real.
- Aprender Kotlin sobre la JVM utilizando IntelliJ IDEA, Gradle y Git.
- Avanzar de forma progresiva desde los fundamentos hasta la orientación a objetos, las colecciones, las pruebas, las interfaces y un proyecto aplicado.
- Mantener el contenido relacionado con los resultados de aprendizaje y criterios de evaluación del currículo oficial de DAM.

DAM Prep no pretende reproducir literalmente un temario existente ni seguir necesariamente su mismo orden. Los contenidos se seleccionan, reorganizan y adaptan al formato de tutoría guiada de la aplicación.

## Estado actual

El proyecto se encuentra en una fase inicial funcional.

Actualmente incluye:

- onboarding y preferencias persistentes de nombre, tratamiento, sistema operativo y tema;
- temas claro, oscuro y según el sistema;
- navegación entre portada, listado y lecciones;
- navegación por botones y teclado dentro de las lecciones;
- catálogo provisional de 24 lecciones;
- tres lecciones con contenido:
    1. Bienvenida y mapa del camino;
    2. Qué es el software y cómo se construye;
    3. Preparar el entorno de trabajo;
- componentes visuales para diagramas, ejemplos de código, herramientas y capturas;
- comprobación práctica final en la lección de preparación del entorno;
- pruebas automatizadas de los modelos, preferencias y catálogo de contenidos;
- verificación automática del formato, las pruebas y la compilación.

La pantalla de apuntes y el progreso persistente todavía están pendientes. La finalización existe en la lección de preparación del entorno, pero aún no se guarda entre sesiones ni se aplica a todo el catálogo.

## Enfoque didáctico

Cada lección se divide en pantallas cortas. La intención es presentar una idea importante cada vez y combinar:

- explicación conceptual;
- vocabulario técnico contextualizado;
- ejemplos de código;
- esquemas y recorridos visuales;
- uso gradual de herramientas profesionales;
- actividades prácticas para comprobar lo aprendido.

El orden de DAM Prep responde a esta progresión didáctica propia. Por ello puede diferir tanto del orden de las fuentes de referencia como de la distribución utilizada por un centro educativo concreto.

## Fuentes y marco curricular

La principal referencia didáctica del proyecto es:

- [Programación 1DAM, curso 2022/2023, de José Luis González Sánchez](https://github.com/joseluisgs/Programacion-00-2022-2023).

Este material organiza contenidos de programación para primero de DAM y relaciona el temario con sus resultados de aprendizaje. DAM Prep lo utiliza como orientación, pero adapta la selección, la redacción, el orden y la presentación a su propio formato.

Como marco curricular oficial se consultan:

- [Real Decreto 450/2010, de 16 de abril](https://www.boe.es/buscar/doc.php?id=BOE-A-2010-8067), por el que se establece el título de Técnico Superior en Desarrollo de Aplicaciones Multiplataforma y sus enseñanzas mínimas.
- [Orden EDU/2000/2010, de 13 de julio](https://www.boe.es/buscar/doc.php?id=BOE-A-2010-11888), por la que se establece el currículo del ciclo formativo en el ámbito de gestión del Ministerio de Educación.

Para los detalles técnicos se prioriza, cuando corresponde, la documentación oficial de las herramientas utilizadas, como [Kotlin](https://kotlinlang.org/docs/home.html), [IntelliJ IDEA](https://www.jetbrains.com/help/idea/getting-started.html), [Gradle](https://docs.gradle.org/current/userguide/userguide.html) y [Git](https://git-scm.com/doc).

### Atribución del material educativo

El repositorio de referencia de José Luis González Sánchez está publicado bajo la licencia [Creative Commons Reconocimiento-NoComercial-CompartirIgual 4.0 Internacional](https://creativecommons.org/licenses/by-nc-sa/4.0/deed.es). Los contenidos educativos de DAM Prep que adapten ese material deben conservar la atribución, el uso no comercial y las mismas condiciones de licencia.

El código fuente de DAM Prep no tiene todavía una licencia publicada. Esta distinción deberá conservarse al preparar una distribución pública del proyecto.

## Tecnologías

- Kotlin/JVM
- Compose Multiplatform para escritorio
- Material 3
- Navigation Compose
- DataStore Preferences
- Gradle con Kotlin DSL
- Kotlin Test y JUnit Platform

La compilación utiliza una toolchain de Java 25.

## Ejecutar el proyecto

El repositorio incluye Gradle Wrapper, por lo que no es necesario instalar Gradle por separado.

En macOS o Linux:

```bash
./gradlew run
```

En Windows:

```powershell
gradlew.bat run
```

También se puede abrir el proyecto directamente con IntelliJ IDEA y ejecutar la configuración principal de la aplicación.

## Verificación

Ejecutar las pruebas:

```bash
./gradlew test
```

Compilar y verificar el proyecto completo:

```bash
./gradlew build
```

La tarea `build` también comprueba el estilo del código. Para aplicar automáticamente el formato acordado:

```bash
./gradlew ktlintFormat
```

Para generar el instalador correspondiente al sistema operativo actual:

```bash
./gradlew packageDistributionForCurrentOS
```

Compose Desktop no permite generar instaladores para otro sistema operativo: los paquetes de macOS, Windows y Linux deben construirse en cada plataforma respectiva.

Si macOS rechaza la firma porque la carpeta está gestionada por un servicio de sincronización, el código puede mantenerse en su ubicación y dirigir únicamente los archivos de distribución a una carpeta local:

```bash
./gradlew packageDistributionForCurrentOS -PnativeDistributionOutput=/private/tmp/dam-prep-distribution
```

## Estructura principal

```text
src/main/kotlin/dev/kuromiichi/damprep/
├── data/
│   ├── lessons/       Catálogo, modelos y contenidos de las lecciones
│   └── preferences/   Preferencias persistentes del usuario
├── ui/
│   ├── common/        Componentes compartidos y visuales didácticos
│   ├── home/          Portada y resumen de progreso
│   ├── lessons/       Listado y reproducción de lecciones
│   ├── navigation/    Rutas y navegación de la aplicación
│   ├── notes/         Área de apuntes, todavía provisional
│   ├── onboarding/    Configuración inicial
│   ├── settings/      Preferencias de nombre, tratamiento y tema
│   ├── theme/         Sistema visual
│   └── window/        Adaptación al tamaño de ventana
└── App.kt             Composición principal de la aplicación
```

Los recursos gráficos se encuentran en `src/main/composeResources/drawable` y las pruebas en `src/test/kotlin`.

## Próximos pasos

1. Documentar la relación entre cada lección, sus fuentes y los resultados de aprendizaje correspondientes.
2. Guardar el progreso, el último paso visitado y la finalización de las prácticas.
3. Desarrollar la lección de primeros programas sin repetir la comprobación del entorno.
4. Extender las actividades prácticas y los criterios de finalización al resto del catálogo.
5. Implementar apuntes persistentes.
6. Completar los metadatos, iconos y pruebas de los instaladores nativos.

## Contribución

El proyecto todavía está definiendo su modelo de contenidos y su alcance. Antes de añadir una lección conviene comprobar:

- qué objetivo didáctico persigue;
- con qué resultado de aprendizaje o criterio curricular se relaciona;
- qué fuentes se han utilizado;
- qué conocimientos previos necesita;
- qué actividad permitirá comprobar que el concepto se ha entendido.
