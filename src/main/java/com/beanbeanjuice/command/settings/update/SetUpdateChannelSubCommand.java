package com.beanbeanjuice.command.settings.update;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ISubCommand;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link ISubCommand} used to set the current {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}
 * to the update {@link net.dv8tion.jda.api.entities.TextChannel TextChannel} in the
 * specified {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class SetUpdateChannelSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        if (GuildHandler.getCustomGuild(event.getGuild()).setUpdateChannel(event.getChannel().getId())) {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Set Update Channel",
                    "This channel will now receive bot updates! Make sure to enable notifications " +
                            "with the `/bot-update notify true` command!"
            )).queue();
            return;
        }
        event.getHook().sendMessageEmbeds(Helper.sqlServerError()).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Set the current channel to the update channel!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/bot-update set-channel`";
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.SETTINGS;
    }

    @NotNull
    @Override
    public String getName() {
        return "set-channel";
    }

}
