# utils.cmd.httpServer project

A simple HTTP server that consists of a single executable and serves a folder from the
machine. It may be useful when you need to open an HTML page or set of web resources
in a browser, and they require an HTTP server to be opened correctly (pages making
AJAX requests to local resources, Selenium reports, etc.).

This project uses Quarkus, the Supersonic Subatomic Java Framework. :)

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ . 

## Usage:
1. server # for default localhost:8080
2. server 8081 # for localhost and port 8081
3. server 0.0.0.0:8081 # for defining host and port to listen on

## Note about ports

The server will start on port 8080 by default. However, Quarkus starts listening on another
HTTP port, which is required for some system purposes. I couldn't find any way to disable it
and all I managed to do so far is to make Quarkus pick some random port by setting
```quarkus.http.port=0``` (see https://quarkus.io/guides/http-reference for details). Maybe there 
can exist any interference with the server port, but I don't think it can happen in 99.999999%
of all the cases.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `utils.cmd.httpServer-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar target/utils.cmd.httpServer-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/utils.cmd.httpServer-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.

## Building native executable on Windows

I used Windows 10 x64 VM. Reference for building native Quarkus images: https://quarkus.io/guides/building-native-image

Steps on Windows:

1. Download and unpack GraalVM for Windows
2. Download and install Visual Studio 2017 Visual C++ Build Tools (https://aka.ms/vs/15/release/vs_buildtools.exe)
3. Set JAVA_HOME and GRAALVM_HOME to unpacked GraalVM folder
4. Run ```${GRAALVM_HOME}/bin/gu install native-image```
5. Add ```${GRAALVM_HOME}/bin``` to PATH
6. Add ```C:\Program Files (x86)\Microsoft Visual Studio\2017\BuildTools\VC\Tools\MSVC\14.16.27023\bin\Hostx64\x64``` to PATH
7. Open cmd, go to the project folder
8. Run ```"C:\Program Files (x86)\Microsoft Visual Studio\2017\BuildTools\VC\Auxiliary\Build\vcvars64"```
9. Run ```mvnw clean install```
10. Run ```mvnw package -Pnative```
