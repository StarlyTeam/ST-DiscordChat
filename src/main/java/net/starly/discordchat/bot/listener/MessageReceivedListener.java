package net.starly.discordchat.bot.listener;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.starly.discordchat.context.MessageContent;
import net.starly.discordchat.context.MessageType;
import org.bukkit.plugin.java.JavaPlugin;

@AllArgsConstructor
public class MessageReceivedListener extends ListenerAdapter {

    private final JavaPlugin plugin;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMember() == null) return;
        if (event.getAuthor().isBot()) return;

        User user = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();

        MessageContent config = MessageContent.getInstance();

        if (channel.getId().equals(config.getMessage(MessageType.BOT, "channel.chat"))) {
            config.getMessage(MessageType.BOT, "discordMessage").ifPresent(msg -> {
                String replacedMessage = msg.replace("{user}", user.getName())
                                            .replace("{message}", message.getContentRaw());
                plugin.getServer().broadcastMessage(replacedMessage);
            });
        }
    }
}
