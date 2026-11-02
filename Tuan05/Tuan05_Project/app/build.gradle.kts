plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.example.tuan05project"
    compileSdk { version = release(37) }
    defaultConfig {
        applicationId = "com.example.tuan05project"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures { compose = true; buildConfig = true }
    testOptions { unitTests.isReturnDefaultValues = true }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation("androidx.compose.ui:ui-text-google-fonts")
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.10.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.10.2")
    implementation("io.coil-kt:coil-compose:2.7.0")

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-installations")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-config")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.android.gms:play-services-ads:25.5.0")
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
}
