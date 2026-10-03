package com.example.bedwars;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MapData {
    private final String name;
    private final Map<Integer, Location> teamSpawns = new HashMap<>();
    private final Map<Integer, Location> teamBeds = new HashMap<>();
    private final Map<Integer, Location> teamShops = new HashMap<>();
    private final Map<Integer, Map<String, Location>> teamGenerators = new HashMap<>();

    public MapData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<Integer, Location> getTeamSpawns() {
        return teamSpawns;
    }

    public Map<Integer, Location> getTeamBeds() {
        return teamBeds;
    }

    public Map<Integer, Location> getTeamShops() {
        return teamShops;
    }

    public Map<Integer, Map<String, Location>> getTeamGenerators() {
        return teamGenerators;
    }

    public void setSpawn(int teamIndex, Location loc) {
        teamSpawns.put(teamIndex, loc);
    }

    public void setBed(int teamIndex, Location loc) {
        teamBeds.put(teamIndex, loc);
    }

    public void setShop(int teamIndex, Location loc) {
        teamShops.put(teamIndex, loc);
    }

    public void setGenerator(int teamIndex, String type, Location loc) {
        teamGenerators.computeIfAbsent(teamIndex, k -> new HashMap<>()).put(type.toLowerCase(), loc);
    }

    public Optional<Location> getSpawn(int teamIndex) {
        return Optional.ofNullable(teamSpawns.get(teamIndex));
    }

    public Optional<Location> getBed(int teamIndex) {
        return Optional.ofNullable(teamBeds.get(teamIndex));
    }

    public Optional<Location> getShop(int teamIndex) {
        return Optional.ofNullable(teamShops.get(teamIndex));
    }

    public Optional<Location> getGenerator(int teamIndex, String type) {
        Map<String, Location> map = teamGenerators.get(teamIndex);
        if (map == null) return Optional.empty();
        return Optional.ofNullable(map.get(type.toLowerCase()));
    }

    public int getTeamCount() {
        int max = 0;
        max = Math.max(max, teamSpawns.size());
        max = Math.max(max, teamBeds.size());
        max = Math.max(max, teamShops.size());
        for (Map<String, Location> team : teamGenerators.values()) {
            max = Math.max(max, team.size());
        }
        return Math.max(1, max);
    }
}
