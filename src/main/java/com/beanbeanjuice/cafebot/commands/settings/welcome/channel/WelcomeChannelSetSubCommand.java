package com.beanbeanjuice.cafebot.commands.settings.welcome.channel;

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

public class WelcomeChannelSetSubCommand extends Command implements ISubCommand {

    public WelcomeChannelSetSubCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        Optional<OptionMapping> channelMapping = Optional.ofNullable(event.getOption("channel"));
        GuildChannelUnion channel = channelMapping.map(OptionMapping::getAsChannel).orElse((GuildChannelUnion) event.getChannel());

        cafeBot.getCafeAPI().getGuildsEndpoint().updateGuildInformation(event.getGuild().getId(), GuildInformationType.WELCOME_CHANNEL_ID, channel.getId())
                .thenAcceptAsync((ignored) -> {
                    event.getHook().sendMessageEmbeds(Helper.successEmbed(
                            "Welcome Channel Set",
                            String.format("The welcome channel has been set to %s.", channel.getAsMention())
                    )).queue();
                })
                .exceptionallyAsync((e) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            "Error Setting Welcome Channel",
                            String.format("There was an error setting the welcome channel: %s", e.getMessage())
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
        return "Set the welcome channel!";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.CHANNEL, "channel", "The channel to set the welcome channel to.", false)
        };
    }

}
