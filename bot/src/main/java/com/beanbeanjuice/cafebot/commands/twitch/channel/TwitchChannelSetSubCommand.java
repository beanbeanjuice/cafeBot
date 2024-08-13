package com.beanbeanjuice.cafebot.commands.twitch.channel;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildInformationType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Optional;

public class TwitchChannelSetSubCommand extends Command implements ISubCommand {

    public TwitchChannelSetSubCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        Optional<OptionMapping> channelMapping = Optional.ofNullable(event.getOption("channel"));

        String channelID = channelMapping
                .map(OptionMapping::getAsChannel)
                .map(GuildChannelUnion::asTextChannel)
                .map(TextChannel::getId)
                .orElse(event.getChannel().getId());

        cafeBot.getCafeAPI().getGuildsEndpoint()
                .updateGuildInformation(event.getGuild().getId(), GuildInformationType.TWITCH_CHANNEL_ID, channelID)
                .thenAcceptAsync((ignored) -> {
                    event.getHook().sendMessageEmbeds(Helper.successEmbed(
                            "Twitch Notifications Channel Set",
                            String.format("The live notifications channel has been successfully set to %s!", event.getGuild().getChannelById(TextChannel.class, channelID).getAsMention())
                    )).queue();
                })
                .exceptionallyAsync((e) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            "Error Setting Twitch Notifications Channel",
                            String.format("There was an error setting the twitch notifications channel: %s", e.getMessage())
                    )).queue();
                    return null;
                });
    }

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescription() {
        return "Set the twitch notifications channel.";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.CHANNEL, "channel", "The channel to set the twitch channel to.", false)
        };
    }

}
