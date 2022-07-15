package com.beanbeanjuice.command.settings.logging;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ISubCommand;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link ISubCommand} used to remove the log {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}
 * from a specified {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class RemoveLogChannelSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        if (GuildHandler.getCustomGuild(event.getGuild()).setLogChannelID("0")) {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Removed Log Channel",
                    "The log channel has been successfully removed."
            )).queue();
            return;
        }
        event.getHook().sendMessageEmbeds(Helper.sqlServerError()).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Remove the log channel!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/log-channel remove`";
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.SETTINGS;
    }

    @NotNull
    @Override
    public String getName() {
        return "remove";
    }

}
