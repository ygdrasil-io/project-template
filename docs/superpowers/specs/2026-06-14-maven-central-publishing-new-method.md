# Maven Central Publishing – New Method (Central Portal Tokens)

## Status

Approved.

## Problem

The project currently publishes to Maven Central using legacy Sonatype OSSRH credentials (`ossrhUsername`/`ossrhPassword`). Sonatype has deprecated this authentication method in favor of API tokens generated from the Central Portal (`central.sonatype.com`). The project needs to adopt the new Portal token authentication while preserving snapshot publishing.

## Solution

Adopt the `io.github.gradle-nexus.publish-plugin` (Nexus Publish Plugin) which:

- Automates Maven Central staging (create, close, release repositories)
- Supports both Central Portal (releases) and legacy OSSRH (snapshots) repositories
- Works with Portal API tokens for authentication
- Is the industry standard for Gradle Maven Central publishing

## Changes

### `gradle/libs.versions.toml`
- Add `nexus-publish` version `1.3.0`
- Add `nexus-publish` plugin entry

### `build.gradle.kts` (root)
- Apply `io.github.gradle-nexus.publish-plugin`
- Configure `nexusPublishing` with:
  - Release repository → `central.sonatype.com/service/local/staging/deploy/maven2/`
  - Snapshot repository → `s01.oss.sonatype.org/content/repositories/snapshots/`
  - Credentials from `mavenCentralUsername`/`mavenCentralPassword` (Portal tokens)

### `kmp-publish.gradle.kts`
- Remove `publishing.repositories {}` block (now managed by Nexus plugin)
- Update credential property names to `mavenCentralUsername`/`mavenCentralPassword`

### `gradle.properties`
- Rename comment placeholders from `ossrhUsername`/`ossrhPassword` to `mavenCentralUsername`/`mavenCentralPassword`

## Credentials

Users generate a User Token from `central.sonatype.com` → Account → User Token, and set in `~/.gradle/gradle.properties`:

```properties
mavenCentralUsername=<token-username>
mavenCentralPassword=<token-password>
signingKey=<gpg-private-key>
signingPassword=<gpg-password>
```

## Workflow

```bash
# Snapshot publish
./gradlew :shared:publishToSonatype

# Release (auto-closes and releases staging)
./gradlew :shared:publishToSonatype :shared:closeAndReleaseSonatypeStagingRepository
```

## Non-Goals

- Migration from SNAPSHOT to release versioning scheme
- CI/CD publish automation (out of scope)
- Namespace/group changes
