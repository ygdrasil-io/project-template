package ygdrasil.conventions

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
}

kotlin {
    jvmToolchain(25)
    
    androidTarget()
    
    jvm() // Cible pour Desktop (JVM)
    
    // Cibles iOS (Compose 1.11+ ne supporte plus x86_64)
    iosArm64()
    iosSimulatorArm64()
}

android {
    compileSdk = 37
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_25
        targetCompatibility = JavaVersion.VERSION_25
    }
}
