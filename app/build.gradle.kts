import java.util.Properties
import java.io.File

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android") version "2.57.2"
    kotlin("plugin.serialization") version "2.2.21"
    alias(libs.plugins.google.gms.google.services)
}

// Load .env file
val envFile = File(rootDir, ".env")
val envProperties = Properties()
if (envFile.exists()) {
    envProperties.load(envFile.inputStream())
}

// Helper function to get env variable
fun getEnvVariable(key: String, default: String = "missing_key"): String {
    return envProperties.getProperty(key) ?: System.getenv(key) ?: default
}

android {
    namespace = "com.skillMatcher.buildMate"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.skillMatcher.buildMate"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        // Read Gemini API Key from .env file
        val geminiApiKey = getEnvVariable("GEMINI_API_KEY")
        buildConfigField("String", "GEMINI_API_KEY", "\"$geminiApiKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation("androidx.compose.animation:animation:1.7.5")
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)
    implementation(libs.androidx.adapters)
    implementation(libs.firebase.firestore)
    implementation(libs.coil.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    val room_version = "2.8.3"

    implementation("androidx.room:room-runtime:$room_version")

    // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
    // See Add the KSP plugin to your project
    ksp("androidx.room:room-compiler:$room_version")

    // If this project only uses Java source, use the Java annotationProcessor
    // No additional plugins are necessary
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")

    val nav_version = "2.9.6"

    implementation("androidx.navigation:navigation-compose:$nav_version")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    implementation("com.google.dagger:hilt-android:2.57.2")
    ksp("com.google.dagger:hilt-android-compiler:2.57.2")

    implementation("androidx.hilt:hilt-navigation-compose:1.3.0")
    //kapt("androidx.hilt:hilt-compiler:1.2.0")
    ksp("androidx.hilt:hilt-compiler:1.3.0")

    implementation ("com.canopas.compose-animated-navigationbar:bottombar:1.0.1")

    implementation ("com.google.accompanist:accompanist-pager:0.28.0")
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.28.0")

    // Core Retrofit
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
// Gson converter (for JSON parsing)
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
// OkHttp (network client for Retrofit)
    implementation("com.squareup.okhttp3:okhttp:5.3.0")
// OkHttp Logging Interceptor (for debugging API calls)
    implementation("com.squareup.okhttp3:logging-interceptor:5.3.0")

}