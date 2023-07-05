# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).


## [1.2.0] -  tbd

### Added
- Add project name to sonar cloud properties
- Added Suppliers and Customers fields for logic filtering
- Added new client id to get roles from the new country risk published app

### Changed
- Map Between API and New Data Model for getting Suppliers and Customers

## [1.1.1] -  2023-05-16

### Fixes

- Upgrade version of spring-security-web to fix vul to 6.0.3
- Upgrade version of spring boot to fix vul to 3.0.6
- Update DEPENDENCIES file
- Update Code of Conduct

### Removed

- Removed Helm Charts folder and sub folders content, helms can be found on leading repository described on Readme.md 


## [1.1.0] -  2023-04-20

### Changed

- Changed test data that shows on dashboard
- Changed sonar token on properties
- Changed h2 database on test to postgres containers
- Changed Dependencies file based on new upgraded dependencies
- Changed Arc42 and User Guide documentation

### Fixes

- Upgrade version of spring-expression to 6.0.8
- Upgrade version to fix snake yaml vulnerability 
- Upgrade Commons upload lib version to fix vulnerability
- Fix Bug for Sonar Long conversion from long native
- Readme updated with new Helm Chart leading repository information
- Upgraded jackson-databind and spring-core
- Fixed dockerfile Jar and Trivy scan path

### Added

- Added .tractusx metafile with information about leading repository
- Added new images on docs folder

## [1.0.4] -  2023-03-02

### Fixes

- Fix image that veracode is validating
- Fix Header on charts to be validated with Company group

### Changed 
- Change structure of folders on README.md
- Change Helm chart README.md on current version released

### Removed 
- Dockerfile removing the same EXPOSE block

## [1.0.3] -  2023-02-20

### Added

- Added arq42 documentation
- Added UserGuide


## [1.0.2] -  2023-02-20

### Changed

- Fix hardcoded application secret name on deployment helm chart

## [1.0.1] -  2023-02-20

### Changed

- Fix whitespace handling that removed apiVersion declaration from Helm templates

## [1.0.0] - 2023-01-26

### Added

- First Release 

