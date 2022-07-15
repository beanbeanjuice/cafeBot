package com.beanbeanjuice.command.settings.birthday;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ISubCommand;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link ISubCommand} used to remove the birthday {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}.
 *
 * @author beanbeanjuice
 * @since v3.0.1
 */
public class RemoveBirthdayChannelSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        if (GuildHandler.getCustomGuild(event.getGuild()).setBirthdayChannelID("0")) {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Removed Birthday Channel",
                    "The current birthday channel has been removed."
            )).queue();
            return;
        }
        event.getHook().sendMessageEmbeds(Helper.sqlServerError()).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Remove the birthday channel from the server!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/birthday-channel remove`";
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
