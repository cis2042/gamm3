pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // For MediaPipe
        maven { url = uri("https://storage.googleapis.com/download.tensorflow.org/models") }
    }
}

rootProject.name = "GemmaMessenger"
include(":app")
