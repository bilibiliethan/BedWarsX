package com.example.bedwars;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class CombatListener implements Listener {
    private final BedWarsPlugin plugin;

    public CombatListener(BedWarsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Optional<Game> gameOpt = plugin.getPlayerGame(player);
        if (gameOpt.isEmpty()) {
            return;
        }

        Game game = gameOpt.get();
        Team team = game.getTeam(player);
        if (team == null) {
            return;
        }

        if (!team.isBedAlive()) {
            team.removePlayer(player);
            game.checkWinner();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Optional<Game> gameOpt = plugin.getPlayerGame(player);
        if (gameOpt.isEmpty()) {
            return;
        }

        Game game = gameOpt.get();
        Team team = game.getTeam(player);
        if (team != null) {
            team.removePlayer(player);
        }

        plugin.leaveGame(player);
        game.checkWinner();
    }
}
