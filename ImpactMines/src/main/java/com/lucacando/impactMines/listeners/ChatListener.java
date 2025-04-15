package com.lucacando.impactMines.listeners;

import com.lucacando.impactMines.Main;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;

public class ChatListener implements Listener {

    Main main;

    public ChatListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        ChatColor chatColor = ChatColor.GRAY;
        if (main.getPlayerRank(e.getPlayer()).getPermissionLevel() > 0) chatColor = ChatColor.WHITE;
        String message = e.getMessage();
        if (main.getPlayerRank(e.getPlayer()).getPermissionLevel() >= 3) message = ChatColor.translateAlternateColorCodes('&', e.getMessage());
        e.setFormat(main.formatPlayerName(e.getPlayer()) + chatColor + ": " + message);
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String msg = event.getMessage();
        if (msg.startsWith("/")) {
            String[] parts = msg.substring(1).split(" ");
            String label = parts[0].toLowerCase();
            if (!main.knownCommands.containsKey(label)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(main.prefix + "Can't find that command.");
            }
        }
    }

}
