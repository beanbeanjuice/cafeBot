package com.beanbeanjuice.command.settings.venting;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ISubCommand;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link ISubCommand} used to set the venting {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}
 * for a specified {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class SetVentingChannelSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        if (GuildHandler.getCustomGuild(event.getGuild()).setVentingChannelID(event.getChannel().getId())) {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Set Venting Channel",
                    "This channel will now receive anonymous vents! This can cause many moderation issues within your server as " +
                            "you will no longer be able to tell who sent what message in this chat. To disable this, just do `/venting-channel remove`."
            )).queue();
            return;
        }
        event.getHook().sendMessageEmbeds(Helper.sqlServerError()).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Set the current channel to the venting channel!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/venting-channel set`";
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
