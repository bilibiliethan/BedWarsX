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

public class MapEditorGUI implements Listener {
    private final BedWarsPlugin plugin;

    public MapEditorGUI(BedWarsPlugin plugin) {
        this.plugin = plugin;
    }

    public void openMapList(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, "BedWars Maps");
        List<String> maps = plugin.getMapManager().listMaps();

        if (maps.isEmpty()) {
            ItemStack empty = new ItemStack(Material.BARRIER);
            ItemMeta meta = empty.getItemMeta();
            meta.setDisplayName(ChatColor.RED + "没有地图");
            empty.setItemMeta(meta);
            inv.setItem(22, empty);
            player.openInventory(inv);
            return;
        }

        int slot = 0;
        for (String mapName : maps) {
            if (slot >= 54) {
                break;
            }

            ItemStack item = new ItemStack(Material.MAP);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + mapName);

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "点击编辑此地图");
            meta.setLore(lore);

            item.setItemMeta(meta);
            inv.setItem(slot, item);
            slot++;
        }

        player.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        String title = event.getView().getTitle();
        if (title == null) {
            return;
        }

        if (title.equals("BedWars Maps")) {
            event.setCancelled(true);

            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || clicked.getType() == Material.AIR) {
                return;
            }

            ItemMeta meta = clicked.getItemMeta();
            if (meta == null || !meta.hasDisplayName()) {
                return;
            }

            String raw = ChatColor.stripColor(meta.getDisplayName());
            if (raw == null || raw.isBlank()) {
                return;
            }

            player.sendMessage(ChatColor.YELLOW + "请使用命令进行精确设置，比如:");
            player.sendMessage(ChatColor.YELLOW + " /bw map setspawn " + raw + " 0");
            player.sendMessage(ChatColor.YELLOW + " /bw map setbed " + raw + " 0");
            player.sendMessage(ChatColor.YELLOW + " /bw map setgenerator " + raw + " 0 iron");
            player.sendMessage(ChatColor.YELLOW + " /bw map setshop " + raw + " 0");
        }
    }
}
