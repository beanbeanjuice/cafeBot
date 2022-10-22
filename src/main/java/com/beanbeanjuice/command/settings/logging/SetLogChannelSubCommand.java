package com.beanbeanjuice.command.settings.logging;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ISubCommand;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ISubCommand} used to set the log {@link TextChannel TextChannel}
 * for the specified {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class SetLogChannelSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {

        // Checking if a text channel.
        if (!Helper.isTextChannel(event.getChannel())) {
            event.getHook().sendMessageEmbeds(Helper.notATextChannelEmbed(event.getChannelType())).queue();
            return;
        }

        TextChannel channel = event.getChannel().asTextChannel();
        if (event.getOption("log_channel") != null)
            channel = event.getOption("log_channel").getAsChannel().asTextChannel();

        // If the channel is already set, notify them that this cannot be done.
        if (GuildHandler.getCustomGuild(event.getGuild()).isDailyChannel(channel.getId())) {
            event.getHook().sendMessageEmbeds(Helper.alreadyDailyChannel()).queue();
            return;
        }

        if (GuildHandler.getCustomGuild(event.getGuild()).setLogChannelID(channel.getId())) {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Set Log Channel",
                    "The current channel has been set to a `log` channel!"
            )).queue();
            return;
        }

        event.getHook().sendMessageEmbeds(Helper.sqlServerError()).queue();
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.CHANNEL, "log_channel", "The text channel to send logging information to.", false)
                .setChannelTypes(ChannelType.TEXT));
        return options;
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Set the log channel to the current channel!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/log-channel set`";
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
