# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Fixed

- Workflow status badges have been fixed after having been messed up by an erroneous refactoring of a class name.

## [1.0.3-alpha] - 2025-07-23

### Fixed

- Artifact name has been corrected after having been messed up by an erroneous refactoring of a class name.

## [1.0.2-alpha] - 2025-07-23

### Added

- This changelog.
- Calculation of a latitude band based on latitude in degrees.
- Determination of hemisphere based on latitude band character code.
- Parsing of UTM coordinate strings with either latitude band or hemisphere.

### Changed

- The class previously known as UTM has been renamed to UTMCoordinate.
- The UTM class name has been repurposed as a utility class for UTM constants and utility methods.

## [1.0.1-alpha] - 2025-07-23

### Fixed

- Added missing distributionManagement element to pom.xml for package deployment to GitHub Packages. 

## [1.0.0-alpha] - 2025-07-23

### Added

- Initial release with functioning conversion from UTM to latitude and longitude, and vice versa.

[Unreleased]: https://github.com/Maritims/utm-lat-lng/compare/v1.0.3-alpha...main
[1.0.2-alpha]: https://github.com/Maritims/utm-lat-lng/compare/v1.0.2-alpha...v1.0.3-alpha
[1.0.2-alpha]: https://github.com/Maritims/utm-lat-lng/compare/v1.0.1-alpha...v1.0.2-alpha
[1.0.1-alpha]: https://github.com/Maritims/utm-lat-lng/compare/v1.0.0-alpha...v1.0.1-alpha
[1.0.0-alpha]: https://github.com/Maritims/utm-lat-lng/releases/tag/v1.0.0-alpha