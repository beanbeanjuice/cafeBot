package com.beanbeanjuice.command.settings.raffle;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ISubCommand;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link ISubCommand} used to remove the {@link com.beanbeanjuice.utility.section.moderation.raffle.Raffle Raffle} {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}.
 *
 * @author beanbeanjuice
 */
public class RemoveRaffleChannelSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {

        // Attempt to remove the raffle channel from the database.
        if (GuildHandler.getCustomGuild(event.getGuild()).setRaffleChannel("0")) {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Removed Raffle Channel",
                    "The raffle channel has been successfully removed."
            )).queue();
            return;
        }
        event.getHook().sendMessageEmbeds(Helper.sqlServerError()).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Remove the raffle channel!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/raffle-channel remove`";
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
