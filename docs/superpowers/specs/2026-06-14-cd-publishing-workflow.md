# CD Publishing Workflow

## Status

Approved.

## Problem

Le projet sait maintenant publier sur Maven Central (snapshots + releases) via le Nexus Publish Plugin, mais ça reste manuel. Il faut automatiser la publication dans le CI GitHub :

1. Publier un snapshot automatiquement sur chaque push vers `master`
2. Publier une release sur chaque tag `v*`
3. Permettre une publication snapshot manuelle depuis n'importe quelle branche

## Solution

Créer un fichier `.github/workflows/publish.yml` unique avec 3 jobs déclenchés par des événements différents.

## Architecture

```
.github/workflows/publish.yml
├── snapshot-on-push
│   ├── trigger: push master
│   ├── steps: checkout → setup-java → gradlew publishToSonatype
│   └── destination: s01.oss.sonatype.org (snapshots)
├── release-on-tag
│   ├── trigger: push tag v*
│   ├── steps: checkout → setup-java → set version from tag → publishToSonatype → closeAndRelease
│   └── destination: central.sonatype.com (releases)
└── snapshot-manual
    ├── trigger: workflow_dispatch (input confirm)
    ├── steps: checkout → setup-java → gradlew publishToSonatype
    └── destination: s01.oss.sonatype.org (snapshots)
```

## Déclencheurs

```yaml
on:
  push:
    branches: [master]
    tags: ['v*']
  workflow_dispatch:
    inputs:
      confirm:
        description: 'Type "snapshot" to confirm'
        required: true
```

## Détail des jobs

### `snapshot-on-push`

- Runner: `ubuntu-latest`
- Se déclenche sur `push` vers `master`
- Vérifie que le nom de la branche ne commence pas par `v` (évite le double déclenchement avec tags)
- Steps: checkout → setup-java (Zulu 25, cache Gradle) → chmod → `./gradlew :shared:publishToSonatype`
- Utilise les secrets : `MAVEN_CENTRAL_USERNAME`, `MAVEN_CENTRAL_PASSWORD`, `SIGNING_KEY`, `SIGNING_PASSWORD`

### `release-on-tag`

- Runner: `ubuntu-latest`
- Se déclenche sur `push` de tag `v*`
- Extrait la version du tag (`v1.0.0` → `1.0.0`)
- Override `version` dans `gradle.properties` pour que le Nexus plugin utilise le repo de release
- Steps: checkout → setup-java → set version → publish → closeAndRelease
- `closeAndReleaseMavenCentralStagingRepository` ferme et publie le staging repository automatiquement

### `snapshot-manual`

- Runner: `ubuntu-latest`
- Se déclenche sur `workflow_dispatch` avec confirmation
- Steps identiques à `snapshot-on-push`

## Secrets GitHub requis

| Secret | Usage |
|---|---|
| `MAVEN_CENTRAL_USERNAME` | Token username du Central Portal |
| `MAVEN_CENTRAL_PASSWORD` | Token password du Central Portal |
| `SIGNING_KEY` | Clé GPG privée |
| `SIGNING_PASSWORD` | Passphrase GPG |

## Non-Goals

- Gestion de versioning automatique (bump de version, changelog, etc.)
- Publication vers d'autres registres (GitHub Packages, etc.)
- Tests avant publication (le CI existant est déjà séparé)
