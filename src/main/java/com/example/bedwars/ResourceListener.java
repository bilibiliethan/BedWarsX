package com.example.bedwars;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class ResourceListener implements Listener {
    private final BedWarsPlugin plugin;

    public ResourceListener(BedWarsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player player)) {
            return;
        }

        if (plugin.getPlayerGame(player).isEmpty()) {
            return;
        }

        Item item = event.getItem();
        ItemStack stack = item.getItemStack();
        Material type = stack.getType();

        if (!isBedWarsResource(type)) {
            return;
        }

        boolean shouldConvert = plugin.isXpModeGame(player) || plugin.getConfig().getBoolean("xp-mode-global", false);
        if (!shouldConvert) {
            return;
        }

        event.setCancelled(true);

        int amount = stack.getAmount();
        int xp = getXpValue(type) * amount;

        player.giveExp(xp);
        item.remove();
        player.sendMessage("§a拾取资源并获得经验: " + xp);
    }

    private boolean isBedWarsResource(Material type) {
        return type == Material.IRON_INGOT || type == Material.GOLD_INGOT || type == Material.EMERALD;
    }

    private int getXpValue(Material type) {
        if (type == Material.IRON_INGOT) {
            return plugin.getConfig().getInt("xp-values.iron", 3);
        }
        if (type == Material.GOLD_INGOT) {
            return plugin.getConfig().getInt("xp-values.gold", 7);
        }
        if (type == Material.EMERALD) {
            return plugin.getConfig().getInt("xp-values.emerald", 10);
        }
        return 0;
    }
}
