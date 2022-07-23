package com.beanbeanjuice.command.settings.counting;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ISubCommand;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.cafeapi.exception.api.AuthorizationException;
import com.beanbeanjuice.cafeapi.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.exception.api.ResponseException;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ISubCommand} used to set the counting {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}
 * for a specified {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class SetCountingChannelSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        TextChannel channel = event.getTextChannel();
        if (event.getOption("counting_channel") != null)
            channel = event.getOption("counting_channel").getAsTextChannel();

        // If the channel is already set, notify them that this cannot be done.
        if (GuildHandler.getCustomGuild(event.getGuild()).isDailyChannel(channel.getId())) {
            event.getHook().sendMessageEmbeds(Helper.alreadyDailyChannel()).queue();
            return;
        }

        // Attempt to add the counting channel to the database.
        if (GuildHandler.getCustomGuild(event.getGuild()).setCountingChannel(channel.getId())) {

            // Send a success embed if it worked.
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Updated Counting Channel",
                    "Successfully set the counting channel to this channel. " +
                            "To remove counting, just delete the channel."
            )).queue();

            // Now, try to create "Counting Information" in the database.
            try {
                Bot.getCafeAPI().COUNTING_INFORMATION.createGuildCountingInformation(event.getGuild().getId());
            }
            catch (ConflictException ignored) {}  // Ignore this.
            catch (AuthorizationException | ResponseException e) {
                event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                        "Error Creating Counting Information",
                        "There has been an error creating counting information. Please re-add the counting channel."
                )).queue();
            }
            return;
        }

        // Send if there was an error.
        event.getHook().sendMessageEmbeds(Helper.sqlServerError()).queue();
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.CHANNEL, "counting_channel", "The text channel set as a counting channel.", false)
                .setChannelTypes(ChannelType.TEXT));
        return options;
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Set the counting channel.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "/counting-channel set";
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
