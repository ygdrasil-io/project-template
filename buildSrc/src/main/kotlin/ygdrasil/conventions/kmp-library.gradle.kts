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

    val uiUtilitiesStub = project.layout.buildDirectory.dir("konan-stubs/UIUtilities.framework").map { dir ->
        dir.asFile.mkdirs()
        val tbd = dir.file("UIUtilities.tbd")
        tbd.asFile.writeText("""---
!tapi-tbd
tbd-version:     4
targets:         [ arm64-ios-simulator, x86_64-ios-simulator ]
install-name:    '/System/Library/SubFrameworks/UIUtilities.framework/UIUtilities'
current-version: 1.0
exports:
  - targets:         [ arm64-ios-simulator, x86_64-ios-simulator ]
    re-exports:      [ '/System/Library/Frameworks/UIKit.framework/UIKit' ]
""")
        dir.asFile.absolutePath
    }

    targets.matching { it.name == "iosSimulatorArm64" || it.name == "iosX64" }.configureEach {
        (this as KotlinNativeTarget).binaries.all {
            linkerOpts("-F${uiUtilitiesStub.get()}")
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
