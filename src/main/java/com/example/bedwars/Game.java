package com.example.bedwars;

import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game {
    private final UUID id;
    private final GameMode mode;
    private final MapData mapData;
    private GameState state = GameState.WAITING;
    private final List<Player> players = new ArrayList<>();
    private final List<Team> teams = new ArrayList<>();

    // 适配 BedWarsPlugin 里的调用：new Game(UUID.randomUUID(), mode)
    public Game(UUID id, GameMode mode) {
        this(id, mode, null);
    }

    // 适配 BedWarsPlugin 里的调用：new Game(UUID.randomUUID(), mode, mapData)
    public Game(UUID id, GameMode mode, MapData mapData) {
        this.id = id;
        this.mode = mode;
        this.mapData = mapData;
        int teamCount = (mapData != null && mapData.getTeamSpawns() != null) ? mapData.getTeamSpawns().size() : 4;
        for (int i = 0; i < teamCount; i++) {
            teams.add(new Team(i, this));
        }
    }

    public UUID getId() { return id; }
    public GameMode getMode() { return mode; }
    public MapData getMapData() { return mapData; }
    public GameState getState() { return state; }
    public void setState(GameState state) { this.state = state; }
    public List<Player> getPlayers() { return players; }
    public List<Team> getTeams() { return teams; }

    // 修改为返回 boolean，适配 BedWarsPlugin 里的 boolean added = game.addPlayer(player);
    public boolean addPlayer(Player player) {
        if (!players.contains(player)) {
            players.add(player);
            for (Team team : teams) {
                if (team.getPlayers().size() < 2) {
                    team.addPlayer(player);
                    break;
                }
            }
            return true;
        }
        return false;
    }

    public void removePlayer(Player player) {
        players.remove(player);
        for (Team team : teams) {
            team.removePlayer(player);
        }
    }

    public Team getTeam(Player player) {
        for (Team team : teams) {
            if (team.contains(player)) return team;
        }
        return null;
    }

    public void broadcast(String message) {
        for (Player player : players) player.sendMessage(message);
    }

    public void checkWinner() {
        List<Team> alive = new ArrayList<>();
        for (Team team : teams) {
            if (!team.getPlayers().isEmpty()) alive.add(team);
        }
        if (alive.size() == 1) {
            broadcast("§e游戏结束！获胜队伍: " + alive.get(0).getId());
            state = GameState.ENDED;
        }
    }
}
