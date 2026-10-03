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

    // 补全的方法 1：根据玩家获取所在队伍
    public Team getTeam(Player player) {
        for (Team team : teams) {
            if (team.contains(player)) {
                return team;
            }
        }
        return null;
    }

    // 补全的方法 2：向游戏内所有玩家广播消息
    public void broadcast(String message) {
        for (Player player : players) {
            player.sendMessage(message);
        }
    }

    // 补全的方法 3：检查游戏是否结束（例如只剩一队）
    public void checkWinner() {
        List<Team> aliveTeams = new ArrayList<>();
        for (Team team : teams) {
            if (!team.getPlayers().isEmpty()) {
                aliveTeams.add(team);
            }
        }
        if (aliveTeams.size() == 1) {
            broadcast("§e游戏结束！获胜队伍: " + aliveTeams.get(0).getId());
            // 在这里可以添加结束游戏、重置状态的逻辑
            this.started = false;
        }
    }

    public void addPlayer(Player player) {
        if (!players.contains(player)) {
            players.add(player);
            for (Team team : teams) {
                if (team.getPlayers().size() < 2) {
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
