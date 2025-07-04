package com.beanbeanjuice.cafebot.commands.settings.update.channel;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildInformationType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Optional;

public class UpdateChannelSetSubCommand extends Command implements ISubCommand {

    public UpdateChannelSetSubCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        Optional<OptionMapping> channelMapping = Optional.ofNullable(event.getOption("channel"));

        GuildChannelUnion channel = channelMapping.map(OptionMapping::getAsChannel).orElse((GuildChannelUnion) event.getChannel());

        cafeBot.getCafeAPI().getGuildsEndpoint().updateGuildInformation(event.getGuild().getId(), GuildInformationType.UPDATE_CHANNEL_ID, channel.getId())
                .thenAcceptAsync((ignored) -> {
                    event.getHook().sendMessageEmbeds(Helper.successEmbed(
                            "Update Channel Set",
                            String.format("The update channel has been successfully set to %s. Don't forget to enable bot updates!", channel.getAsMention())
                    )).queue();
                })
                .exceptionallyAsync((e) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            "Error Setting Update Channel",
                            String.format("There was an error setting the update channel: %s", e.getMessage())
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
        return "Set the bot update channel!";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.CHANNEL, "channel", "The channel you want to set the update channel to.", false)
        };
    }

}
