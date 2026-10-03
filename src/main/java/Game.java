package com.example.bedwars;

import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game {
    private final BedWarsPlugin plugin;
    private final GameMode mode;
    private final MapData mapData;
    private final List<Player> players = new ArrayList<>();
    private final List<Team> teams = new ArrayList<>();
    private final UUID id = UUID.randomUUID();
    private boolean started = false;

    public Game(BedWarsPlugin plugin, GameMode mode, MapData mapData) {
        this.plugin = plugin;
        this.mode = mode;
        this.mapData = mapData;
        // 初始化队伍（根据地图数据，默认4个队伍）
        int teamCount = mapData.getTeamSpawns() != null ? mapData.getTeamSpawns().size() : 4;
        for (int i = 0; i < teamCount; i++) {
            teams.add(new Team(i, this));
        }
    }

    public UUID getId() { return id; }
    public GameMode getMode() { return mode; }
    public MapData getMapData() { return mapData; }
    public List<Player> getPlayers() { return players; }
    public List<Team> getTeams() { return teams; }
    public boolean isStarted() { return started; }
    public void setStarted(boolean started) { this.started = started; }

    public void addPlayer(Player player) {
        if (!players.contains(player)) {
            players.add(player);
            // 自动分配队伍逻辑
            for (Team team : teams) {
                if (team.getPlayers().size() < 2) { // 假设每队最多2人
                    team.addPlayer(player);
                    break;
                }
            }
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
        for (Team team : teams) {
            team.removePlayer(player);
        }
    }

    public boolean containsPlayer(Player player) {
        return players.contains(player);
    }
}
