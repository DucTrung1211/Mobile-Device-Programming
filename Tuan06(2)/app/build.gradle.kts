import java.util.Properties
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}
val secrets = Properties().apply {
    val source = rootProject.file("secrets.properties")
    if (source.exists()) source.inputStream().use { load(it) }
}
android {
    namespace = "com.uth.week6.permissions"
    compileSdk { version = release(37) }
    defaultConfig {
        applicationId = "com.uth.week6.permissions"
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
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.coil.compose)
    implementation("io.coil-kt:coil-svg:2.7.0")
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.okhttp.mockwebserver)
}
tasks.withType<Test>().configureEach {
    systemProperty("liveApi", providers.gradleProperty("liveApi").getOrElse("false"))
    systemProperty("taskToken", secrets.getProperty("TASK_TOKEN", ""))
    testLogging { showStandardStreams = true }
}
