package net.starly.discordchat.context;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageType {

    NORMAL("messages"),
    ERROR("errorMessages"),
    BOT("bot");

    private final String key;
}