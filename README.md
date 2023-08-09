Minimax Simulator
=================

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/luhsra/MinimaxSimulator/blob/develop/LICENSE.txt) &nbsp; [![Latest release](http://img.shields.io/github/release/luhsra/MinimaxSimulator.svg)](https://github.com/luhsra/MinimaxSimulator/releases)	&nbsp; [![Java CI with Maven](https://github.com/luhsra/MinimaxSimulator/actions/workflows/test.yml/badge.svg)](https://github.com/luhsra/MinimaxSimulator/actions/workflows/test.yml)

Minimax Simulator is a platform independent GUI-based Minimax simulator written in Java.

**Content:**
- [System Requirements](#system-requirements)
- [Compiling from Source](#compiling-source)
- [Starting the Simulator](#executing)
- [How the Simulator works](#documentation)
- [Found an Issue or Bug?](#bug)
- [Requesting a Feature?](#feature)
- [Change Log](#changelog)
- [License](#license)

<a name="system-requirements"></a> System Requirements
------------------------------------------------------
* JRE 11 or higher
* (optional for smoother GUI: libcanberra-gtk-module)

<a name="compiling-source"></a> Compiling from Source
------------------------------------------------------
#### Prerequirements
* JDK 11
* Apache Maven 3.0.5

#### Compilation
The recommended way to compile the simulator from source code is:
```bash
mvn clean compile
```
\
In order to create a working and executable JAR file run the following command.
This will automatically compile the source if no compiled sources are available.
```bash
mvn package
```
\
The best way to create a new JAR file is therefore:
```bash
mvn clean package
```

#### JUnitTests
To run the UnitTests enter:
```bash
mvn test
```

<a name="executing"></a> Starting the Simulator 
------------------------------------------------------
To start the simulator enter:
```bash
java -jar target/minimax_simulator-2.0.0-jar-with-dependencies.jar
```

If you get an error like
``gdk_x11_display_set_window_scale: assertion 'GDK_IS_X11_DISPLAY (display)' failed``
, try:
```bash
java -Djdk.gtk.version=2 -jar target/minimax_simulator-2.0.0-jar-with-dependencies.jar
```
<a name="documentation"></a> How the Simulator works 
------------------------------------------------------
See https://luhsra.github.io/MinimaxSimulator/

<a name="bug"></a> Found an Issue or Bug?
-----------------------------------------
If you found a bug or any kind of mistake, please let us know by opening up an issue.

You are welcome to submit a pull request with your fix afterwards.

<a name="feature"></a> Requesting a Feature?
--------------------------------------------
If you are missing a feature within the simulator, feel free to ask us about it by adding a new request by opening up a new issue with the prefix `[feature request]`.

Note that usually you can speed up the process by submitting a pull request providing the needed changes for your requested feature.

<a name="changelog"></a> Change Log
---------------------------------
See [Change Log](https://luhsra.github.io/MinimaxSimulator/changelog.html)

<a name="license"></a> License
------------------------------
Distributed under [MIT license](http://opensource.org/licenses/MIT).

Copyright (c) 2013-2019 Leibniz Universität Hannover, Institut für Systems Engineering, Fachgebiet System- und Rechnerarchitektur
