plugins {
    id("com.mikepenz.convention.android-library")
    id("com.mikepenz.convention.kotlin")
    id("com.mikepenz.convention.publishing")
}

android {
    namespace = "com.mikepenz.materialdrawer"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)

    api(libs.androidx.drawerlayout)
    api(libs.androidx.recyclerView)
    implementation(libs.androidx.cardView)
    implementation(libs.google.material)

    // add the constraintLayout used to create the items and headers
    implementation(libs.androidx.constraintLayout)

    // used to fill the RecyclerView with the items
    // and provides single and multi selection, expandable items
    // https://github.com/mikepenz/FastAdapter
    api(libs.fastAdapter.core)
    api(libs.fastAdapter.expandable)
}