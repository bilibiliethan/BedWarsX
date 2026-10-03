package com.example.bedwars;

import org.bukkit.entity.Player;

public final class XpShopUtil {

    private XpShopUtil() {
    }

    public static boolean canAfford(Player player, int cost) {
        return player.getTotalExperience() >= cost;
    }

    public static boolean pay(Player player, int cost) {
        if (!canAfford(player, cost)) {
            return false;
        }

        player.giveExp(-cost);
        return true;
    }
}
