pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
    buildscript {
        dependencies {
            classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.4.0")
        }
    }
    plugins {
        id("org.jetbrains.kotlin.multiplatform") version "2.4.0" apply false
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

configure<org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xopt-in=org.jetbrains.kotlin.gradle.plugins.KotlinNativeCacheApi")
    }
}

rootProject.name = "kmp-starter-pack"
include(":shared")
