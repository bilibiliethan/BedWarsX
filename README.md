# BedWarsX

Paper 1.20.5 plugin template for a small Bed Wars-like project.

Important: Paper 1.20.5 requires Java 21 on the server. The build uses Java 21 toolchain.

Features:
- lobby and game join commands
- map metadata management for spawns, beds, generators, shops
- XP resource conversion when players pick up iron/gold/emerald
- basic generator loop
- basic XP shop
- bed break detection and winner logic

## Requirements

- Java 21 (JDK 21) installed for building and required by the server runtime
- Gradle 8.6+ (recommended) — update the wrapper if necessary:

```bash
./gradlew wrapper --gradle-version 8.6
```

## Compile

```bash
# ensure the wrapper uses a Gradle version that supports Java 21 (8.6+)
./gradlew clean build
```

Jar will be generated under:

```text
build/libs/BedWarsX-0.1.0.jar
```

## Install

1. Copy the jar to your Paper server `plugins/` directory.
2. Start the server with Java 21 (example):

```bash
java -Xmx2G -Xms1G -jar paper-1.20.5.jar nogui
```

3. Example commands:

```text
/bw map create arena1
/bw map setspawn arena1 0
/bw map setbed arena1 0
/bw map setgenerator arena1 0 iron
/bw map setgenerator arena1 0 gold
/bw map setgenerator arena1 0 emerald
/bw map setshop arena1 0
/bw join squads arena1
/bw start
/bw shop
```

Note: Put the world directory for the map into `plugins/BedWarsX/maps/arena1` before importing.
