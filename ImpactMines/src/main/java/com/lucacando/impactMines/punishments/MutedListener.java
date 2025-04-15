package com.lucacando.impactMines.punishments;

import com.lucacando.impactMines.Main;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.AbstractMap;
import java.util.Date;

public class MutedListener implements Listener {

    Main main;
    PunishmentManager manager;

    public MutedListener(Main main) {
        this.main = main;
        manager = main.getPunishmentManager();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (manager.isPlayerMuted(e.getPlayer())) {
            e.setCancelled(true);
            AbstractMap.SimpleEntry<Date, String> info = manager.getMuteExpirationInfo(e.getPlayer());
            if (info != null) {
                Date expiresAt = info.getKey();
                String timeLeft = info.getValue();
                if (expiresAt == null) {
                    e.getPlayer().sendMessage(main.prefix + ChatColor.RED + "You are muted permanently.");
                } else {
                    e.getPlayer().sendMessage(main.prefix + ChatColor.RED + "You are muted, your mute expires in " + ChatColor.BLUE + timeLeft + ChatColor.RED + ".");
                }
            } else {
                e.getPlayer().sendMessage(main.prefix + "You're muted but we can't find any information about it, please contact a staff on discord.");
            }
        }
    }

}
