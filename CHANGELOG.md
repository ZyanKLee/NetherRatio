# Changelog

All notable changes to this project will be documented in this file.


## ### Added

- Implement coordinate bounds feature and enhance automated release workflow

## [2.2.0] - 2026-02-07

### Added
- **Coordinate Bounds**: New safety feature to prevent teleportation into ungenerated chunks or beyond world borders
  - Configurable min/max X and Z coordinate limits
  - Automatic clamping of portal destinations to safe areas
  - Disabled by default for backward compatibility
  - Documented in all language files
- Automated publishing workflow for Modrinth and Hangar platforms via GitHub Actions
- Enhanced release script with automated deployment instructions

### Changed
- Updated Paper API dependency to 1.21.11-R0.1-SNAPSHOT for latest compatibility
- Merged separate publishing workflow into unified build-and-release workflow
- Improved configuration documentation with coordinate bounds examples

### Technical
- Added `coordinate-bounds` configuration section with `enabled`, `min-x`, `max-x`, `min-z`, `max-z` options
- Added bounds validation and clamping methods to ConfigManager
- Enhanced PortalTravelListener to check and enforce coordinate bounds
- Updated README with comprehensive coordinate bounds documentation

## [2.1.2] - 2026-02-07

### Added
- GitHub Actions workflow for automated publishing to Modrinth and Hangar

### Changed
- Enhanced release script with better instructions for GitHub releases
- Updated publishing documentation

## [2.1.1] - 2026-02-07

### Added
- Per-World Ratio Configuration: Set different ratios for specific world/nether pairs
- Portal Coordinate Calculator: Calculate portal coordinates without traveling (`/netherratio calc [x z]`)
- Improved command structure with consistent subcommands (list, set, calc, reload)

### Changed
- Refactored command system to use subcommand pattern
- Updated configuration format to support both simple and advanced world-pair definitions
- Renamed project from NetherCorrespondence to NetherRatio for clarity
- Enhanced permission system with `netherratio.calc` for coordinate calculator access

### Fixed
- Build compatibility issues with MessagesManager API
- Package naming consistency across all files

## [2.0.2] - Previous Release

### Added
- Initial multi-language support (English, German, French, Italian, Korean)
- Basic world ratio configuration
- Portal travel coordinate conversion

## [1.0.0] - Initial Release

### Added
- Core Nether/Overworld coordinate conversion functionality
- Configuration system for custom ratios
- Basic command interface
