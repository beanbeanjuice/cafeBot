package com.beanbeanjuice.cafebot.utility.listeners;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomRoleType;
import com.beanbeanjuice.cafebot.api.wrapper.api.exception.ApiRequestException;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CountingListener extends ListenerAdapter {

    private final CafeBot cafeBot;

    public CountingListener(final CafeBot cafeBot) {
        this.cafeBot = cafeBot;
    }

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        if (!event.isFromGuild() || event.getAuthor().isBot()) return;

        String guildId = event.getGuild().getId();
        String channelId = event.getChannel().getId();

        cafeBot.getCafeAPI().getCustomChannelApi().getCustomChannel(guildId, CustomChannelType.COUNTING)
                .thenAccept((customChannel) -> {
                    if (customChannel == null || !customChannel.getChannelId().equals(channelId)) return;

                    handleCountingEvent(event);
                });
    }

    private void handleCountingEvent(MessageReceivedEvent event) {
        String number = event.getMessage().getContentRaw().split(" ")[0];
        if (!Helper.isNumber(number)) return;

        checkNumber(event, Integer.parseInt(number));
    }

    private void checkNumber(MessageReceivedEvent event, int number) {
        String guildId = event.getGuild().getId();
        String userId = event.getAuthor().getId();

        cafeBot.getCafeAPI().getCountingApi().updateCountingStatistics(guildId, userId, number)
                .thenAccept((statistics) -> {
                    boolean newBest = (number >= statistics.getHighestCount());
                    event.getMessage().addReaction(Emoji.fromFormatted("✅")).queue();
                    if (newBest) event.getMessage().addReaction(Emoji.fromFormatted("✨")).queue();
                })
                .exceptionally((ex) -> {
                    if (ex.getCause() instanceof ApiRequestException) {
                        handleFailure(event, guildId);

                        return null;
                    }

                    return null;
                });

    }

    private void handleFailure(MessageReceivedEvent event, String guildId) {
        cafeBot.getCafeAPI().getCountingApi().getCountingStatistics(guildId)
                .thenAccept((statistics) -> {
                    event.getMessage().addReaction(Emoji.fromFormatted("<:you_are_embarassing:1081417389532528660>")).queue();
                    event.getMessage().addReaction(Emoji.fromFormatted("❌")).queue();
                    event.getMessage().replyEmbeds(failedEmbed(
                            event.getMember(), statistics.getCurrentCount(), statistics.getHighestCount()
                    )).queue();
                });

        cafeBot.getCafeAPI().getCustomRoleApi().getCustomRole(guildId, CustomRoleType.COUNTING_FAILURE)
                .thenAccept((customRole) -> {
                    Role role = event.getGuild().getRoleById(customRole.getRoleId());
                    if (role == null) return;
                    event.getGuild().addRoleToMember(event.getAuthor(), role).queue();
                });
    }

    private MessageEmbed failedEmbed(Member member, final int lastNumber, final int highestNumber) {
        return Helper.errorEmbed(
                "Counting Failed",
                String.format(
                        """
                        <:cafeBot_angry:1171726164092518441> Counting failed due to %s at **%d**. The highest number achieved on this server was **%d**! \
                        Counting has now been reset to 0.

                        Remember, you can't count twice in a row and the numbers *must* increment by 1!
                        """, member.getAsMention(), lastNumber, highestNumber
                )
        );
    }

}
