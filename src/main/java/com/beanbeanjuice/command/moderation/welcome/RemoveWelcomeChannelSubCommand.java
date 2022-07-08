package com.beanbeanjuice.command.moderation.welcome;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ISubCommand;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Removes the current welcome {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}
 * from the specified {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class RemoveWelcomeChannelSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        if (GuildHandler.getCustomGuild(event.getGuild()).setWelcomeChannelID("0")) {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Removed Welcome Channel",
                    "The welcome channel has been successfully removed!"
            )).queue();
            return;
        }
        event.getHook().sendMessageEmbeds(Helper.sqlServerError()).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Remove the current welcome channel!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/welcome-channel remove`";
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.MODERATION;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return false;
    }

    @NotNull
    @Override
    public String getName() {
        return "remove";
    }

}
