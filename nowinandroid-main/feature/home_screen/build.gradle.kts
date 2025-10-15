plugins {
    alias(libs.plugins.nowinandroid.android.feature)
    alias(libs.plugins.nowinandroid.android.library.compose)
    alias(libs.plugins.nowinandroid.android.library.jacoco)
    alias(libs.plugins.roborazzi)
}
android {
    namespace = "com.example.home_screen"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)

    implementation(projects.feature.sightsList)
    implementation(projects.feature.locationTracking)
    implementation(projects.feature.permissions)

    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.robolectric)
    testImplementation(projects.core.testing)

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(projects.core.testing)
}