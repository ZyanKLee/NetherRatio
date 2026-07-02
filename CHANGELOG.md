# Changelog

All notable changes to this project will be documented in this file.

## [3.0.2] - 2026-07-02

### Changed

- **deps-dev**: Bump org.junit.jupiter:junit-jupiter

### Fixed

- Correct release note generation for merge-commit workflows

Ci

- Implement semantic-release principles
- Replace PR title check with per-commit commitlint

Merge

- [#21](https://github.com/ZyanKLee/NetherRatio/pull/21)
- [#22](https://github.com/ZyanKLee/NetherRatio/pull/22)
- [#23](https://github.com/ZyanKLee/NetherRatio/pull/23)
- [#24](https://github.com/ZyanKLee/NetherRatio/pull/24)

## [3.0.1] - 2026-06-16

### Changed

- **deps-dev**: Bump org.junit.jupiter:junit-jupiter
- **deps-dev**: Bump org.apache.maven.plugins:maven-surefire-plugin

### Fixed

- Release script version prefix

Merge

- [#18](https://github.com/ZyanKLee/NetherRatio/pull/18)
- [#17](https://github.com/ZyanKLee/NetherRatio/pull/17)

## [3.0.0] - 2026-06-16

### Added

- Target Paper 26.1 and Java 25

Ci

- Add build workflow for pushes and pull requests
- Add static analysis, coverage, and dependency checks to the build
- Restrict GITHUB_TOKEN to contents: read

## [2.5.0] - 2026-06-10

### Added

- Folia support is defined in project settings

### Fixed

- Stop overwriting config.yml with stale in-memory state on shutdown
- Apply coordinate offsets in /netherratio calc
- Revert paper-api to 1.21.11-R0.1-SNAPSHOT

Merge

- [#7](https://github.com/ZyanKLee/NetherRatio/pull/7)
- [#14](https://github.com/ZyanKLee/NetherRatio/pull/14)
- [#15](https://github.com/ZyanKLee/NetherRatio/pull/15)
- [#13](https://github.com/ZyanKLee/NetherRatio/pull/13)

## [2.4.1] - 2026-04-08

### Added

- Enhance release script with branch and sync status checks
- Improve changelog preparation for Hangar version creation using JSON escaping
- Folia support
- Add Folia support to the publishing workflow

### Changed

- **deps-dev**: Bump org.apache.maven.plugins:maven-shade-plugin

Merge

- [#4](https://github.com/ZyanKLee/NetherRatio/pull/4)

## [2.3.2] - 2026-02-08

### Added

- Add GNU General Public License version 3 to the repository

### Changed

- **deps-dev**: Bump org.apache.maven.plugins:maven-compiler-plugin
- **deps-dev**: Bump org.apache.maven.plugins:maven-shade-plugin

Merge

- [#1](https://github.com/ZyanKLee/NetherRatio/pull/1)
- [#2](https://github.com/ZyanKLee/NetherRatio/pull/2)
- [#3](https://github.com/ZyanKLee/NetherRatio/pull/3)

## [2.3.1] - 2026-02-07

### Added

- Enhance Hangar upload process with JWT authentication and version creation

### Changed

- **release**: Update CHANGELOG.md with version number and date after release

## [2.3.0] - 2026-02-07

### Added

- Add support for coordinate offsets in world pair configurations

### Changed

- **release**: Update release process to generate and use release notes with git-cliff
- **release**: Update changelog generation command in release script

## [2.2.3] - 2026-02-07

### Changed

- **release**: Update changelog template to correctly format unreleased changes

### Fixed

- Correct authorization header format for Hangar API

## [2.2.2] - 2026-02-07

### Added

- Enhance Hangar publishing step with versioning and platform support

## [2.2.1] - 2026-02-07

### Added

- Implement coordinate bounds feature and enhance automated release workflow

## [2.2.0] - 2026-02-07

### Added

- Enhance automated publishing workflow and update release script instructions
- Add coordinate bounds configuration and clamping logic for portal travel

### Fixed

- Update paper-api dependency version to 1.21.11-R0.1-SNAPSHOT

## [2.1.2] - 2026-02-07

### Added

- Add automated publishing setup with GitHub Actions and update release script instructions

## [2.1.1] - 2026-02-07

### Added

- Update README with enhanced configuration options and examples for per-world ratios

## [2.1.0] - 2026-02-07

### Added

- Implement per-world ratios and enhance command functionality for NetherRatio
- Enhance WorldRatioCommand with subcommands and improve command usage messages

### Fixed

- Update Paper version in README to 1.21.10

### Refactored

- Rename Nethercorrespondence to NetherRatio across project files and update permissions

## [2.0.2] - 2026-02-06

### Fixed

- Update references from Nethercorrespondence to NetherRatio in build files and scripts

## [2.0.1] - 2026-02-06

### Fixed

- Update version in plugin.yml to maintain compatibility with API version


