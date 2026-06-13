@file:Suppress("UnstableApiUsage")
package ygdrasil.conventions

import com.android.build.api.variant.KotlinMultiplatformAndroidComponentsExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
}

val konanStubs = project.rootProject.projectDir.resolve("konan-stubs")

kotlin {
    jvmToolchain(25)

    android {}

    jvm()

    iosArm64()
    iosSimulatorArm64()

    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.all {
            linkerOpts("-F$konanStubs")
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
