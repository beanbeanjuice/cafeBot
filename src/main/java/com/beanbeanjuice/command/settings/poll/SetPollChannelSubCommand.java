package com.beanbeanjuice.command.settings.poll;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ISubCommand;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.section.moderation.poll.Poll;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link ISubCommand} used to set the {@link Poll Poll} {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}.
 *
 * @author beanbeanjuice
 */
public class SetPollChannelSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {

        // Makes sure the poll channel has been added to the database.
        if (GuildHandler.getCustomGuild(event.getGuild()).setPollChannel(event.getChannel().getId())) {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Set Poll Channel",
                    "The current channel has been set to a `poll` channel!"
            )).queue();
            return;
        }

        event.getHook().sendMessageEmbeds(Helper.sqlServerError()).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Set the current channel to poll channel!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/poll-channel set #general` or `/poll-channel set`";
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.SETTINGS;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return false;
    }

    @NotNull
    @Override
    public String getName() {
        return "set";
    }

}
