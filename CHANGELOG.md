# Changelog

All notable changes to this project will be documented in this file.

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
