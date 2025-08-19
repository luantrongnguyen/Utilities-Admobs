plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
    id("maven-publish") // Để publish lên JitPack sau
}

android {
    namespace = "com.luantrongnguyen.ultis_admobs"
    compileSdk = 36

    defaultConfig {
        minSdk = 27

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    }
    buildFeatures {
        compose = true
    }
    lint {
        abortOnError = false // Prevent Lint errors from failing the build
        checkReleaseBuilds = false // Disable Lint for release builds
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.process)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Jetpack Compose
    implementation("androidx.compose.ui:ui:1.6.0") // Phiên bản mới nhất
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.0")
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.0")

    // MVVM: Lifecycle và ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0") // ViewModel cho Compose
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")

    // Google AdMob
    implementation("com.google.android.gms:play-services-ads:23.2.0") // Phiên bản mới nhất (kiểm tra tại developers.google.com/admob/android/sdk)

    // Các phụ thuộc khác nếu cần (ví dụ: Coroutines cho async trong MVVM)
    implementation("androidx.compose.foundation:foundation:1.6.7") // For Box
    implementation("androidx.compose.material3:material3:1.2.1") // For Text
    implementation("androidx.compose.ui:ui:1.6.7") // For Modifier, Alignment
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
}