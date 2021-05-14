package com.beanbeanjuice.utility.raffle;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.helper.timestamp.TimestampDifference;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

/**
 * A custom {@link Raffle} class.
 *
 * @author beanbeanjuice
 */
public class Raffle {

    private String guildID;
    private String messageID;
    private Timestamp raffleEndTime;
    private Integer winnerAmount;

    /**
     * Creates a new {@link Raffle} object.
     * @param guildID The ID of the {@link net.dv8tion.jda.api.entities.Guild Guild} containing the {@link Raffle}.
     * @param messageID The ID of the {@link net.dv8tion.jda.api.entities.Message Message} related to the {@link Raffle}.
     * @param raffleEndTime The {@link Timestamp} of when the {@link Raffle} ends.
     * @param winnerAmount The {@link Integer} amount of people who can win the raffle.
     */
    public Raffle(@NotNull String guildID, @NotNull String messageID, @NotNull Timestamp raffleEndTime, @NotNull Integer winnerAmount) {
        this.guildID = guildID;
        this.messageID = messageID;
        this.raffleEndTime = raffleEndTime;
        this.winnerAmount = winnerAmount;
    }

    /**
     * @return The ID of the {@link net.dv8tion.jda.api.entities.Guild Guild} for the {@link Raffle}.
     */
    @NotNull
    public String getGuildID() {
        return guildID;
    }

    /**
     * @return The ID of the {@link net.dv8tion.jda.api.entities.Message Message} for the {@link Raffle}.
     */
    @NotNull
    public String getMessageID() {
        return messageID;
    }

    /**
     * @return The ID of the {@link Timestamp} for when the {@link Raffle} will end.
     */
    @NotNull
    public Timestamp getRaffleEndTime() {
        return raffleEndTime;
    }

    /**
     * @return The amount of people who can win the {@link Raffle}.
     */
    @NotNull
    public Integer getWinnerAmount() {
        return winnerAmount;
    }

    /**
     * @return Whether or not the {@link Raffle} should calculate a winner.
     */
    @NotNull
    public Boolean isFinished() {
        Long timeBetween = BeanBot.getGeneralHelper().compareTwoTimeStamps(raffleEndTime, new Timestamp(System.currentTimeMillis()), TimestampDifference.MINUTES);
        return timeBetween > 0;
    }

}
