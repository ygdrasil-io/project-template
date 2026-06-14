# Maven Central Publishing – New Method Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Migrate from legacy Sonatype credentials to Central Portal API tokens by adopting `io.github.gradle-nexus.publish-plugin` while keeping snapshot publishing.

**Architecture:** Add the Nexus Publish Plugin at the root project level to manage staging lifecycle, remove inline `repositories {}` from the convention plugin, and rename credentials to `mavenCentralUsername`/`mavenCentralPassword`.

**Tech Stack:** Gradle, `io.github.gradle-nexus.publish-plugin:1.3.0`, Kotlin DSL

---

### Task 1: Add nexus-publish plugin to version catalog

**Files:**
- Modify: `gradle/libs.versions.toml`
- Modify: `buildSrc/build.gradle.kts`

- [ ] **Step 1: Add version + plugin to `gradle/libs.versions.toml`**

In `[versions]` add:
```
nexus-publish = "1.3.0"
```

In `[plugins]` add:
```
nexus-publish = { id = "io.github.gradle-nexus.publish-plugin", version.ref = "nexus-publish" }
```

- [ ] **Step 2: Add dependency in `buildSrc/build.gradle.kts`**

```kotlin
implementation("io.github.gradle-nexus.publish-plugin:io.github.gradle-nexus.publish-plugin.gradle.plugin:${libs.versions.nexus.publish.get()}")
```

- [ ] **Step 3: Commit**

```
git add gradle/libs.versions.toml buildSrc/build.gradle.kts
git commit -m "chore: add nexus-publish plugin to version catalog"
```

---

### Task 2: Configure Nexus Publish Plugin in root build

**Files:**
- Modify: `build.gradle.kts` (root)

- [ ] **Step 1: Apply plugin and configure nexusPublishing**

```kotlin
plugins {
    alias(libs.plugins.nexus.publish)
}

nexusPublishing {
    repositories {
        create("mavenCentral") {
            nexusUrl.set(uri("https://central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(project.findProperty("mavenCentralUsername") as? String ?: System.getenv("MAVEN_CENTRAL_USERNAME") ?: "")
            password.set(project.findProperty("mavenCentralPassword") as? String ?: System.getenv("MAVEN_CENTRAL_PASSWORD") ?: "")
        }
    }
}
```

- [ ] **Step 2: Commit**

```
git add build.gradle.kts
git commit -m "feat: configure nexus-publish plugin with Central Portal + snapshots"
```

---

### Task 3: Update publishing convention plugin

**Files:**
- Modify: `buildSrc/src/main/kotlin/ygdrasil/conventions/kmp-publish.gradle.kts`

- [ ] **Step 1: Remove `repositories` block and update credential names**

Remove lines 41-53 (`publishing.repositories {}` block) entirely.

Update line 49 credential references (no longer needed since the block is removed).

The file should become:

```kotlin
package ygdrasil.conventions

plugins {
    id("maven-publish")
    id("signing")
}

group = "io.ygdrasil.shared"
version = "1.0.0-SNAPSHOT"

publishing {
    publications.withType<MavenPublication>().configureEach {
        pom {
            name.set("KMP Starter Pack Shared Library")
            description.set("Shared library logic for KMP Starter Pack")
            url.set("https://github.com/ygdrasil-io/project-template")

            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }

            developers {
                developer {
                    id.set("ygdrasil-io")
                    name.set("Ygdrasil team")
                    email.set("contact@ygdrasil.com")
                }
            }

            scm {
                connection.set("scm:git:git://github.com/ygdrasil-io/project-template.git")
                developerConnection.set("scm:git:ssh://github.com/ygdrasil-io/project-template.git")
                url.set("https://github.com/ygdrasil-io/project-template")
            }
        }
    }
}

signing {
    val signingKey = project.findProperty("signingKey") as? String ?: System.getenv("SIGNING_KEY")
    val signingPassword = project.findProperty("signingPassword") as? String ?: System.getenv("SIGNING_PASSWORD")
    if (!signingKey.isNullOrEmpty() && !signingPassword.isNullOrEmpty()) {
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications)
    }
}
```

- [ ] **Step 2: Commit**

```
git add buildSrc/src/main/kotlin/ygdrasil/conventions/kmp-publish.gradle.kts
git commit -m "refactor: remove repositories block from publish convention (delegated to nexus plugin)"
```

---

### Task 4: Update gradle.properties documentation

**Files:**
- Modify: `gradle.properties`

- [ ] **Step 1: Update credential placeholders**

Replace lines 21-23:

```properties
# Maven Central Publication placeholders (Override in ~/.gradle/gradle.properties)
# Generate token at https://central.sonatype.com -> Account -> User Token
# mavenCentralUsername=YOUR_TOKEN_USERNAME
# mavenCentralPassword=YOUR_TOKEN_PASSWORD
# signingKey=YOUR_GPG_SECRET_KEY
# signingPassword=YOUR_GPG_PASSWORD
```

- [ ] **Step 2: Commit**

```
git add gradle.properties
git commit -m "docs: update credential docs for Central Portal tokens"
```

---

### Task 5: Verify build compiles

**Files:** N/A

- [ ] **Step 1: Run build to verify configuration compiles**

```
./gradlew :shared:build --no-daemon
```

Expected: BUILD SUCCESSFUL (after downloading dependencies)

- [ ] **Step 2: Verify nexus plugin tasks are available**

```
./gradlew tasks --group=publishing --no-daemon
```

Expected: Shows `publishToSonatype`, `closeAndReleaseSonatypeStagingRepository`, etc.

- [ ] **Step 3: Commit any build fixes**

```
git commit -m "fix: adjustments after build verification"
```
