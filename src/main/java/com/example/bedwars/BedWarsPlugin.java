package com.example.bedwars;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class BedWarsPlugin extends JavaPlugin {

    private final Map<UUID, Game> games = new HashMap<>();
    private final Map<UUID, UUID> playerGameMap = new HashMap<>();
    private Location lobbyLocation;
    private MapManager mapManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadLobbyFromConfig();

        this.mapManager = new MapManager(this);
        this.mapManager.ensureMapsFolder();

        getCommand("bw").setExecutor(new BwCommand(this));

        Bukkit.getPluginManager().registerEvents(new ResourceListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ShopGUI(), this);
        Bukkit.getPluginManager().registerEvents(new BedBreakListener(this), this);
        Bukkit.getPluginManager().registerEvents(new CombatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new MapEditorGUI(this), this);

        getLogger().info("BedWarsX enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("BedWarsX disabled.");
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public void loadLobbyFromConfig() {
        if (!getConfig().contains("lobby.world")) {
            return;
        }

        String worldName = getConfig().getString("lobby.world");
        if (worldName == null || Bukkit.getWorld(worldName) == null) {
            return;
        }

        double x = getConfig().getDouble("lobby.x");
        double y = getConfig().getDouble("lobby.y");
        double z = getConfig().getDouble("lobby.z");
        float yaw = (float) getConfig().getDouble("lobby.yaw");
        float pitch = (float) getConfig().getDouble("lobby.pitch");

        lobbyLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
    }

    public void setLobby(Location location) {
        this.lobbyLocation = location.clone();

        getConfig().set("lobby.world", location.getWorld().getName());
        getConfig().set("lobby.x", location.getX());
        getConfig().set("lobby.y", location.getY());
        getConfig().set("lobby.z", location.getZ());
        getConfig().set("lobby.yaw", location.getYaw());
        getConfig().set("lobby.pitch", location.getPitch());
        saveConfig();
    }

    public Optional<Location> getLobbyLocation() {
        return Optional.ofNullable(lobbyLocation);
    }

    public Game createGame(GameMode mode) {
        Game game = new Game(UUID.randomUUID(), mode);
        games.put(game.getId(), game);
        return game;
    }

    public Game createGame(GameMode mode, MapData mapData) {
        Game game = new Game(UUID.randomUUID(), mode, mapData);
        games.put(game.getId(), game);
        return game;
    }

    public Optional<Game> getGame(UUID id) {
        return Optional.ofNullable(games.get(id));
    }

    public Optional<Game> getPlayerGame(Player player) {
        UUID gameId = playerGameMap.get(player.getUniqueId());
        if (gameId == null) {
            return Optional.empty();
        }
        return getGame(gameId);
    }

    public void joinGame(Player player, GameMode mode) {
        if (playerGameMap.containsKey(player.getUniqueId())) {
            return;
        }

        Game game = createGame(mode);
        boolean added = game.addPlayer(player);
        if (added) {
            playerGameMap.put(player.getUniqueId(), game.getId());
        }
    }

    public void joinGame(Player player, GameMode mode, MapData mapData) {
        if (playerGameMap.containsKey(player.getUniqueId())) {
            return;
        }

        Game game = createGame(mode, mapData);
        boolean added = game.addPlayer(player);
        if (added) {
            playerGameMap.put(player.getUniqueId(), game.getId());
        }
    }

    public void leaveGame(Player player) {
        UUID gameId = playerGameMap.remove(player.getUniqueId());
        if (gameId == null) {
            return;
        }
        Game game = games.get(gameId);
        if (game != null) {
            game.removePlayer(player);
            games.remove(gameId);
        }
    }

    public boolean isXpModeGame(Player player) {
        Optional<Game> optionalGame = getPlayerGame(player);
        return optionalGame.isPresent() && optionalGame.get().getMode() == GameMode.XP_MODE;
    }
}
