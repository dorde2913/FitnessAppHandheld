plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    //kotlin("kapt")
    id("com.google.dagger.hilt.android")
    //kotlin("kapt") // Required for annotation processing
    id("com.google.devtools.ksp")
    kotlin("kapt")
    kotlin("plugin.serialization") version "1.9.22"
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.example.fitnessapplicationhandheld"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.jimapp"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
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
    implementation(libs.play.services.wearable)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)




    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // For Jetpack Compose integration
    implementation(libs.hilt.navigation.compose)

    // If using ViewModel with Hilt
    implementation(libs.lifecycle.viewmodel.ktx)

    implementation(libs.kotlinx.serialization.json) // Latest version
    implementation(libs.room.common)
    implementation(libs.room.ktx)

    implementation(libs.work.runtime.ktx)
    implementation(libs.hilt.common)
    implementation(libs.hilt.work)
    implementation(libs.room.runtime) // Latest version
    ksp(libs.room.compiler) // For Java
    ksp(libs.room.compiler) // For Kotlin
    implementation(libs.room.ktx) // If using Kotlin Coroutines
    implementation(libs.accompanist.pager)


    implementation(libs.navigation.compose)

    //vico graphs
    implementation(libs.vico.compose)
    implementation(libs.vico.compose.m2)
    implementation(libs.vico.compose.m3)

    implementation(libs.datastore.preferences)


}