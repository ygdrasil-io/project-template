@file:Suppress("UnstableApiUsage")
package ygdrasil.conventions

import com.android.build.api.variant.KotlinMultiplatformAndroidComponentsExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
}

kotlin {
    jvmToolchain(25)

    android {}

    jvm()

    iosArm64()
    iosSimulatorArm64()

    targets.withType<KotlinNativeTarget>().configureEach {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    freeCompilerArgs.addAll(
                        "-Xoverride-konan-properties=minVersion.ios=18.5",
                        "-Xoverride-konan-properties=minVersion.ios_simulator=18.5"
                    )
                }
            }
        }
    }

    val simulatorSdk: String? = try {
        project.providers.exec {
            commandLine("xcrun", "--sdk", "iphonesimulator", "--show-sdk-path")
        }.standardOutput.asText.orNull?.trim()
    } catch (_: Exception) { null }

    if (simulatorSdk != null) {
        val subFrameworksDir = "$simulatorSdk/System/Library/SubFrameworks"
        targets.matching { it.name == "iosSimulatorArm64" || it.name == "iosX64" }.configureEach {
            (this as KotlinNativeTarget).binaries.all {
                linkerOpts("-F$subFrameworksDir")
            }
        }
    }
}

extensions.configure<KotlinMultiplatformAndroidComponentsExtension> {
    finalizeDsl(
        org.gradle.api.Action<com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension> {
            namespace = "io.ygdrasil.shared"
            compileSdk = 37
            minSdk = 24
            withHostTest {}
        }
    )
}
