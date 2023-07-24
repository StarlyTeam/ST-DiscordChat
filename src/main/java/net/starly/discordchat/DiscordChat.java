package net.starly.discordchat;

import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.starly.core.bstats.Metrics;
import net.starly.discordchat.bot.DiscordBotManager;
import net.starly.discordchat.listener.AsyncPlayerChatListener;
import net.starly.discordchat.bot.webhook.WebhookManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;

public class DiscordChat extends JavaPlugin {

    @Getter
    private static DiscordChat instance;
    @Getter
    private DiscordBotManager bot;
    @Getter
    private static WebhookManager webhook;


    public String getString(String path) {
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
        this.bot = new DiscordBotManager(this);

        try {
            webhook = new WebhookManager(getConfig().getString("webhook.url"));
        } catch (Exception e) {
            Bukkit.getLogger().severe("제대로 된 웹훅 URl을 입력해주세요!");
        }

        TextChannel ServerOnOffCH = null;

        try {
            ServerOnOffCH = DiscordBotManager.getJda().getTextChannelById(getConfig().getString("bot.onServer.channelID"));
        } catch (Exception e) {
            System.out.println("제대로 된 채널ID를 입력해주세요.");
        }

        if (ServerOnOffCH == null) return;

        if (getString("bot.onServer.content") != null) {
            System.out.println(getString("bot.onServer.content"));
            ServerOnOffCH.sendMessage(getString("bot.onServer.content")).queue();
        }

        if (getString("bot.onServer.Embed.content") != null) {
            EmbedBuilder embed = new EmbedBuilder();
            if (getString("bot.onServer.Embed.title") != null)
                embed.setTitle(getString("bot.onServer.Embed.title"));
            embed.setDescription(getString("bot.onServer.Embed.content"));
            if (getString("bot.onServer.Embed.footer") != null)
                embed.setFooter(getString("bot.onServer.Embed.footer"));
            if (getString("bot.onServer.Embed.color") != null) {
                embed.setColor(new Color(Integer.decode(getString("bot.onServer.Embed.color"))));
            }
            ServerOnOffCH.sendMessageEmbeds(embed.build()).queue();
        }


        /* COMMAND
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        // TODO: 수정

        /* LISTENER
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        Bukkit.getPluginManager().registerEvents(new AsyncPlayerChatListener(), this);
    }

    @Override
    public void onDisable() {
        TextChannel ServerOnOffCH = null;

        try {
            ServerOnOffCH = DiscordBotManager.getJda().getTextChannelById(getConfig().getString("bot.offServer.channelID"));
        } catch (Exception e) {
            System.out.println("제대로 된 채널ID를 입력해주세요.");
        }

        if (ServerOnOffCH == null) return;

        try {
            if (getString("bot.onServer.content") != null) {
                System.out.println(getString("bot.offServer.content"));
                ServerOnOffCH.sendMessage(getString("bot.offServer.content")).queue();
            }

            if (getString("bot.offServer.Embed.content") != null) {
                EmbedBuilder embed = new EmbedBuilder();
                if (getString("bot.offServer.Embed.title") != null)
                    embed.setTitle(getString("bot.offServer.Embed.title"));
                embed.setDescription(getString("bot.offServer.Embed.content"));
                if (getString("bot.offServer.Embed.footer") != null)
                    embed.setFooter(getString("bot.offServer.Embed.footer"));
                if (getString("bot.offServer.Embed.color") != null) {
                    embed.setColor(new Color(Integer.decode(getString("bot.offServer.Embed.color"))));
                }
                ServerOnOffCH.sendMessageEmbeds(embed.build()).queue();
            }
        } catch (Exception e) {
            System.out.println("§e알 수 없는 오류가 발생했습니다. 관리자에게 문의해주세요.");
        }

        if (this.bot != null) {
            this.bot.shutdown();
        }
    }
}
