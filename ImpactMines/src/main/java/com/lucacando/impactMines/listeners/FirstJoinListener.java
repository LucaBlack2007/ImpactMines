package com.lucacando.impactMines.listeners;

import com.lucacando.impactMines.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class FirstJoinListener implements Listener {

    Main main;

    public FirstJoinListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        main.instantiatePlayer(e.getPlayer());
    }
}
