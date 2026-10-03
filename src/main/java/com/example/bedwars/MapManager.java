package com.example.bedwars;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MapManager {
    private final JavaPlugin plugin;
    private final File mapsRoot;

    public MapManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.mapsRoot = new File(plugin.getDataFolder(), "maps");
    }

    public void ensureMapsFolder() {
        if (!mapsRoot.exists()) {
            if (mapsRoot.mkdirs()) {
                plugin.getLogger().info("Maps folder created: " + mapsRoot.getAbsolutePath());
            } else {
                plugin.getLogger().warning("Failed to create maps folder: " + mapsRoot.getAbsolutePath());
            }
        }
    }

    public File getMapsRoot() {
        return mapsRoot;
    }

    public List<String> listMaps() {
        if (!mapsRoot.exists()) {
            return Collections.emptyList();
        }
        File[] dirs = mapsRoot.listFiles(File::isDirectory);
        if (dirs == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(dirs)
                .map(File::getName)
                .sorted()
                .collect(Collectors.toList());
    }

    public boolean createMapFolder(String name) {
        if (name == null || name.isBlank()) {
            return false;
        }
        File dir = new File(mapsRoot, name);
        if (dir.exists()) {
            return false;
        }
        return dir.mkdirs();
    }

    public File getMapFolder(String name) {
        return new File(mapsRoot, name);
    }

    public File getMetaFile(String name) {
        return new File(getMapFolder(name), "meta.yml");
    }

    public void saveMapData(MapData data) throws IOException {
        File meta = getMetaFile(data.getName());
        File parent = meta.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }

        FileConfiguration cfg = YamlConfiguration.loadConfiguration(meta);
        cfg.set("name", data.getName());

        cfg.createSection("spawns");
        for (Map.Entry<Integer, Location> entry : data.getTeamSpawns().entrySet()) {
            saveLocation(cfg, "spawns." + entry.getKey(), entry.getValue());
        }

        cfg.createSection("beds");
        for (Map.Entry<Integer, Location> entry : data.getTeamBeds().entrySet()) {
            saveLocation(cfg, "beds." + entry.getKey(), entry.getValue());
        }

        cfg.createSection("shops");
        for (Map.Entry<Integer, Location> entry : data.getTeamShops().entrySet()) {
            saveLocation(cfg, "shops." + entry.getKey(), entry.getValue());
        }

        cfg.createSection("generators");
        for (Map.Entry<Integer, Map<String, Location>> teamEntry : data.getTeamGenerators().entrySet()) {
            String teamKey = String.valueOf(teamEntry.getKey());
            for (Map.Entry<String, Location> gen : teamEntry.getValue().entrySet()) {
                saveLocation(cfg, "generators." + teamKey + "." + gen.getKey(), gen.getValue());
            }
        }

        cfg.save(meta);
    }

    public Optional<MapData> loadMapData(String mapName) {
        File meta = getMetaFile(mapName);
        if (!meta.exists()) {
            return Optional.empty();
        }

        FileConfiguration cfg = YamlConfiguration.loadConfiguration(meta);
        MapData data = new MapData(mapName);

        if (cfg.isConfigurationSection("spawns")) {
            for (String key : cfg.getConfigurationSection("spawns").getKeys(false)) {
                try {
                    int team = Integer.parseInt(key);
                    Location loc = loadLocation(cfg, "spawns." + key);
                    if (loc != null) {
                        data.setSpawn(team, loc);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }

        if (cfg.isConfigurationSection("beds")) {
            for (String key : cfg.getConfigurationSection("beds").getKeys(false)) {
                try {
                    int team = Integer.parseInt(key);
                    Location loc = loadLocation(cfg, "beds." + key);
                    if (loc != null) {
                        data.setBed(team, loc);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }

        if (cfg.isConfigurationSection("shops")) {
            for (String key : cfg.getConfigurationSection("shops").getKeys(false)) {
                try {
                    int team = Integer.parseInt(key);
                    Location loc = loadLocation(cfg, "shops." + key);
                    if (loc != null) {
                        data.setShop(team, loc);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }

        if (cfg.isConfigurationSection("generators")) {
            for (String teamKey : cfg.getConfigurationSection("generators").getKeys(false)) {
                try {
                    int team = Integer.parseInt(teamKey);
                    for (String genKey : cfg.getConfigurationSection("generators." + teamKey).getKeys(false)) {
                        Location loc = loadLocation(cfg, "generators." + teamKey + "." + genKey);
                        if (loc != null) {
                            data.setGenerator(team, genKey, loc);
                        }
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }

        return Optional.of(data);
    }

    public boolean importWorld(String mapName) {
        File mapDir = getMapFolder(mapName);
        if (!mapDir.exists() || !mapDir.isDirectory()) {
            plugin.getLogger().warning("Map folder not found: " + mapDir.getAbsolutePath());
            return false;
        }

        if (Bukkit.getWorld(mapName) != null) {
            return true;
        }

        File serverWorld = new File(Bukkit.getWorldContainer(), mapName);
        if (serverWorld.exists() && new File(serverWorld, "level.dat").exists()) {
            World world = Bukkit.createWorld(new WorldCreator(mapName));
            return world != null;
        }

        File targetDir = new File(Bukkit.getWorldContainer(), mapName);
        if (!targetDir.exists()) {
            try {
                copyDirectory(mapDir.toPath(), targetDir.toPath());
            } catch (IOException e) {
                plugin.getLogger().log(java.util.logging.Level.SEVERE, "Failed to copy map folder", e);
                return false;
            }
        }

        if (!new File(targetDir, "level.dat").exists()) {
            plugin.getLogger().warning("Map folder is not a valid world folder: " + targetDir.getAbsolutePath());
            return false;
        }

        World world = Bukkit.createWorld(new WorldCreator(mapName));
        return world != null;
    }

    public void copyDirectory(Path source, Path target) throws IOException {
        Files.walk(source).forEach(path -> {
            try {
                Path rel = source.relativize(path);
                Path targetPath = target.resolve(rel.toString());
                if (Files.isDirectory(path)) {
                    if (!Files.exists(targetPath)) {
                        Files.createDirectories(targetPath);
                    }
                } else {
                    Files.createDirectories(targetPath.getParent());
                    Files.copy(path, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void saveLocation(FileConfiguration cfg, String path, Location loc) {
        if (loc == null || loc.getWorld() == null) {
            return;
        }
        cfg.set(path + ".world", loc.getWorld().getName());
        cfg.set(path + ".x", loc.getX());
        cfg.set(path + ".y", loc.getY());
        cfg.set(path + ".z", loc.getZ());
        cfg.set(path + ".yaw", loc.getYaw());
        cfg.set(path + ".pitch", loc.getPitch());
    }

    private Location loadLocation(FileConfiguration cfg, String path) {
        if (!cfg.contains(path + ".x")) {
            return null;
        }

        String worldName = cfg.getString(path + ".world");
        World world = worldName == null || worldName.isBlank() ? null : Bukkit.getWorld(worldName);

        double x = cfg.getDouble(path + ".x");
        double y = cfg.getDouble(path + ".y");
        double z = cfg.getDouble(path + ".z");
        float yaw = (float) cfg.getDouble(path + ".yaw");
        float pitch = (float) cfg.getDouble(path + ".pitch");

        if (world == null) {
            return new Location(null, x, y, z, yaw, pitch);
        }
        return new Location(world, x, y, z, yaw, pitch);
    }
}
