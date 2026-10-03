package com.example.bedwars;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Optional;

public class BedBreakListener implements Listener {
    private final BedWarsPlugin plugin;

    public BedBreakListener(BedWarsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBedBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!isBed(block)) {
            return;
        }

        Player player = event.getPlayer();
        Optional<Game> gameOpt = plugin.getPlayerGame(player);
        if (gameOpt.isEmpty()) {
            return;
        }

        Game game = gameOpt.get();
        if (game.getState() != GameState.STARTED) {
            return;
        }

        Team targetTeam = findTeamByBedLocation(game, block.getLocation());
        if (targetTeam == null) {
            return;
        }

        Team playerTeam = game.getTeam(player);
        if (playerTeam == targetTeam) {
            player.sendMessage("§c你不能破坏自己的床！");
            event.setCancelled(true);
            return;
        }

        targetTeam.setBedAlive(false);
        game.broadcast("§c队伍 " + targetTeam.getId() + " 的床已被摧毁！");
        player.sendMessage("§a你破坏了队伍 " + targetTeam.getId() + " 的床！");

        game.checkWinner();
    }

    private boolean isBed(Block block) {
        return block.getType().name().toUpperCase().endsWith("BED");
    }

    private Team findTeamByBedLocation(Game game, Location blockLocation) {
        for (Team team : game.getTeams()) {
            if (team.getBedLocation() == null) {
                continue;
            }
            if (team.getBedLocation().getWorld() == blockLocation.getWorld()
                    && team.getBedLocation().distanceSquared(blockLocation) < 2.5D) {
                return team;
            }
        }
        return null;
    }
}
