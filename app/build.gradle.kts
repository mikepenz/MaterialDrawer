plugins {
    id("com.mikepenz.convention.android-application")
    id("com.mikepenz.convention.kotlin")
    id("com.mikepenz.aboutlibraries.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.mikepenz.materialdrawer.app"

    defaultConfig {
        multiDexEnabled = true
        setProperty("archivesBaseName", "MaterialDrawer-v$versionName-c$versionCode")
    }

    productFlavors {
    }

    buildFeatures {
        viewBinding = true
    }

    packaging {
        resources {
            excludes.add("META-INF/library-core_release.kotlin_module")
            excludes.add("META-INF/library_release.kotlin_module")
        }
    }
}

dependencies {
    implementation(project(":materialdrawer"))
    implementation(project(":materialdrawer-iconics"))
    implementation(project(":materialdrawer-nav"))

    implementation(libs.google.material)

    implementation(libs.androidx.cardView)
    implementation(libs.androidx.recyclerView)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    // used to showcase how to load images
    implementation("io.coil-kt:coil:2.7.0")

    //used to provide different itemAnimators for the RecyclerView
    //https://github.com/mikepenz/ItemAnimators
    implementation(libs.itemAnimators.core)

    // used to provide out of the box icon font support. simplifies development,
    // and provides scalable icons. the core is very very light
    // https://github.com/mikepenz/Android-Iconics
    implementation(libs.iconics.core)

    // used to generate the Open Source section
    // https://github.com/mikepenz/AboutLibraries
    implementation(baseLibs.aboutlibraries.view)

    // used to provide the MiniDrawer to normal Drawer crossfade effect via a SlidingPane layout
    // --> https://github.com/mikepenz/MaterialDrawer/blob/develop/app/src/main/java/com/mikepenz/materialdrawer/app/MiniDrawerActivity.java
    // https://github.com/mikepenz/Crossfader
    implementation("com.mikepenz:crossfader:1.6.0@aar")
    // used to provide the two step crossfade DrawerLayout. Which allows to have a mini layout which transforms to a normal layout within the drawer
    // --> https://github.com/mikepenz/MaterialDrawer/blob/develop/app/src/main/java/com/mikepenz/materialdrawer/app/CrossfadeDrawerLayoutActvitiy.java
    // https://github.com/mikepenz/CrossfadeDrawerLayout
    implementation("com.mikepenz:crossfadedrawerlayout:1.1.0@aar")

    // icon fonts used inside the sample
    // https://github.com/mikepenz/Android-Iconics
    implementation("com.mikepenz:google-material-typeface:4.0.0.2-kotlin@aar")
    implementation("com.mikepenz:fontawesome-typeface:5.13.3.0-kotlin@aar")
    implementation("com.mikepenz:octicons-typeface:11.1.0.0-kotlin@aar")


    implementation("androidx.multidex:multidex:2.0.1")

    implementation("androidx.slidingpanelayout:slidingpanelayout:1.1.0") {
        version {
            strictly("1.1.0")
        }
    }
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")


}

configurations.configureEach {
    resolutionStrategy.force(libs.fastAdapter.core)
    resolutionStrategy.force(libs.iconics.core)
}