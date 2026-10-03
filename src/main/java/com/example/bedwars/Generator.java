package com.example.bedwars;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Generator {
    private final BedWarsPlugin plugin;
    private final GeneratorType type;
    private final Location location;
    private BukkitTask task;

    public Generator(BedWarsPlugin plugin, GeneratorType type, Location location) {
        this.plugin = plugin;
        this.type = type;
        this.location = location.clone();
    }

    public void start() {
        if (task != null && !task.isCancelled()) {
            return;
        }

        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (location == null || location.getWorld() == null) {
                    return;
                }

                Location dropLoc = location.clone().add(0.5, 1.0, 0.5);
                location.getWorld().dropItemNaturally(dropLoc, new ItemStack(type.getMaterial(), 1));
            }
        }.runTaskTimer(plugin, 20L, type.getIntervalTicks());
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }
}
