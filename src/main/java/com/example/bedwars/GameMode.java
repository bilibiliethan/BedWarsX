package com.example.bedwars;

public enum GameMode {
    SQUADS(4, 4),
    DOUBLES(8, 2),
    SOLO(8, 1),
    XP_MODE(4, 4);

    private final int teamCount;
    private final int maxPlayersPerTeam;

    GameMode(int teamCount, int maxPlayersPerTeam) {
        this.teamCount = teamCount;
        this.maxPlayersPerTeam = maxPlayersPerTeam;
    }

    public int getTeamCount() {
        return teamCount;
    }

    public int getMaxPlayersPerTeam() {
        return maxPlayersPerTeam;
    }

    public static GameMode fromString(String raw) {
        if (raw == null) return null;
        try {
            return GameMode.valueOf(raw.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
