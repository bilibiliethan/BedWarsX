# BedWarsX

Paper 1.20.5 plugin template for a small Bed Wars-like project.

Features:
- lobby and game join commands
- map metadata management for spawns, beds, generators, shops
- XP resource conversion when players pick up iron/gold/emerald
- basic generator loop
- basic XP shop
- bed break detection and winner logic

## Compile

```bash
./gradlew clean build
```

Jar will be generated under:

```text
build/libs/BedWarsX-0.1.0.jar
```

## Install

1. Copy the jar to your Paper server `plugins/` directory.
2. Start the server.
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
