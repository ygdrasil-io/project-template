@file:Suppress("UnstableApiUsage")
package ygdrasil.conventions

import com.android.build.api.variant.KotlinMultiplatformAndroidComponentsExtension
import org.jetbrains.kotlin.gradle.plugins.KotlinNativeCacheApi

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
}

kotlin {
    jvmToolchain(25)

    android {}

    jvm()

    iosArm64 {
        binaries.framework {
            @OptIn(KotlinNativeCacheApi::class)
            setDisableNativeCache(true)
        }
    }
    iosSimulatorArm64 {
        binaries.framework {
            @OptIn(KotlinNativeCacheApi::class)
            setDisableNativeCache(true)
        }
    }
}

extensions.configure<KotlinMultiplatformAndroidComponentsExtension> {
    finalizeDsl(
        org.gradle.api.Action<com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension> {
            namespace = "io.ygdrasil.shared"
            compileSdk = 37
            minSdk = 24
        }
    )
}
