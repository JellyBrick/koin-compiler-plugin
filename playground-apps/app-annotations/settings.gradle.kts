enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
}

rootProject.name = "koin-stress-test"

include(":app")
include(":core:common")
include(":core:model")
include(":core:database")
include(":core:datastore")
include(":core:network")
include(":core:data")
include(":core:domain")
include(":core:analytics")
include(":core:notifications")
include(":feature:home")
include(":feature:bookmarks")
include(":feature:settings")
include(":feature:detail")
include(":sync:work")
