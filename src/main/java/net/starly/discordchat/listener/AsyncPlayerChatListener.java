package net.starly.discordchat.listener;

import net.starly.discordchat.DiscordChat;
import net.starly.discordchat.bot.webhook.WebhookManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.IOException;

public class AsyncPlayerChatListener implements Listener {

    private WebhookManager webhook = DiscordChat.getWebhook();


    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        webhook.setUsername(e.getPlayer().getDisplayName());
        webhook.setAvatarUrl("https://mc-heads.net/head/" + e.getPlayer().getDisplayName());
        webhook.setContent(e.getMessage());
        try {
            webhook.execute();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}