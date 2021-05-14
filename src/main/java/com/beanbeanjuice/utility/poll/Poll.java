package com.beanbeanjuice.utility.poll;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.helper.timestamp.TimestampDifference;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

/**
 * A class used for {@link Poll} objects.
 *
 * @author beanbeanjuice
 */
public class Poll {

    private String guildID;
    private String messageID;
    private Timestamp pollEndTime;

    /**
     * Creates a new {@link Poll} object.
     * @param guildID The ID of the {@link net.dv8tion.jda.api.entities.Guild Guild} associated with the {@link Poll}.
     * @param messageID The ID of the {@link net.dv8tion.jda.api.entities.Message Message} associated with the {@link Poll}.
     * @param pollEndTime The ending {@link Timestamp} for the {@link Poll}.
     */
    public Poll(@NotNull String guildID, @NotNull String messageID, @NotNull Timestamp pollEndTime) {
        this.guildID = guildID;
        this.messageID = messageID;
        this.pollEndTime = pollEndTime;
    }

    /**
     * @return The ID of the {@link net.dv8tion.jda.api.entities.Guild Guild} associated with the {@link Poll}.
     */
    @NotNull
    public String getGuildID() {
        return guildID;
    }

    /**
     * @return The ID of the {@link net.dv8tion.jda.api.entities.Message} associated with the {@link Poll}.
     */
    @NotNull
    public String getMessageID() {
        return messageID;
    }

    /**
     * @return The ending {@link Timestamp} of the {@link Poll}.
     */
    @NotNull
    public Timestamp getPollEndTime() {
        return pollEndTime;
    }

    @NotNull
    public Boolean isFinished() {
        return BeanBot.getGeneralHelper().compareTwoTimeStamps(pollEndTime, new Timestamp(System.currentTimeMillis()), TimestampDifference.MINUTES) > 0;
    }

}
