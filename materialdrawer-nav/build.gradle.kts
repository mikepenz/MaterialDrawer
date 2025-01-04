plugins {
    id("com.mikepenz.convention.android-library")
    id("com.mikepenz.convention.kotlin")
    id("com.mikepenz.convention.publishing")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.mikepenz.materialdrawer.nav"
}

dependencies {
    implementation(project(":materialdrawer"))

    implementation(libs.androidx.navigation.runtime)
    implementation(libs.androidx.navigation.ui)
}