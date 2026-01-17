package com.beanbeanjuice.cafebot.utility.listeners.modals.polls;

import com.beanbeanjuice.cafebot.api.wrapper.type.poll.Poll;
import com.beanbeanjuice.cafebot.api.wrapper.type.poll.PollOption;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PollListener extends ListenerAdapter {

    private final CafeBot bot;

    public PollListener(CafeBot bot) {
        this.bot = bot;
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getUser() != null && event.getUser().isBot()) return;

        String guildId = event.getGuild().getId();
        String messageId = event.getMessageId();
        String emoji = event.getEmoji().getFormatted();
        String userId = event.getUserId();

        bot.getCafeAPI().getPollApi().getPoll(guildId, messageId).thenAccept((poll) -> {
            for (PollOption option : poll.getOptions()) {
                if (!option.getEmoji().equals(emoji)) continue;

                bot.getCafeAPI().getPollApi().setVote(poll.getId(), option.getId(), userId, true).thenAccept((newPoll) -> {
                    if (!poll.isAllowMultiple()) handleIllegalMultiple(event, poll, option);

                    Helper.pmUser(event.getUser(), Helper.successEmbed(
                            String.format("Poll #%d (%s)", poll.getId(), poll.getTitle()),
                            String.format("You submitted your vote for %s **%s** - *%s*.", option.getEmoji(), option.getTitle(), option.getDescription().orElse("No Description"))
                    ));
                });
            }
        });
    }

    private void handleIllegalMultiple(MessageReactionAddEvent event, Poll oldPoll, PollOption option) {
        if (oldPoll.isAllowMultiple()) return;  // ignore if multiples ARE allowed.

        List<String> emojisToRemove = new ArrayList<>();
        String userId = event.getUserId();

        for (PollOption potentialOption : oldPoll.getOptions()) {
            if (potentialOption.getId() == option.getId()) continue;  // Do not remove old option.
            if (!Arrays.asList(potentialOption.getVoters()).contains(userId)) continue;

            emojisToRemove.add(potentialOption.getEmoji().get());
            break;
        }

        event.retrieveMessage().queue((message) -> {
            emojisToRemove.forEach((emoji) -> message.removeReaction(Emoji.fromUnicode(emoji), event.retrieveUser().complete()).queue());
        });
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getUser() != null && event.getUser().isBot()) return;

        String guildId = event.getGuild().getId();
        String messageId = event.getMessageId();
        String emoji = event.getEmoji().getFormatted();
        String userId = event.getUserId();

        // Do nothing if not a poll.
        bot.getCafeAPI().getPollApi().getPoll(guildId, messageId).thenAccept((poll) -> {
            for (PollOption option : poll.getOptions()) {
                if (!option.getEmoji().equals(emoji)) continue;

                bot.getCafeAPI().getPollApi().setVote(poll.getId(), option.getId(), userId, false).thenRun(() -> {
                    Helper.pmUser(event.getUser(), Helper.errorEmbed(
                            String.format("Poll #%d (%s)", poll.getId(), poll.getTitle()),
                            String.format("You removed your vote for %s **%s** - *%s*.", option.getEmoji(), option.getTitle(), option.getDescription().orElse("No Description"))
                    ));
                });
            }
        });
    }

}
