ds3_contract_comparator
=======================

Compatible with Java 8 and above.

## Install
The contract comparator utilizes code from [Ds3 Autogen](https://github.com/SpectraLogic/ds3_autogen) through the 
[Composite Build](https://docs.gradle.org/current/userguide/composite_builds.html) feature available in Gradle version 3.2+.
You must clone both repositories in the same parent folder.

Clone the [Ds3 Autogen](https://github.com/SpectraLogic/ds3_autogen) and the [Ds3 Contract Comparator](https://github.com/SpectraLogic/ds3_contract_comparator) project into the same parent folder.
The gradle build file of Ds3 Contract Comparator will locate the Ds3 Autogen build if both repositories are located in the same folder.

    `git clone https://github.com/SpectraLogic/ds3_autogen.git`
    `git clone https://github.com/SpectraLogic/ds3_contract_comparator.git`

To build the project via command line, cd into the parent folder of Ds3 Contract Comparator and build using gradle.
    `cd ds3_contract_comparator`
    `./gradlew clean build`

## Setup Using IntelliJ

Compatible with [IntelliJ 2016.3](https://www.jetbrains.com/idea/whatsnew/#v2016-3) or higher. Lower versions do not support Composite Builds.

Import Ds3 Contract Comparator into IntelliJ
- Open IntelliJ and select `Import Project`
- Find the `build.gradle` file contained at the root of the Ds3 Contract Comparator project and select it
- Accept the defaults
- There will be a pop-up asking you to `select modules/data to include in the project`, select all four and click `OK`. The module names are:
  - `ds3_contract_comparator` (root)
  - `:ds3-contract-comparator`
  - `:ds3-contract-comparator-cli`
  - `:ds3-contract-comparator-print`

Add Ds3 Autogen as a Composite Build
- Click on the `Gradle` tab located on the right-hand side of the IDE
  - Click on the `+` icon to attach a gradle project
  - Find the `build.gradle` file contained at the root of the Ds3 Autogen project and select it
  - Accept the defaults and import all modules
  - Within the `Gradle Projects` window right-click on the `ds3-contract-comparator` and select `Composite Build Configuration` 
  - In the pop-up select `ds3_autogen` and click `OK`
- Refresh Gradle

## Tests

To run the tests use:

    `./gradlew clean test`
