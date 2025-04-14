package com.lucacando.impactMines.discord;

import com.lucacando.impactMines.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class DiscordChatListener implements Listener {

    Main main;

    public DiscordChatListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        main.jda.getGuildById("1361023373529976862").getTextChannelById("1361129104337801297").sendMessage(
                "**" + player.getName() + "**: " + event.getMessage()
        ).queue();
    }
}
