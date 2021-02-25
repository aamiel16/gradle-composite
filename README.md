# gradle-composite

## Scenario:
1. Monorepo of microservices with each folder can be a module (`dependent-1`, `dependent-2`), library (`dependency-1`), plugin (`plugin-1`)
2. We don't want to publish library / plugin to Maven, keep everything local
3. Each module can be worked on as individual projects
4. To satisfy points `2.` and `3.`, each module is a composite build which include builds of library and plugin
5. For scenarios where we want to refactor, we have the `composite` folder / project that includes all modules, library and plugin.
   We would just open the `composite` project in IntelliJ.

## Components:
- `composite`: composite build project that `buildInclude()`'s `dependency-1`, `module-1`, `module-2`, `plugin-1`. Mainly use for refactoring common code, and testing if any module breaks
- `dependency-1`: a Kotlin library that exposes a Library class. Used by `module-1` & `module-2` in `dependencies {}`
- `plugin-1`: a Gradle plugin that registers a greeting task. Used by `module-2` in `plugins {}`
- `module-1`: a composite build project & Kotlin application that uses `dependency-1`
- `module-2`: a composite build project & Kotlin application that uses `dependency-1` and `plugin-1`

## Issues
To reproduce, open the `composite` folder in IntelliJ. All the listed issues have it as prerequisite
```
Issue 1:  Ordering of `includeBuild` matters if a including a plugin.
          As workaround, put `includeBuild` of plugins as first lines
```

```
Issue 2:  Running `test` task for `dependency-1` or `plugin-1` module fails with error:
          Included build in /composite-full/dependency-1 has the same root project name 'dependency-1' as the main build.

          As workaround, add if check for each dependent module `settings.gradle.kts`:
              if (gradle.parent == null) {
                  includeBuild("../plugin-1")
                  includeBuild("../dependency-1")
              }
```

```
Issue 3:  Running `test` task for `plugin-1` fails with error:
          FAILURE: Build failed with an exception.
          * Where:
          Build file '/composite-full/module-2/build.gradle.kts' line: 9
          * What went wrong:
          Plugin [id: 'org.company.plugin.greeting'] was not found in any of the following sources:
          - Gradle Core Plugins (plugin is not in 'org.gradle' namespace)
          - Included Builds (None of the included builds contain this plugin)
          - Plugin Repositories (plugin dependency must include a version number for this source)
```
