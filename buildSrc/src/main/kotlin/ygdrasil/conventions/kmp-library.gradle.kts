package ygdrasil.conventions

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
}

kotlin {
    jvmToolchain(25)
    
    jvm() // Cible pour Desktop (JVM)
    
    // Cibles iOS
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    androidLibrary {
        compileSdk = 35
        minSdk = 24
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
}
