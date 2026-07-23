# Changelog

All notable changes to this project will be documented in this file.

## [1.0.1] - 2026-07-18

### Fixed
- Fixed compatibility with Minecraft 1.21.10 Yarn mappings.
- Fixed the `/s` command accessing the server and world.
- Fixed game mode serialization when saving return data.
- Fixed returning from Spectator mode across dimensions.

### Changed
- Updated the Gradle Wrapper to the official Gradle 8.14.3 distribution.

## [1.0.0] - 2026-07-18

### Added
- Initial release.
- Added the `/s` command to toggle Spectator mode.
- Added persistent return positions that survive server restarts.
- Added optional Creative and Adventure mode restrictions.
- Added per-world configuration support.
- Added the `/s reload` command for server operators.
