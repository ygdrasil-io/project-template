# Guide de Contribution / Contributing Guide

Merci de contribuer à ce projet ! Ce guide vous aidera à naviguer dans le processus de contribution.

Thank you for contributing to this project! This guide will help you navigate the contribution process.

## Code de Conduite / Code of Conduct

Ce projet suit un [Code de Conduite](CODE_OF_CONDUCT.md). En participant, vous vous engagez à le respecter.

This project follows a [Code of Conduct](CODE_OF_CONDUCT.md). By participating, you agree to uphold it.

## Signaler un Bug / Reporting a Bug

**Avant de créer un rapport / Before creating a report:**
- Vérifiez que le bug n'a pas déjà été signalé / Check if the bug has already been reported
- Assurez-vous d'utiliser la dernière version / Make sure you're on the latest version
- Consultez les discussions existantes / Check existing discussions

**Quand vous créez un rapport / When creating a report:**
- Utilisez le template [Bug Report](.github/ISSUE_TEMPLATE/bug_report.md)
- Incluez les étapes de reproduction / Include steps to reproduce
- Fournissez l'environnement (OS, version JDK, etc.) / Provide environment details
- Ajoutez des logs ou captures d'écran si possible / Add logs or screenshots if possible

## Proposer une Fonctionnalité / Suggesting a Feature

- Utilisez le template [Feature Request](.github/ISSUE_TEMPLATE/feature_request.md)
- Décrivez le problème que vous souhaitez résoudre / Describe the problem you want to solve
- Expliquez pourquoi cette fonctionnalité serait utile / Explain why this feature would be useful
- Si possible, proposez une approche d'implémentation / If possible, suggest an implementation approach

## Envoyer une Pull Request / Submitting a Pull Request

### Prérequis / Prerequisites

Avant de soumettre une PR, assurez-vous que / Before submitting a PR, make sure:

- [ ] Les tests passent localement / Tests pass locally (`./gradlew :shared:jvmTest`)
- [ ] La documentation a été mise à jour si nécessaire / Documentation is updated if needed
- [ ] Le CHANGELOG.md a été mis à jour / CHANGELOG.md has been updated
- [ ] Le titre suit le format Conventional Commits / Title follows Conventional Commits format

### Build Local / Local Build

```bash
# Tests rapides JVM (fast-track) / Fast JVM tests
./gradlew :shared:jvmTest

# Tous les tests / All tests
./gradlew allTests

# Génération documentation API / Generate API docs
./gradlew :shared:dokkaGfm
```

### Conventional Commits (English only)

This project uses [Conventional Commits](https://www.conventionalcommits.org/).

**Format:** `<type>(<scope>): <description>`

**Types autorisés / Allowed types:**

| Type       | Usage (English)                                         |
|-----------|---------------------------------------------------------|
| `feat`    | New feature                                             |
| `fix`     | Bug fix                                                 |
| `build`   | Build system or dependencies                            |
| `chore`   | Maintenance, tooling, dependencies                      |
| `ci`      | CI/CD configuration                                     |
| `docs`    | Documentation changes                                   |
| `perf`    | Performance improvement                                 |
| `refactor`| Code refactoring (no behavior change)                   |
| `test`    | Adding or fixing tests                                  |
| `style`   | Code style (formatting, imports ordering)               |

**Scopes:** `shared`, `buildSrc`, `docs`, `release`

**Exemples / Examples:**
```
feat(shared): add caching layer to PlatformRepository
fix(buildSrc): resolve AGP compatibility issue
docs: update README with new badges
```

### Workflow Git / Git Workflow

**Branches:**
- `master` — branche de release / release branch (protégée / protected)
- `feat/*` — nouvelles fonctionnalités / new features
- `fix/*` — correctifs / bug fixes
- `chore/*` — maintenance, refactoring, outillage / maintenance, tooling

**Règles / Rules:**
- Pas de commit direct sur `master` / No direct commits to `master`
- Les branches doivent être rebasées sur `master` avant PR / Branches must be rebased on `master` before PR
- Les commits doivent être atomiques (un changement par commit) / Commits should be atomic (one change per commit)

### Processus de Review / Review Process

1. **Créer une Pull Request / Create a Pull Request**
   - Utilisez le [template PR](.github/PULL_REQUEST_TEMPLATE.md)
   - Titre au format conventional commit / Title in conventional commit format
   - Référencez les issues liées / Reference related issues

2. **Review**
   - Au moins 1 approbation requise / At least 1 approval required
   - Les revues sont obligatoires pour merge / Reviews are required before merging
   - Les commentaires doivent être adressés / All comments must be addressed

3. **Merge**
   - Stratégie : Squash + Rebase / Strategy: Squash + Rebase
   - Le titre du squash doit garder le format conventional commit / Squash title must keep conventional commit format
   - Supprimez la branche après merge / Delete branch after merge

### Versionnement / Versioning

Ce projet suit le [Semantic Versioning](https://semver.org/).

This project follows [Semantic Versioning](https://semver.org/).

- `MAJOR` — changement incompatible / breaking change
- `MINOR` — nouvelle fonctionnalité rétrocompatible / backward-compatible feature
- `PATCH` — correctif rétrocompatible / backward-compatible fix

Les versions SNAPSHOT (`1.0.0-SNAPSHOT`) sont utilisées pendant le développement actif / SNAPSHOT versions are used during active development.
Les versions release sont publiées via le workflow de publication (`releaseVersion` property) / Release versions are published via the release workflow.

## Licence / License

En contribuant, vous acceptez que vos contributions soient sous licence MIT.

By contributing, you agree that your contributions will be licensed under the MIT License.
