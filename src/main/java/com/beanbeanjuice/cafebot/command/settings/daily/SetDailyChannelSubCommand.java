package com.beanbeanjuice.cafebot.command.settings.daily;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ISubCommand;
import com.beanbeanjuice.cafebot.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ISubCommand} used to set the daily {@link TextChannel TextChannel}
 * for the specified {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class SetDailyChannelSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {

        // Checking if a text channel.
        if (!Helper.isTextChannel(event.getChannel())) {
            event.getHook().sendMessageEmbeds(Helper.notATextChannelEmbed(event.getChannelType())).queue();
            return;
        }

        TextChannel channel = event.getChannel().asTextChannel();

        if (event.getOption("daily_channel") != null)
            channel = event.getOption("daily_channel").getAsChannel().asTextChannel();

        if (GuildHandler.getCustomGuild(event.getGuild()).isCustomChannel(channel.getId())) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed("Channel Already Set",
                    "This current channel is already set to something. For a list of channels you are already using with this bot, do `/" +
                            "list-custom-channels`")).queue();
            return;
        }

        if (GuildHandler.getCustomGuild(event.getGuild()).setDailyChannelID(event.getChannel().getId())) {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Updated Daily Channel",
                    "Successfully set the daily channel to this channel. " +
                            "To remove the daily channel, just delete the channel. Just a reminder, " +
                            "any integrations you have with this channel will be deleted upon the copy of this channel."
            )).queue();

            return;
        }
        event.getHook().sendMessageEmbeds(Helper.sqlServerError()).queue();
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.CHANNEL, "daily_channel", "The text channel to reset daily.", false)
                .setChannelTypes(ChannelType.TEXT));
        return options;
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Set the current channel to the daily channel!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/daily-channel set`";
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.SETTINGS;
    }

    @NotNull
    @Override
    public String getName() {
        return "set";
    }

}
