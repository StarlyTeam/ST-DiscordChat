package net.starly.discordchat.bot.listener;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.starly.discordchat.DiscordChat;
import net.starly.discordchat.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;

public class MessageReceivedListener extends ListenerAdapter {

    private Configuration config = DiscordChat.getInstance().getConfig();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMember() == null) return;
        if (event.getAuthor().isBot()) return;

        User user = event.getAuthor();
        Message message = event.getMessage();
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();

        if (channel.getId().equals(config.getString("bot.channel.chat"))) {
            Bukkit.getServer().broadcastMessage(ColorUtil.color(config.getString("bot.discordMessage")).replace("{user}", user.getName()) + content);
        }
    }
}
