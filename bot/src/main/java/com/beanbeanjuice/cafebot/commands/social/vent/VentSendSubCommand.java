package com.beanbeanjuice.cafebot.commands.social.vent;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildInformationType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.time.Instant;
import java.util.Optional;

public class VentSendSubCommand extends Command implements ISubCommand {

    public VentSendSubCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        String guildID = event.getGuild().getId();
        String message = event.getOption("message").getAsString();

        cafeBot.getCafeAPI().getGuildsEndpoint()
                .getGuildInformation(guildID)
                .thenAcceptAsync((guildInformation) -> {
                    String ventingChannelID = guildInformation.getSetting(GuildInformationType.VENTING_CHANNEL_ID);
                    Optional<TextChannel> channelOptional = Optional.ofNullable(event.getGuild().getChannelById(TextChannel.class, ventingChannelID));

                    channelOptional.ifPresentOrElse(
                            (channel) -> sendVent(message, channel, event),
                            () -> sendFailure(event)
                    );
                });
    }

    private void sendVent(final String message, final TextChannel channel, final SlashCommandInteractionEvent event) {
        channel.sendMessageEmbeds(getVentEmbed(message)).queue();

        event.getHook().sendMessageEmbeds(
                Helper.successEmbed(
                        "Vent Sent",
                        String.format("Your anonymous vent has been successfully sent to %s.", channel.getAsMention())
                )
        ).queue();
    }

    private MessageEmbed getVentEmbed(final String message) {
        return new EmbedBuilder()
                .setTitle("Anonymous Vent")
                .setColor(Helper.getRandomColor())
                .setDescription(message)
                .setTimestamp(Instant.now())
                .build();
    }

    private void sendFailure(final SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(
                Helper.errorEmbed(
                        "Venting Channel Error",
                        "This server may not have a venting channel set."
                )
        ).queue();
    }

    @Override
    public String getName() {
        return "send";
    }

    @Override
    public String getDescription() {
        return "Send an anonymous vent!";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "message", "The message you want to anonymously vent.", true)
        };
    }
}
