rootProject.name = "MaterialDrawer"

// enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }

    versionCatalogs {
        create("baseLibs") {
            from("com.mikepenz:version-catalog:0.1.3")
        }
    }
}


include(":app")
include(":materialdrawer")
include(":materialdrawer-iconics")
include(":materialdrawer-nav")