# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).


# Changelog

## [1.3.0] - [unreleased]

### Changed
- BPDM endpoints changed for requesting EDC when available
- Logic mapping changed between Gate Pool and Country Risk DTO
- Update header year for TRG on each file

### Added
- Added mapping for generic Endpoint
- Added mapping for Pool lsa types

### Fixes
- Fix vulnerability on spring boot version and upgrade version to 3.1.8

## [1.2.3] - 2023-11-30

### Fixes
- Fix vulnerability on ch.qos.logback lib to 1.14.13

## [1.2.2] - 2023-11-30

### Removed
- Removed org.owasp.esapi lib 

### Changed
- Change Unit Test from testRestTemplate to webTestClient since testRestTemplate does not handle UNAUTHORIZED errors

## [1.2.1] - 2023-11-27

### Changed
- Changed arq42 documentation to be updated to current application
- Update Dependencies md file

### Fixes
- Fix bug on sharing endpoint authorization
- Fix health check for trivy scan on docker image
- Fix vulnerability find on spring security core 6.1.1
- Fix vulnerability find on spring web flux 3.1.2
- Fix vulnerability with exclusion of bouncycastle lib on spring security
- Fix vulnerability find on owasp antisamy 1.7.3

### Added
- Added docker registry workflow
- Added dockerhub notice.md
- Added docker notice.md to readme.md

## [1.2.0] - 2023-10-10

### Added
- Added project name to Sonar Cloud properties.
- Added Suppliers and Customers fields for logic filtering.
- Added new client ID to get roles from the newly published Country Risk app.
- Added new file for [Standard Api Documentation Controller](docs/swagger/sharing_controller.yml)

### Changed
- Mapped between API and new Data Model for getting Suppliers and Customers.
- Major Updated libraries:
  - Updated spring boot parent version to 3.1.2
  - org.springframework.boot:spring-boot-starter-web to 3.1.2
  - org.springframework.security:spring-security-web to 6.1.1
- Changed Dependencies file with new library versions.
- Changed Mapping to adapt new Data Model on BPDM Gate
- Enable Hidden endpoints for Sharing Controller
- Change Dtos of Sharing Controllers


### Fixes
- Upgraded version of spring-boot-autoconfigure to fix vulnerability to 3.1.1.
- Upgraded lib on object mapper after org.zalando:problem-spring-web update.

### Removed
- Replaced RestTemplate with WebClient for non-blocking HTTP requests.


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

