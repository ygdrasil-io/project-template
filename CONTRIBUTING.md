# Contributing Guide

Thank you for contributing to this project! This guide will help you navigate the contribution process.

## Code of Conduct

This project follows a [Code of Conduct](CODE_OF_CONDUCT.md). By participating, you agree to uphold it.

## Reporting a Bug

**Before creating a report:**
- Check if the bug has already been reported
- Make sure you're on the latest version
- Check existing discussions

**When creating a report:**
- Use the [Bug Report](.github/ISSUE_TEMPLATE/bug_report.md) template
- Include steps to reproduce
- Provide environment details (OS, JDK version, etc.)
- Add logs or screenshots if possible

## Suggesting a Feature

- Use the [Feature Request](.github/ISSUE_TEMPLATE/feature_request.md) template
- Describe the problem you want to solve
- Explain why this feature would be useful
- If possible, suggest an implementation approach

## Submitting a Pull Request

### Prerequisites

Before submitting a PR, make sure:

- [ ] Tests pass locally (`./gradlew :shared:jvmTest`)
- [ ] Documentation is updated if needed
- [ ] CHANGELOG.md has been updated
- [ ] Title follows Conventional Commits format

### Local Build

```bash
# Fast JVM tests
./gradlew :shared:jvmTest

# All tests
./gradlew allTests

# Generate API docs
./gradlew :shared:dokkaGfm
```

### Conventional Commits

This project uses [Conventional Commits](https://www.conventionalcommits.org/).

**Format:** `<type>(<scope>): <description>`

**Allowed types:**

| Type       | Usage                                              |
|-----------|----------------------------------------------------|
| `feat`    | New feature                                        |
| `fix`     | Bug fix                                            |
| `build`   | Build system or dependencies                       |
| `chore`   | Maintenance, tooling, dependencies                 |
| `ci`      | CI/CD configuration                                |
| `docs`    | Documentation changes                              |
| `perf`    | Performance improvement                            |
| `refactor`| Code refactoring (no behavior change)              |
| `test`    | Adding or fixing tests                             |
| `style`   | Code style (formatting, imports ordering)          |

**Scopes:** `shared`, `buildSrc`, `docs`, `release`

**Examples:**
```
feat(shared): add caching layer to PlatformRepository
fix(buildSrc): resolve AGP compatibility issue
docs: update README with new badges
```

### Git Workflow

**Branches:**
- `master` — release branch (protected)
- `feat/*` — new features
- `fix/*` — bug fixes
- `chore/*` — maintenance, tooling

**Rules:**
- No direct commits to `master`
- Branches must be rebased on `master` before PR
- Commits should be atomic (one change per commit)

### Review Process

1. **Create a Pull Request**
   - Use the [PR template](.github/PULL_REQUEST_TEMPLATE.md)
   - Title in conventional commit format
   - Reference related issues

2. **Review**
   - At least 1 approval required
   - Reviews are required before merging
   - All comments must be addressed

3. **Merge**
   - Strategy: Squash + Rebase
   - Squash title must keep conventional commit format
   - Delete branch after merge

### Versioning

This project follows [Semantic Versioning](https://semver.org/).

- `MAJOR` — breaking change
- `MINOR` — backward-compatible feature
- `PATCH` — backward-compatible fix

SNAPSHOT versions (`1.0.0-SNAPSHOT`) are used during active development.
Release versions are published via the release workflow (`releaseVersion` property).

## License

By contributing, you agree that your contributions will be licensed under the MIT License.
