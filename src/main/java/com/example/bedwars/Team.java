package com.example.bedwars;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Team {
    private final int id;
    private final Set<UUID> players = new HashSet<>();
    private boolean bedAlive = true;
    private Location bedLocation;
    private Location spawnLocation;
    private Location shopLocation;

    public Team(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int size() {
        return players.size();
    }

    public boolean isBedAlive() {
        return bedAlive;
    }

    public void setBedAlive(boolean bedAlive) {
        this.bedAlive = bedAlive;
    }

    public void addPlayer(Player player) {
        players.add(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
    }

    public boolean contains(Player player) {
        return players.contains(player.getUniqueId());
    }

    public Set<UUID> getPlayers() {
        return players;
    }

    public Location getBedLocation() {
        return bedLocation;
    }

    public void setBedLocation(Location bedLocation) {
        this.bedLocation = bedLocation;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public Location getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(Location shopLocation) {
        this.shopLocation = shopLocation;
    }
}
