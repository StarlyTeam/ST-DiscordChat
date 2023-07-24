package net.starly.discordchat.bot;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.starly.discordchat.bot.listener.MessageReceivedListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class DiscordBotManager {

    @Getter
    private static JDA jda;
    private final FileConfiguration config;
    private final Logger log;

    public DiscordBotManager(JavaPlugin plugin) {
        this.config = plugin.getConfig();
        this.log = plugin.getLogger();

        try {
            if (config.getString("bot.token") == null) { log.severe("토큰이 존재하지 않습니다."); return; }
            JDABuilder builder = JDABuilder.createDefault(config.getString("bot.token"));
            jda = builder.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .enableCache(CacheFlag.MEMBER_OVERRIDES)
                    .setEnableShutdownHook(false)
                    .setActivity(parseActivity())
                    .setStatus(parseOnlineStatus())
                    .addEventListeners(new MessageReceivedListener())
                    .build();
            jda.awaitReady();

        } catch (Exception e) {
            e.printStackTrace();
            log.severe("봇 로딩중에 오류가 발생하였습니다. (관리자에게 문의해주세요.)");
        }
        loadSlashCommands();
    }

    private Activity parseActivity() {
        Activity.ActivityType activity = Activity.ActivityType.valueOf(config.getString("bot.presence.type").toUpperCase());
        return Activity.of(activity, config.getString("bot.presence.text"));
    }

    private OnlineStatus parseOnlineStatus() {
        return OnlineStatus.valueOf(config.getString("bot.status").toUpperCase());
    }

    private void loadSlashCommands() {
        log.info("(/) 명령어를 업데이트합니다.");
        jda.updateCommands().addCommands(
                Commands.slash("후원안내", "후원안내 메시지를 생성합니다."),
                Commands.slash("플랜안내", "플랜안내 메시지를 생성합니다.")
        ).queue();
    }

    public void shutdown() {
        try {
            jda.shutdown();
        } catch (NoClassDefFoundError ignored) {
        }
    }
}