package com.example.bedwars;

import org.bukkit.Material;

public enum GeneratorType {
    IRON(Material.IRON_INGOT, 20 * 2),
    GOLD(Material.GOLD_INGOT, 20 * 7),
    EMERALD(Material.EMERALD, 20 * 15);

    private final Material material;
    private final int intervalTicks;

    GeneratorType(Material material, int intervalTicks) {
        this.material = material;
        this.intervalTicks = intervalTicks;
    }

    public Material getMaterial() {
        return material;
    }

    public int getIntervalTicks() {
        return intervalTicks;
    }
}
