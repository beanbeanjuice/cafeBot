package com.beanbeanjuice.utility.poll;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

public class Poll {

    private String guildID;
    private String messageID;
    private Timestamp pollEndTime;

    public Poll(@NotNull String guildID, @NotNull String messageID, @NotNull Timestamp pollEndTime) {
        this.guildID = guildID;
        this.messageID = messageID;
        this.pollEndTime = pollEndTime;
    }

    @NotNull
    public String getGuildID() {
        return guildID;
    }

    @NotNull
    public String getMessageID() {
        return messageID;
    }

    @NotNull
    public Timestamp getPollEndTime() {
        return pollEndTime;
    }

}
