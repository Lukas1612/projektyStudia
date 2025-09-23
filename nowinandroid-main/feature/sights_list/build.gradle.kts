plugins {
    alias(libs.plugins.nowinandroid.android.feature)
    alias(libs.plugins.nowinandroid.android.library.compose)
    alias(libs.plugins.nowinandroid.android.library.jacoco)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "com.example.sights_list"
}

dependencies {
    implementation(libs.accompanist.permissions)
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.notifications)
    implementation(projects.feature.sightMap)
    implementation(projects.feature.locationTracking)

    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.robolectric)
    testImplementation(projects.core.testing)

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(projects.core.testing)
    implementation(libs.coil.compose.v240)
    //implementation("io.coil-kt:coil-compose:2.4.0")

    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)
}