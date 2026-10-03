package com.example.bedwars;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class Team {
    private final int id;
    private final Game game;
    private final List<Player> players = new ArrayList<>();
    private Location bedLocation;
    private Location spawnLocation;
    private Location shopLocation;
    private boolean bedAlive = true;  // 新增：床是否存活

    public Team(int id, Game game) {
        this.id = id;
        this.game = game;
    }

    public int getId() { return id; }
    public Game getGame() { return game; }
    public List<Player> getPlayers() { return players; }
    
    public void addPlayer(Player player) { 
        if (!players.contains(player)) players.add(player); 
    }
    public void removePlayer(Player player) { 
        players.remove(player); 
    }
    public boolean contains(Player player) { 
        return players.contains(player); 
    }

    public Location getBedLocation() { return bedLocation; }
    public void setBedLocation(Location bedLocation) { this.bedLocation = bedLocation; }
    public Location getSpawnLocation() { return spawnLocation; }
    public void setSpawnLocation(Location spawnLocation) { this.spawnLocation = spawnLocation; }
    public Location getShopLocation() { return shopLocation; }
    public void setShopLocation(Location shopLocation) { this.shopLocation = shopLocation; }

    // 新增：床状态相关方法
    public boolean isBedAlive() { return bedAlive; }
    public void setBedAlive(boolean bedAlive) { this.bedAlive = bedAlive; }
}
