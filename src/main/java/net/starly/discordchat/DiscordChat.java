package net.starly.discordchat;

import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.starly.core.bstats.Metrics;
import net.starly.discordchat.bot.DiscordBotManager;
import net.starly.discordchat.context.MessageContent;
import net.starly.discordchat.listener.AsyncPlayerChatListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;

public class DiscordChat extends JavaPlugin {

    @Getter
    private static DiscordChat instance;
    @Getter
    private DiscordBotManager bot;
    public String getString(String name) { return getConfig().getString(name); }

    @Override
    public void onEnable() {
        /* SETUP
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        instance = this;
        new Metrics(this, 12345); // TODO: 수정

        /* CONFIG
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        saveDefaultConfig();
        MessageContent.getInstance().initialize(getConfig());
        this.bot = new DiscordBotManager(this);

        TextChannel ServerOnOffCH = DiscordBotManager.getJda().getTextChannelById(getConfig().getString("bot.onServer.channelId"));

        if (ServerOnOffCH == null) return;

        ServerOnOffCH.sendMessage(getString("bot.onServer.content")).queue();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(getString("bot.onServer.Embed.title"));
        embed.setDescription(getString("bot.onServer.Embed.content"));
        embed.setFooter(getString("bot.onServer.Embed.footer"));
        embed.setColor(new Color(Integer.decode(getString("bot.onServer.Embed.color"))));
        ServerOnOffCH.sendMessageEmbeds(embed.build()).queue();

        /* COMMAND
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        // TODO: 수정

        /* LISTENER
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        getServer().getPluginManager().registerEvents(new AsyncPlayerChatListener(), this);
    }

    @Override
    public void onDisable() {

        TextChannel ServerOnOffCH = DiscordBotManager.getJda().getTextChannelById(getConfig().getString("bot.offServer.channelID"));

        if (ServerOnOffCH == null) return;

        ServerOnOffCH.sendMessage(getString("bot.offServer.content")).queue();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(getString("bot.offServer.Embed.title"));
        embed.setDescription(getString("bot.offServer.Embed.content"));
        embed.setFooter(getString("bot.offServer.Embed.footer"));
        embed.setColor(new Color(Integer.decode(getString("bot.offServer.Embed.color"))));

        if (this.bot != null) {
            this.bot.shutdown();
        }
    }

    private void sendMessage() {
        // TODO 하세요.
    }
}
