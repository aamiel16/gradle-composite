rootProject.name = "composite"

includeBuild("../plugin-1") // Ordering is important for plugin
includeBuild("../module-1")
includeBuild("../module-2")
includeBuild("../dependency-1")
