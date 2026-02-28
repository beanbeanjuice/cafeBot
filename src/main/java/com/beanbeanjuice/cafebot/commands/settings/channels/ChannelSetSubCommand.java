package com.beanbeanjuice.cafebot.commands.settings.channels;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Arrays;
import java.util.Optional;

public class ChannelSetSubCommand extends Command implements ISubCommand {

    public ChannelSetSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        CustomChannelType type = CustomChannelType.valueOf(event.getOption("type").getAsString());
        Optional<OptionMapping> channelMapping = Optional.ofNullable(event.getOption("channel"));
        GuildChannelUnion channel = channelMapping.map(OptionMapping::getAsChannel).orElse((GuildChannelUnion) event.getChannel());
        String guildId = event.getGuild().getId();
        String channelId = channel.getId();

        if (channel.getType() != ChannelType.TEXT) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Invalid Channel",
                    "I-... I'm sorry... we don't do substitutions here... please use a text channel."
            )).queue();
            return;
        }

        this.bot.getCafeAPI().getCustomChannelApi().setCustomChannel(guildId, type, channelId)
                .thenAccept((customChannel) -> {
                    event.getHook().sendMessageEmbeds(Helper.successEmbed(
                            String.format("%s Channel Set", type.getFriendlyName()),
                            String.format("Channel has been set to %s!\n\n%s", channel.getAsMention(), type.getDescription())
                    )).queue();
                })
                .exceptionally((ex) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            String.format("Error Setting %s Channel", type.getFriendlyName()),
                            "There was an error setting the channel... I'm so sorry.. 🥺"
                    )).queue();

                    this.bot.getLogger().log(this.getClass(), LogLevel.WARN, String.format("Error Setting %s Channel: %s", type.getFriendlyName(), ex.getMessage()), true, true);
                    return null;
                });
    }

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescriptionPath() {
        return "Set a custom channel!";
    }

    @Override
    public OptionData[] getOptions() {
        OptionData channelTypeData = new OptionData(OptionType.STRING, "type", "The channel type you want to set", true);

        Arrays.stream(CustomChannelType.values()).forEach((type) -> channelTypeData.addChoice(type.getFriendlyName(), type.name()));

        return new OptionData[] {
                channelTypeData,
                new OptionData(OptionType.CHANNEL, "channel", "The channel you want to set.", false).setChannelTypes(ChannelType.TEXT)
        };
    }

}
