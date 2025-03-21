import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
}
val properties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.example.jaemebody"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.jaemebody"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"${properties.getProperty("KAKAO_NATIVE_APP_KEY")}\"")
        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = properties.getProperty("KAKAO_NATIVE_APP_KEY")
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.core:core-splashscreen:1.0.1") // splash screen

    implementation(platform("com.google.firebase:firebase-bom:33.7.0")) // firebase
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth-ktx")

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7") // lifecycle compose

    implementation("com.google.firebase:firebase-firestore") // firestore

    implementation("com.kakao.sdk:v2-user:2.20.6") // 카카오 로그인 API 모듈
}
