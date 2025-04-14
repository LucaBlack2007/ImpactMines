package com.lucacando.impactMines.discord;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class DiscordListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getChannel().getId().equals("1361129104337801297")
                && !e.getAuthor().isBot()) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                    "&8[&5DISCORD&8] " + e.getAuthor().getName() + "&f: " + e.getMessage().getContentDisplay()));
        }
    }



}