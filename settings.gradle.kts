rootProject.name = "Hyperstream-Leonardo"

buildCache {
    local {
        isEnabled = true
        directory = File(rootDir, ".gradle/build-cache")
    }
}
