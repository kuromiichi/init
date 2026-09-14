# DAM Prep feature workflow

The primary agent is the coordinator. For every feature, follow the workflow below, integrate specialist results, perform the mandatory clean verification, and close the work with a related commit. Use reasonable judgment for tiny follow-up fixes, but do not skip a required stage merely because an agent is available or unavailable.

## Product and engineering principles

- Keep the learning path linear, programming-focused, and appropriate for DAM preparation.
- Preserve the established Compose Multiplatform and Material 3 visual language.
- Prefer idiomatic Kotlin and native framework patterns over custom infrastructure.
- Reuse existing components and responsibilities before adding new abstractions.
- Keep changes scoped; preserve unrelated user work and avoid opportunistic refactors.
- Treat generated files under `build/` as disposable outputs, never as source changes.

## 1. Define behavior

Clarify the objective, expected user experience, acceptance criteria, and scope. Separate the feature being built now from possible future improvements. Resolve ambiguity when it would materially change the result; otherwise make and state a reasonable bounded assumption.

## 2. Research

Use `researcher` for bounded repository investigation and authoritative external documentation. Review official Kotlin, Compose Desktop or Compose Multiplatform, Material 3, Room, Koin, Gradle, and other relevant sources. Contrast educational content with the [Programación 1DAM](https://github.com/joseluisgs/Programacion-00-2022-2023) and [Entornos de Desarrollo 1DAM](https://github.com/joseluisgs/EntornosDesarrollo-00-2022-2023) reference repositories, and with the BOE or other authoritative curriculum sources when applicable. State which references were relevant to the feature; do not force content from an unrelated module or copy source wording. Use curriculum and learning outcomes to guide scope internally, but do not expose curricular connections, RA labels, BOE links, or similar traceability in the learner-facing lesson unless the user explicitly requests it. Do not repeat sound research without a concrete gap or contradiction.

## 3. Design the solution

Use `architect_reviewer` in ARCHITECTURE mode when a separate architectural pass adds value. Seek an idiomatic, native solution coherent with the existing architecture. Review models, responsibilities, packages, reusable components, state, lifecycle, and persistence. The coordinator owns the final design decision and provides a bounded plan.

## 4. Implement

Use `implementer` for a bounded implementation after behavior and design are clear. Give it the objective, acceptance criteria, plan, relevant files, constraints, and research conclusions. Make small, cohesive changes, preserve established visual patterns, and add or update tests for affected logic. Do not allow concurrent writing agents to edit overlapping files.

## 5. Verify technically

Use `tester` for focused tests, compilation, Ktlint, and diff inspection while iterating. Review the final diff and accidental changes. The coordinator must always run this exact clean verification before closing a feature:

```sh
./gradlew --stop
./gradlew --no-daemon clean build
```

Confirm compilation, tests, Ktlint, and KSP from that invocation. The clean build is particularly important because stale Compose resources can produce duplicate `Res` declarations. Never edit individual generated files under `build/`.

Do not run Gradle invocations concurrently. An interrupted or overlapping `run`, test, format, or build can leave compiler processes waiting after KSP and make a healthy build look stuck. Before retrying an unexplained wait, stop or terminate the previous invocation, run `./gradlew --stop`, and then use one bounded Gradle command. Report the last completed task instead of assuming a compiler failure.

## 6. Test the experience

Use `experience_qa` for visual and content QA when the required app and computer-use tools are available. On macOS, do not rely on `./gradlew run` for computer-use QA: it exposes a generic `net.java.openjdk.java` process that the automation surface may not recognize as DAM Prep. After the clean build passes, run `./gradlew createDistributable`, open the generated `.app` under `build/compose/binaries/main/app/`, and attach computer use to that native application identity. Packaging is a QA prerequisite, not a substitute for the mandatory clean build, and generated packages remain disposable outputs.

Because this checkout lives in a File Provider-backed Desktop folder, packaging can recreate numbered duplicates such as `Res 2.kt` or `git 2.svg` even after a successful clean build. If that happens, stop Gradle and package from a disposable copy under a local temporary directory, excluding `.git`, `.gradle`, and `build`; never delete or edit individual generated files. Run the mandatory clean build again in the real checkout after the packaging attempt so its final verification state is authoritative.

Manually exercise affected flows, alternate states, navigation, keyboard and focus behavior, resizing, relevant platforms, legibility, visual consistency, Material 3 consistency, accessibility, and educational content. A successful build or package is not evidence that visual QA passed. The coordinator must state any coverage that could not be verified.

## 7. Iterate

Classify user and QA feedback as behavioral, visual, content, testing, or architecture-related. Send bounded corrections to the cheapest suitable specialist, escalate architecture decisions to the coordinator, and rerun the verification affected by each correction. Do not repeat unrelated research.

## 8. Review and commit

Use `architect_reviewer` in REVIEW mode for the final independent review. Give it the original requirements, acceptance criteria, implementation summary, complete relevant diff, test and build results, and QA observations. Resolve BLOCKER and IMPORTANT findings before closing; judge MINOR findings in context and rerun affected verification after fixes.

Stage only related changes. Run the repository pre-commit workflow and create a conventional commit in Spanish using `feat:`, `fix:`, `refactor:`, `docs:`, `test:`, `chore:`, or another appropriate type. Never include unrelated modifications in the commit and never amend or rewrite history without an explicit request.

## Delegation rules

- The natural specialist split is research, architecture/review, implementation, tests, and visual/content QA; the coordinator integrates their results.
- Keep delegated tasks narrow and send only the context each specialist needs.
- Parallelize only independent work, normally read-only investigation.
- Do not spawn agents merely because they are available.
- Do not repeat work a specialist completed successfully.

## Model and cost policy

- Keep clarified behavior, integration, difficult failures, final clean verification, and commit ownership with the primary Sol coordinator.
- Prefer Terra Medium for bounded implementation and debugging after the design is clear.
- Prefer Terra Low for narrow research, repository inspection, focused tests, lint, deterministic validation, and experience QA.
- Use `architect_reviewer` on Sol Medium for architecture and independent final review.
- Avoid long mechanical command loops in Sol and avoid repeating successful subagent work.
