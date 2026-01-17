package com.beanbeanjuice.cafebot.commands.settings.channels;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Arrays;

public class ChannelRemoveSubCommand extends Command implements ISubCommand {

    public ChannelRemoveSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        String guildId = event.getGuild().getId();
        CustomChannelType type = CustomChannelType.valueOf(event.getOption("type").getAsString());

        this.bot.getCafeAPI().getCustomChannelApi().deleteCustomChannel(guildId, type)
                .thenAccept((ignored) -> {
                    event.getHook().sendMessageEmbeds(Helper.successEmbed(
                            String.format("%s Channel Removed", type.getFriendlyName()),
                            String.format("The %s channel has been successfully removed.", type.getFriendlyName())
                    )).queue();
                })
                .exceptionally((ex) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            String.format("Error Removing %s Channel", type.getFriendlyName()),
                            "There was a problem removing the channel 🥺 please try again later..."
                    )).queue();

                    this.bot.getLogger().log(this.getClass(), LogLevel.WARN, String.format("Error Removing %s Channel: %s", type.getFriendlyName(), ex.getMessage()));
                    return null;
                });
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "Remove a custom channel!";
    }

    @Override
    public OptionData[] getOptions() {
        OptionData channelTypeData = new OptionData(OptionType.STRING, "type", "The channel type you want to set", true);

        Arrays.stream(CustomChannelType.values()).forEach((type) -> channelTypeData.addChoice(type.getFriendlyName(), type.name()));

        return new OptionData[] {
                channelTypeData
        };
    }

}
