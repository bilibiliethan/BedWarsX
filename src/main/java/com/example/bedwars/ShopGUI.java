package com.example.bedwars;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ShopGUI implements Listener {

    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9 * 3, "BedWars XP Shop");

        addItem(inv, 0, Material.IRON_SWORD, "铁剑", 12);
        addItem(inv, 1, Material.STONE_SWORD, "石剑", 8);
        addItem(inv, 2, Material.BOW, "弓", 16);
        addItem(inv, 3, Material.IRON_PICKAXE, "铁镐", 12);
        addItem(inv, 4, Material.IRON_AXE, "铁斧", 12);
        addItem(inv, 5, Material.IRON_CHESTPLATE, "铁胸甲", 22);
        addItem(inv, 6, Material.IRON_BOOTS, "铁靴子", 18);
        addItem(inv, 7, Material.COOKED_BEEF, "恢复食物", 5);
        addItem(inv, 8, Material.ENDER_PEARL, "末影珍珠", 24);

        player.openInventory(inv);
    }

    private static void addItem(Inventory inv, int slot, Material material, String name, int cost) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + name);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW + "花费: " + cost + " 经验");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(slot, item);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        if (!event.getView().getTitle().equalsIgnoreCase("BedWars XP Shop")) {
            return;
        }

        event.setCancelled(true);

        ItemStack stack = event.getCurrentItem();
        if (stack == null || stack.getType() == Material.AIR) {
            return;
        }

        ItemMeta meta = stack.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return;
        }

        String display = ChatColor.stripColor(meta.getDisplayName());
        int cost = getCost(display);
        if (cost <= 0) {
            return;
        }

        if (!XpShopUtil.canAfford(player, cost)) {
            player.sendMessage(ChatColor.RED + "经验不足，至少需要 " + cost + " 点经验。");
            return;
        }

        if (XpShopUtil.pay(player, cost)) {
            switch (display) {
                case "铁剑" -> player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
                case "石剑" -> player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
                case "弓" -> player.getInventory().addItem(new ItemStack(Material.BOW));
                case "铁镐" -> player.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE));
                case "铁斧" -> player.getInventory().addItem(new ItemStack(Material.IRON_AXE));
                case "铁胸甲" -> player.getInventory().addItem(new ItemStack(Material.IRON_CHESTPLATE));
                case "铁靴子" -> player.getInventory().addItem(new ItemStack(Material.IRON_BOOTS));
                case "恢复食物" -> player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 3));
                case "末影珍珠" -> player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
                default -> {
                }
            }
            player.sendMessage(ChatColor.GREEN + "购买成功！");
        }
    }

    private int getCost(String displayName) {
        return switch (displayName) {
            case "铁剑" -> 12;
            case "石剑" -> 8;
            case "弓" -> 16;
            case "铁镐" -> 12;
            case "铁斧" -> 12;
            case "铁胸甲" -> 22;
            case "铁靴子" -> 18;
            case "恢复食物" -> 5;
            case "末影珍珠" -> 24;
            default -> 0;
        };
    }
}
