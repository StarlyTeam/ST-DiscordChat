package net.starly.discordchat.listener;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.starly.discordchat.context.MessageContent;
import net.starly.discordchat.context.MessageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class AsyncPlayerChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        MessageContent config = MessageContent.getInstance();
        String url = config.getMessage(MessageType.BOT, "webhookUrl").orElse(null);
        WebhookClient client = new WebhookClientBuilder(url).build();

        Player player = event.getPlayer();
        String displayName = player.getDisplayName();
        UUID uniqueId = player.getUniqueId();
        String message = event.getMessage();

        WebhookMessageBuilder builder = new WebhookMessageBuilder()
                .setUsername(displayName)
                .setAvatarUrl("https://crafatar.com/renders/head/" + uniqueId)
                .setContent(message);
        client.send(builder.build());
    }
}