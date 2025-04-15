package com.lucacando.impactMines.listeners;

import com.lucacando.impactMines.Main;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener {

    Main main;

    public JoinLeaveListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        main.instantiatePlayer(player);
        e.setJoinMessage(main.getPlayerRank(player).getColor() + player.getDisplayName() + ChatColor.GRAY + " joined the game.");
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        main.instantiatePlayer(player);
        e.setQuitMessage(main.getPlayerRank(player).getColor() + player.getDisplayName() + ChatColor.GRAY + " left the game.");
    }


}
