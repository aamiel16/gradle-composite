listOf("clean").forEach { taskName ->
    tasks.register(taskName) {
        gradle.includedBuilds.forEach { includedBuild ->
            dependsOn(includedBuild.task(":$taskName"))
        }
    }
}