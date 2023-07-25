package net.starly.discordchat.command;

import net.starly.discordchat.DiscordChat;
import net.starly.discordchat.context.MessageContent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class ReloadConfigCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.isOp()) {
            Plugin plugin = DiscordChat.getInstance();
            plugin.reloadConfig();
            MessageContent.getInstance().initialize(plugin.getConfig());
            System.out.println("§a콘피그 파일이 리로드 되었습니다.");
            return true;
        }
        return false;
    }
}
