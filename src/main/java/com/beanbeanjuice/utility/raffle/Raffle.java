package com.beanbeanjuice.utility.raffle;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.helper.timestamp.TimestampDifference;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

public class Raffle {

    private String guildID;
    private String messageID;
    private Timestamp raffleEndTime;
    private Integer winnerAmount;

    public Raffle(@NotNull String guildID, @NotNull String messageID, @NotNull Timestamp raffleEndTime, @NotNull Integer winnerAmount) {
        this.guildID = guildID;
        this.messageID = messageID;
        this.raffleEndTime = raffleEndTime;
        this.winnerAmount = winnerAmount;
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
    public Timestamp getRaffleEndTime() {
        return raffleEndTime;
    }

    @NotNull
    public Integer getWinnerAmount() {
        return winnerAmount;
    }

    @NotNull
    public Boolean isFinished() {
        Long timeBetween = BeanBot.getGeneralHelper().compareTwoTimeStamps(raffleEndTime, new Timestamp(System.currentTimeMillis()), TimestampDifference.MINUTES);
        return timeBetween > 0;
    }

}
