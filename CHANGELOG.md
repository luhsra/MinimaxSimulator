Change Log
==========

All notable changes to this project will be documented in this file.

## 1.1.0 - 2016-08-29
### Added
- undo / redo feature
- language selection (English / German)

### Changed
- HTML signal table exporter now uses template file and HTML5
- Now using Java's Properties class for application configuration

### Fixed
- some GUI elements were not correctly reset after loading or creating a project
- cancel action of waiting dialogs was not processed if the dialog was closed via pressing escape
- machine schematics exported as JPG did not have a white background
- button labels for adding and removing multiplexer connections were not localized
- button icon for adding a register was not the correct one
- multiplexer selection code in MuxView was extended too early
- machine configuration menu item was not localized

## 1.0.1 - 2015-10-15
### Fixed
- machine schematics were not updated on changes
- base machine did not match the base machine from the lectures

## 1.0.0 - 2015-10-09
### Changed
- replaced the Swing GUI with a JavaFX GUI
- projects are saved as JSON instead of XML

### Added
- the following attributes are updated after changes of signal rows:
  - ALU operation codes
  - ALU selection codes
  - Jump targets
