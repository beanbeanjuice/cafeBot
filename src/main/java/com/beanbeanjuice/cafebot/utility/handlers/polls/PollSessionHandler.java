package com.beanbeanjuice.cafebot.utility.handlers.polls;

import com.beanbeanjuice.cafebot.api.wrapper.type.poll.PartialPoll;
import com.beanbeanjuice.cafebot.api.wrapper.type.poll.Poll;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PollSessionHandler {

    private final CafeBot bot;
    private final Map<String, Map<String, PollCreationSessionData>> sessions;

    public PollSessionHandler(CafeBot bot) {
        this.bot = bot;
        this.sessions = new HashMap<>();
    }

    public void createSession(String guildId, String userId, int durationInMinutes) {
        sessions.putIfAbsent(userId, new HashMap<>());
        sessions.get(userId).put(guildId, new PollCreationSessionData(guildId, userId, durationInMinutes));
    }

    public void addInitialData(String guildId, String userId, String title, @Nullable String description, boolean allowMultiple) {
        sessions.get(userId).get(guildId).addInitialData(title, description, allowMultiple);
    }

    public void addOption(String guildId, String userId, @Nullable String emoji, String title, @Nullable String description) {
        sessions.get(userId).get(guildId).addOption(emoji, title, description);
    }

    public PartialPoll getPollAndCloseSession(String guildId, String userId) {
        PartialPoll poll = this.sessions.get(userId).get(guildId).build();
        this.closeSession(guildId, userId);
        return poll;
    }

    public PartialPoll getPoll(String guildId, String userId) {
        return this.sessions.get(userId).get(guildId).build();
    }

    public void closeSession(String guildId, String userId) {
        this.sessions.get(userId).remove(guildId);
    }

    public static MessageEmbed getPollEmbed(Poll poll) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(poll.getTitle());
        embedBuilder.setTimestamp(poll.getEndsAt());

        StringBuilder descriptionBuilder = new StringBuilder();
        poll.getDescription().ifPresent((description) -> {
            descriptionBuilder.append(description);
            descriptionBuilder.append("\n\n");
        });

        descriptionBuilder.append(Arrays.stream(poll.getOptions()).map((pollOption) -> {
            String emoji = pollOption.getEmoji().get(); // Will never be null. Safe to use get
            String title = pollOption.getTitle();

            if (pollOption.getDescription().isPresent()) {
                return String.format("%s **%s** - *%s*", emoji, title, pollOption.getDescription().get());
            }

            return String.format("%s **%s**", emoji, title);
        }).collect(Collectors.joining("\n")));

        if (poll.isAllowMultiple()) descriptionBuilder.append("\n").append("*(Multiple Votes Allowed)*");

        embedBuilder.setDescription(descriptionBuilder.toString());
        embedBuilder.setFooter(String.format("Poll #%d", poll.getId()));
        embedBuilder.setColor(Helper.getRandomColor());

        return embedBuilder.build();
    }

    public MessageEmbed getPartialPollEmbed(String guildId, String userId) {
        PartialPoll poll = this.sessions.get(userId).get(guildId).build();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(poll.getTitle());
        embedBuilder.setTimestamp(poll.getEndsAt());

        StringBuilder descriptionBuilder = new StringBuilder();
        poll.getDescription().ifPresent((description) -> {
            descriptionBuilder.append(description);
            descriptionBuilder.append("\n\n");
        });

        descriptionBuilder.append(Arrays.stream(poll.getOptions()).map((pollOption) -> {
            String emoji = pollOption.getEmoji().orElse("?");
            String title = pollOption.getTitle();

            if (pollOption.getDescription().isPresent()) {
                return String.format("%s **%s** - *%s*", emoji, title, pollOption.getDescription().get());
            }

            return String.format("%s **%s**", emoji, title);
        }).collect(Collectors.joining("\n")));

        if (poll.isAllowMultiple()) descriptionBuilder.append("\n").append("*(Multiple Votes Allowed)*");

        embedBuilder.setDescription(descriptionBuilder.toString());
        embedBuilder.setFooter("Poll In Progress");
        embedBuilder.setColor(Helper.getRandomColor());

        return embedBuilder.build();
    }

}
