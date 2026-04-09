plugins {
    alias(baseLibs.plugins.conventionPlugin)

    alias(baseLibs.plugins.kotlinAndroid) apply false
    alias(baseLibs.plugins.androidApplication) apply false
    alias(baseLibs.plugins.androidLibrary) apply false

    alias(baseLibs.plugins.dokka)
    alias(baseLibs.plugins.aboutLibraries) apply false
    alias(baseLibs.plugins.mavenPublish) apply false

    alias(libs.plugins.navSafeArgs) apply false
}

subprojects {
    plugins.withId("com.android.library") {
        extensions.configure<com.android.build.gradle.LibraryExtension>("android") {
            lint {
                sarifReport = true
            }
        }
    }
    plugins.withId("com.android.application") {
        extensions.configure<com.android.build.gradle.internal.dsl.BaseAppModuleExtension>("android") {
            lint {
                sarifReport = true
            }
        }
    }
}