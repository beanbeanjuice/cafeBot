package com.beanbeanjuice.cafebot.utility.listeners;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.counting.CountingInformation;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildInformationType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Optional;

public class CountingListener extends ListenerAdapter {

    private final CafeBot cafeBot;

    public CountingListener(final CafeBot cafeBot) {
        this.cafeBot = cafeBot;
    }

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        if (!event.isFromGuild()) return;

        String guildID = event.getGuild().getId();
        String channelID = event.getChannel().getId();

        cafeBot.getCafeAPI().getGuildsEndpoint().getGuildInformation(guildID)
                .thenComposeAsync((guildInformation) -> {
                    if (!guildInformation.getSetting(GuildInformationType.COUNTING_CHANNEL_ID).equalsIgnoreCase(channelID)) return null;
                    return cafeBot.getCafeAPI().getCountingEndpoint().getGuildCountingInformation(guildID);
                })
                .thenAcceptAsync((countingInformation) -> {
                    handleCountingEvent(event, countingInformation);
                });
    }

    private void handleCountingEvent(final MessageReceivedEvent event, final CountingInformation countingInformation) {
        String number = event.getMessage().getContentRaw().split(" ")[0];
        if (!Helper.isNumber(number)) return;

        checkNumber(event, Integer.parseInt(number), countingInformation);
    }

    private void checkNumber(final MessageReceivedEvent event, final int number, final CountingInformation countingInformation) {
        String guildID = event.getGuild().getId();
        int lastNumber = countingInformation.getLastNumber();
        int highestNumber = countingInformation.getHighestNumber();

        if (number != lastNumber + 1 || event.getAuthor().getId().equals(countingInformation.getLastUserID())) {
            cafeBot.getCafeAPI().getCountingEndpoint().updateGuildCountingInformation(
                    guildID, highestNumber, 0, "0", countingInformation.getFailureRoleID()
            ).thenAcceptAsync((ignored) -> {
                event.getMessage().addReaction(Emoji.fromFormatted("<:you_are_embarassing:1081417389532528660>")).queue();
                event.getMessage().addReaction(Emoji.fromFormatted("❌")).queue();
                event.getMessage().replyEmbeds(failedEmbed(
                        event.getMember(), countingInformation.getLastNumber(), countingInformation.getHighestNumber()
                )).queue();
            });

            Optional<Role> roleOptional = Optional.ofNullable(event.getGuild().getRoleById(countingInformation.getFailureRoleID()));
            roleOptional.ifPresent((role) -> event.getGuild().addRoleToMember(event.getAuthor(), role).queue());

            return;
        }

        lastNumber++;
        boolean newBest = (lastNumber > highestNumber);
        if (lastNumber > highestNumber) highestNumber = lastNumber;

        cafeBot.getCafeAPI().getCountingEndpoint()
                .updateGuildCountingInformation(guildID, highestNumber, lastNumber, event.getAuthor().getId(), countingInformation.getFailureRoleID())
                .thenAcceptAsync((ignored) -> {
                    event.getMessage().addReaction(Emoji.fromFormatted("✅")).queue();

                    if (newBest) event.getMessage().addReaction(Emoji.fromFormatted("✨")).queue();
                });
    }

    private MessageEmbed failedEmbed(Member member, final int lastNumber, final int highestNumber) {
        return Helper.errorEmbed(
                "Counting Failed",
                String.format(
                        """
                        Counting failed due to %s at **%d**. The highest number achieved on this server was **%d**! \
                        Counting has now been reset to 0.
                        
                        Remember, you can't count twice in a row and the numbers *must* increment by 1!
                        """, member.getAsMention(), lastNumber, highestNumber
                )
        );
    }

}
