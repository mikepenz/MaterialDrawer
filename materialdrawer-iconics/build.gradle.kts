plugins {
    id("com.mikepenz.convention.android-library")
    id("com.mikepenz.convention.kotlin")
    id("com.mikepenz.convention.publishing")
}

android {
    namespace = "com.mikepenz.materialdrawer.iconics"
}

dependencies {
    implementation(project(":materialdrawer"))

    // used to provide out of the box icon font support. simplifies development,
    // and provides scalable icons. the core is very very light
    // https://github.com/mikepenz/Android-Iconics
    implementation(libs.iconics.core)
}