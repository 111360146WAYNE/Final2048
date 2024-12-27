pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        flatDir {
            //dir("${project(":unityLibrary").projectDir}/libs")
            dirs("./unityLibrary/Libs")

        }
    }
}


rootProject.name = "Final_Project"
include(":app")
include(":unityLibrary")
include(":unityLibrary:mobilenotifications.androidlib")
project(":unityLibrary").projectDir = File("unityLibrary")
