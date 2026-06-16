# Guide de Contribution

Merci de contribuer à ce projet ! Ce guide vous aidera à naviguer dans le processus de contribution.

## Code de Conduite

Ce projet suit un [Code de Conduite](CODE_OF_CONDUCT.fr.md). En participant, vous vous engagez à le respecter.

## Signaler un Bug

**Avant de créer un rapport :**
- Vérifiez que le bug n'a pas déjà été signalé
- Assurez-vous d'utiliser la dernière version
- Consultez les discussions existantes

**Quand vous créez un rapport :**
- Utilisez le template [Bug Report](.github/ISSUE_TEMPLATE/bug_report.md)
- Incluez les étapes de reproduction
- Fournissez l'environnement (OS, version JDK, etc.)
- Ajoutez des logs ou captures d'écran si possible

## Proposer une Fonctionnalité

- Utilisez le template [Feature Request](.github/ISSUE_TEMPLATE/feature_request.md)
- Décrivez le problème que vous souhaitez résoudre
- Expliquez pourquoi cette fonctionnalité serait utile
- Si possible, proposez une approche d'implémentation

## Envoyer une Pull Request

### Prérequis

Avant de soumettre une PR, assurez-vous que :

- [ ] Les tests passent localement (`./gradlew :shared:jvmTest`)
- [ ] La documentation a été mise à jour si nécessaire
- [ ] Le CHANGELOG.fr.md a été mis à jour
- [ ] Le titre suit le format Conventional Commits

### Build Local

```bash
# Tests rapides JVM
./gradlew :shared:jvmTest

# Tous les tests
./gradlew allTests

# Génération documentation API
./gradlew :shared:dokkaGfm
```

### Conventional Commits

Ce projet utilise les [Conventional Commits](https://www.conventionalcommits.org/).

**Format :** `<type>(<scope>): <description>`

**Types autorisés :**

| Type       | Usage                                                |
|-----------|------------------------------------------------------|
| `feat`    | Nouvelle fonctionnalité                              |
| `fix`     | Correctif                                            |
| `build`   | Système de build ou dépendances                      |
| `chore`   | Maintenance, outillage, dépendances                  |
| `ci`      | Configuration CI/CD                                  |
| `docs`    | Documentation                                        |
| `perf`    | Amélioration de performance                          |
| `refactor`| Refactoring (sans changement de comportement)        |
| `test`    | Ajout ou correction de tests                         |
| `style`   | Style de code (formatage, ordre des imports)         |

**Scopes :** `shared`, `buildSrc`, `docs`, `release`

**Exemples :**
```
feat(shared): add caching layer to PlatformRepository
fix(buildSrc): resolve AGP compatibility issue
docs: update README with new badges
```

### Workflow Git

**Branches :**
- `master` — branche de release (protégée)
- `feat/*` — nouvelles fonctionnalités
- `fix/*` — correctifs
- `chore/*` — maintenance, outillage

**Règles :**
- Pas de commit direct sur `master`
- Les branches doivent être rebasées sur `master` avant PR
- Les commits doivent être atomiques (un changement par commit)

### Processus de Review

1. **Créer une Pull Request**
   - Utilisez le [template PR](.github/PULL_REQUEST_TEMPLATE.md)
   - Titre au format conventional commit
   - Référencez les issues liées

2. **Review**
   - Au moins 1 approbation requise
   - Les revues sont obligatoires pour merge
   - Les commentaires doivent être adressés

3. **Merge**
   - Stratégie : Squash + Rebase
   - Le titre du squash doit garder le format conventional commit
   - Supprimez la branche après merge

### Versionnement

Ce projet suit le [Semantic Versioning](https://semver.org/).

- `MAJOR` — changement incompatible
- `MINOR` — nouvelle fonctionnalité rétrocompatible
- `PATCH` — correctif rétrocompatible

Les versions SNAPSHOT (`1.0.0-SNAPSHOT`) sont utilisées pendant le développement actif.
Les versions release sont publiées via le workflow de publication (`releaseVersion` property).

## Licence

En contribuant, vous acceptez que vos contributions soient sous licence MIT.
