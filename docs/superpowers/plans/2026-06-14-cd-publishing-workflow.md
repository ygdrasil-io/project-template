# CD Publishing Workflow Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Automate Maven Central publishing in CI (snapshots on master, releases on tag, manual snapshot from any branch).

**Architecture:** Single `.github/workflows/publish.yml` with 3 jobs — each triggered by different events (push master, push tag v*, workflow_dispatch). The convention plugin is updated to support `-PreleaseVersion` for release tag builds.

**Tech Stack:** GitHub Actions, Gradle, Nexus Publish Plugin, GPG signing

---

### Task 1: Support release version override in convention plugin

**Files:**
- Modify: `buildSrc/src/main/kotlin/ygdrasil/conventions/kmp-publish.gradle.kts`

Currently the version is hardcoded as `version = "1.0.0-SNAPSHOT"`. For release tag builds, we need to override this via `-PreleaseVersion=1.0.0`.

- [ ] **Step 1: Change version to support override**

Change line 9 from:
```kotlin
version = "1.0.0-SNAPSHOT"
```
to:
```kotlin
version = project.findProperty("releaseVersion") as? String ?: "1.0.0-SNAPSHOT"
```

This makes the version configurable: if `-PreleaseVersion=1.0.0` is passed, it uses that value; otherwise defaults to `SNAPSHOT`.

- [ ] **Step 2: Commit**

```
git add buildSrc/src/main/kotlin/ygdrasil/conventions/kmp-publish.gradle.kts
git commit -m "feat: support -PreleaseVersion override for releases"
```

---

### Task 2: Create publish.yml workflow

**Files:**
- Create: `.github/workflows/publish.yml`

**Context:** The CI workflow already exists at `.github/workflows/ci.yml`. The new `publish.yml` handles ONLY publishing — no tests, no build verification (that's the CI's job).

Important: GitHub secrets `MAVEN_CENTRAL_USERNAME`, `MAVEN_CENTRAL_PASSWORD`, `SIGNING_KEY`, `SIGNING_PASSWORD` are already configured.

Each job must have explicit `if` conditions to prevent cross-triggering (e.g., a tag push shouldn't run the snapshot job, and a branch push shouldn't run the release job even though both share the same `push` trigger).

- [ ] **Step 1: Create `.github/workflows/publish.yml`**

```yaml
name: Publish to Maven Central

on:
  push:
    branches: [master]
    tags: ['v*']
  workflow_dispatch:
    inputs:
      confirm:
        description: 'Type "snapshot" to publish a snapshot from this branch'
        required: true

jobs:
  snapshot-on-push:
    if: github.event_name == 'push' && github.ref_name == 'master'
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: 'zulu'
          java-version: '25'
          cache: 'gradle'
      - name: Grant execute permission
        run: chmod +x gradlew
      - name: Publish snapshot
        run: ./gradlew :shared:publishToSonatype --no-daemon --stacktrace
        env:
          MAVEN_CENTRAL_USERNAME: ${{ secrets.MAVEN_CENTRAL_USERNAME }}
          MAVEN_CENTRAL_PASSWORD: ${{ secrets.MAVEN_CENTRAL_PASSWORD }}
          SIGNING_KEY: ${{ secrets.SIGNING_KEY }}
          SIGNING_PASSWORD: ${{ secrets.SIGNING_PASSWORD }}

  release-on-tag:
    if: github.event_name == 'push' && startsWith(github.ref, 'refs/tags/v')
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: 'zulu'
          java-version: '25'
          cache: 'gradle'
      - name: Grant execute permission
        run: chmod +x gradlew
      - name: Extract version from tag
        run: echo "VERSION=${GITHUB_REF_NAME#v}" >> $GITHUB_ENV
      - name: Publish release
        run: ./gradlew :shared:publishToSonatype closeAndReleaseMavenCentralStagingRepository --no-daemon --stacktrace -PreleaseVersion=${{ env.VERSION }}
        env:
          MAVEN_CENTRAL_USERNAME: ${{ secrets.MAVEN_CENTRAL_USERNAME }}
          MAVEN_CENTRAL_PASSWORD: ${{ secrets.MAVEN_CENTRAL_PASSWORD }}
          SIGNING_KEY: ${{ secrets.SIGNING_KEY }}
          SIGNING_PASSWORD: ${{ secrets.SIGNING_PASSWORD }}

  snapshot-manual:
    if: github.event_name == 'workflow_dispatch' && github.event.inputs.confirm == 'snapshot'
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: 'zulu'
          java-version: '25'
          cache: 'gradle'
      - name: Grant execute permission
        run: chmod +x gradlew
      - name: Publish snapshot
        run: ./gradlew :shared:publishToSonatype --no-daemon --stacktrace
        env:
          MAVEN_CENTRAL_USERNAME: ${{ secrets.MAVEN_CENTRAL_USERNAME }}
          MAVEN_CENTRAL_PASSWORD: ${{ secrets.MAVEN_CENTRAL_PASSWORD }}
          SIGNING_KEY: ${{ secrets.SIGNING_KEY }}
          SIGNING_PASSWORD: ${{ secrets.SIGNING_PASSWORD }}
```

Key points:
- **`snapshot-on-push`**: Only runs on push to master. Uses the default SNAPSHOT version from the convention plugin.
- **`release-on-tag`**: Extracts version from tag (e.g., `v1.0.0` → `1.0.0`), passes it via `-PreleaseVersion` which overrides the convention plugin's default version. Also runs `closeAndReleaseMavenCentralStagingRepository` to finalize the staging repo.
- **`snapshot-manual`**: Requires typing "snapshot" in the confirm input to prevent accidental triggering.

- [ ] **Step 2: Commit**

```
git add .github/workflows/publish.yml
git commit -m "feat: add publish workflow with snapshot/release/manual jobs"
```

---

### Task 3: Self-review and verify

**Files:** N/A

- [ ] **Step 1: Verify the workflow file has valid syntax**

Review:
- All 3 jobs have proper `if` conditions (no cross-triggering)
- `release-on-tag` correctly strips `v` prefix from tag name
- `release-on-tag` passes `-PreleaseVersion=${{ env.VERSION }}`
- `snapshot-manual` checks `inputs.confirm == 'snapshot'`
- All jobs pass `MAVEN_CENTRAL_USERNAME`, `MAVEN_CENTRAL_PASSWORD`, `SIGNING_KEY`, `SIGNING_PASSWORD` as env vars
- No hardcoded secrets
- Uses `ubuntu-latest` (publishing doesn't need macOS)
- Java 25 Zulu with Gradle cache

- [ ] **Step 2: Verify convention plugin change**

```
git diff HEAD~1 -- buildSrc/src/main/kotlin/ygdrasil/conventions/kmp-publish.gradle.kts
```

Expected: only line 9 changed from `version = "1.0.0-SNAPSHOT"` to `version = project.findProperty("releaseVersion") as? String ?: "1.0.0-SNAPSHOT"`

- [ ] **Step 3: Final build verification**

```
./gradlew :shared:jvmJar -x test -q --no-daemon
```

Expected: BUILD SUCCESSFUL (no output with -q)
