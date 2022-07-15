package com.beanbeanjuice.command.settings.counting;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ISubCommand;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.cafeapi.exception.api.AuthorizationException;
import com.beanbeanjuice.cafeapi.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.exception.api.ResponseException;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link ISubCommand} used to set the counting {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}
 * for a specified {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class SetCountingChannelSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {

        // Attempt to add the counting channel to the database.
        if (GuildHandler.getCustomGuild(event.getGuild()).setCountingChannel(event.getChannel().getId())) {

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
