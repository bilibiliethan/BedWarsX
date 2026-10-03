package com.example.bedwars;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BwCommand implements CommandExecutor {
    private final BedWarsPlugin plugin;

    public BwCommand(BedWarsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c只有玩家可以使用这个命令！");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage("§e用法: /bw join");
            return true;
        }
        if (args[0].equalsIgnoreCase("join")) {
            plugin.joinGame(player, GameMode.NORMAL); // 调用插件的主类加入游戏
            return true;
        }
        return true;
    }
}
