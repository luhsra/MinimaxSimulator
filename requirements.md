---
title: Get Started
last_updated: Mar 16, 2016
keywords: requirements, java, maven, compile, compiling from source, compiling, source, source code
---

## Compiling from Source

In order to compile the Minimax simulator from source you have to meet the following prerequirements (newer versions should also work):

* JDK 8u40
* Apache Maven 3.0.5

<br />
The recommended way to compile the simulator from source code is:
```bash
mvn clean compile assembly:single
```

To create a JaCoCo coverage report run:
```bash
mvn clean test -P test-coverage
```

## Running the Simulator

In order to run the simulator you need

* JRE 8u40 or higher

<br />
Assuming you are on Mac or Linux and just compiled the latest version from source start the application with:
```bash
java -jar target/minimax_simulator-{version}-jar-with-dependencies.jar
```