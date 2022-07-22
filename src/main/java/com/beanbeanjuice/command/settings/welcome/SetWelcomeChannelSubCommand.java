package com.beanbeanjuice.command.settings.welcome;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ISubCommand;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ISubCommand} used to set the welcome {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}
 * for a specified {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class SetWelcomeChannelSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {

        TextChannel channel = event.getTextChannel();
        if (event.getOption("welcome_channel") != null)
            channel = event.getOption("welcome_channel").getAsTextChannel();

        // If the channel is already set, notify them that this cannot be done.
        if (GuildHandler.getCustomGuild(event.getGuild()).isDailyChannel(channel.getId())) {
            event.getHook().sendMessageEmbeds(Helper.alreadyDailyChannel()).queue();
            return;
        }

        if (GuildHandler.getCustomGuild(event.getGuild()).setWelcomeChannelID(channel.getId())) {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Set Welcome Channel",
                    "This channel has been set to the welcome channel! Make sure to " +
                            "edit the welcome message with the `/welcome-channel edit-message` command!"
            )).queue();
            return;
        }
        event.getHook().sendMessageEmbeds(Helper.sqlServerError()).queue();
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.CHANNEL, "welcome_channel", "The text channel to send welcome information to.", false)
                .setChannelTypes(ChannelType.TEXT));
        return options;
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Set a channel to welcome channel!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/welcome-channel set`";
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
