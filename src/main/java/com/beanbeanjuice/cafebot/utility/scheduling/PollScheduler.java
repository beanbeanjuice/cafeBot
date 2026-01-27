package com.beanbeanjuice.cafebot.utility.scheduling;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.api.wrapper.type.poll.Poll;
import com.beanbeanjuice.cafebot.api.wrapper.type.poll.PollOption;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PollScheduler extends CustomScheduler {

    public PollScheduler(CafeBot bot) {
        super(bot, "Poll-Scheduler");
    }

    @Override
    protected void onStart() {
        // Every minute, get polls that should be done.
        bot.getLogger().log(this.getClass(), LogLevel.INFO, "Starting the poll scheduler", false, false);

        this.scheduler.scheduleAtFixedRate(() -> {
            try {
                bot.getLogger().log(PollScheduler.class, LogLevel.INFO, "Checking polls...", false, false);
                handlePolls();
            } catch (Exception e) {
                bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Checking Polls: " + e.getMessage());
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    private void handlePolls() {
        bot.getCafeAPI().getPollApi().getPolls(true, true).thenAccept((pollsMap) -> {
            for (var entry : pollsMap.entrySet()) {
                String guildId = entry.getKey();
                List<Poll> polls = entry.getValue();

                handleGuildPolls(guildId, polls);
            }
        });
    }

    private void handleGuildPolls(String guildId, List<Poll> polls) {
        Guild guild = bot.getShardManager().getGuildById(guildId);
        if (guild == null) return;

        bot.getCafeAPI().getCustomChannelApi().getCustomChannel(guildId, CustomChannelType.POLL).thenAccept((customCHannel) -> {
            TextChannel channel = guild.getTextChannelById(customCHannel.getChannelId());
            if (channel == null) throw new IllegalStateException("Poll Channel Not Set");

            for (Poll poll : polls) {
                Message message = channel.retrieveMessageById(poll.getMessageId()).complete();

                bot.getCafeAPI().getPollApi().closePoll(poll.getId()).thenAccept((finalPoll) -> {
                    handlePollCompletion(message, finalPoll);
                });
            }
        }).exceptionally((ex) -> {
            bot.getLogger().logToGuild(guild, Helper.errorEmbed("Poll Channel Not Set", "Well... we can't have a poll if you don't even have the poll channel set!"));
            polls.forEach((poll) -> bot.getCafeAPI().getPollApi().deletePoll(poll.getId()));
            throw new CompletionException(ex);
        });
    }

    private void handlePollCompletion(Message message, Poll poll) {
        bot.getLogger().log(this.getClass(), LogLevel.INFO, String.format("Closing Poll: %s", poll.getId()), false, false);

        String winners = Arrays.stream(poll.getResults()).map((option) -> String.format("%s %s", option.getEmoji(), option.getTitle())).collect(Collectors.joining(", "));
        message.editMessage(String.format("<a:cafeBot:1119635469727191190> **Winners**: %s", winners)).setEmbeds(pollCompleteEmbed(poll)).queue();
    }

    private boolean optionIsWinner(PollOption option, Poll poll) {
        int optionId = option.getId();

        return Arrays.stream(poll.getResults()).map(PollOption::getId).anyMatch(id -> id == optionId);
    }

    private MessageEmbed pollCompleteEmbed(Poll poll) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(String.format("%s (Closed)", poll.getTitle()));
        embedBuilder.setTimestamp(poll.getEndsAt());

        StringBuilder descriptionBuilder = new StringBuilder();
        poll.getDescription().ifPresent((description) -> {
            descriptionBuilder.append(description);
            descriptionBuilder.append("\n\n");
        });

        int totalVotes = Arrays.stream(poll.getOptions()).map((option) -> option.getVoters().length).mapToInt(Integer::intValue).sum();

        descriptionBuilder.append(Arrays.stream(poll.getOptions()).map((pollOption) -> {
            boolean isWinner = optionIsWinner(pollOption, poll);

            String emoji = pollOption.getEmoji().get();
            String title = pollOption.getTitle();

            String optionString = isWinner ? "%s" : "||%s||";
            double percentageOfVotes = ((double) pollOption.getVoters().length / totalVotes) * 100;

            if (pollOption.getDescription().isPresent()) {
                String addition = String.format("%s (%.2f%%) **%s** - *%s*", emoji, percentageOfVotes, title, pollOption.getDescription().get());
                optionString = String.format(optionString, addition);
            } else {
                String addition = String.format("%s (%.2f%%) **%s**", emoji, percentageOfVotes, title);
                optionString = String.format(optionString, addition);
            }

            return optionString;
        }).collect(Collectors.joining("\n")));

        if (poll.isAllowMultiple()) descriptionBuilder.append("\n").append("*(Multiple Votes Allowed)*");

        embedBuilder.setDescription(descriptionBuilder.toString());
        embedBuilder.setFooter(String.format("Poll #%d", poll.getId()));
        embedBuilder.setColor(Color.RED);

        return embedBuilder.build();
    }

}
