package net.starly.discordchat;

import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.starly.core.bstats.Metrics;
import net.starly.discordchat.bot.DiscordBotManager;
import net.starly.discordchat.command.ReloadConfigCommand;
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

    private String getStr(String path) {
        return getConfig().getString(path);
    }

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
        if (getString("bot.webhook.url") == null) System.out.println("§e웹훅 URL을 필수로 입력해주세요!");
        else { this.bot = new DiscordBotManager(this); sendOnOffMessage("on"); }

        /* COMMAND
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        getCommand("discordchatreload").setExecutor(new ReloadConfigCommand());

        /* LISTENER
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        getServer().getPluginManager().registerEvents(new AsyncPlayerChatListener(), this);
    }

    @Override
    public void onDisable() {

        if (getString("bot.token") != null) sendOnOffMessage("off");

        if (this.bot != null) {
            this.bot.shutdown();
        }
    }

    private void sendOnOffMessage(String name) {
        TextChannel ServerOnOffCH = DiscordBotManager.getJda().getTextChannelById(getConfig().getString("bot." + name + "Server.channelId"));

        if (ServerOnOffCH == null) return;

        ServerOnOffCH.sendMessage(getString("bot." + name + "Server.content")).queue();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(getString("bot." + name + "Server.Embed.title"));
        embed.setDescription(getString("bot." + name + "Server.Embed.content"));
        embed.setFooter(getString("bot." + name + "Server.Embed.footer"));
        embed.setColor(new Color(Integer.decode(getString("bot." + name + "Server.Embed.color"))));

        ServerOnOffCH.sendMessageEmbeds(embed.build()).queue();
    }
}
