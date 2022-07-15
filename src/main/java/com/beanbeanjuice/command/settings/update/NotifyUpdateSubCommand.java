package com.beanbeanjuice.command.settings.update;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ISubCommand;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ISubCommand} used to set the notification status for updated for the specified
 * {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class NotifyUpdateSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        boolean notify = event.getOption("option").getAsBoolean();

        if (notify) {
            if (GuildHandler.getCustomGuild(event.getGuild()).setNotifyOnUpdate(true)) {
                event.getHook().sendMessageEmbeds(Helper.successEmbed(
                        "Enabled Updated Notifications",
                        "Thank you for enabling update notifications! Don't forget to set " +
                                "the update channel using `/bot-update set-channel`!"
                )).queue();
            } else {
                errorMessage(event);
            }
        } else {
            if (GuildHandler.getCustomGuild(event.getGuild()).setNotifyOnUpdate(false)) {
                event.getHook().sendMessageEmbeds(Helper.successEmbed(
                        "Disabled Update Notifications",
                        "Successfully disabled update notifications!"
                )).queue();
            } else {
                errorMessage(event);
            }
        }
    }

    private void errorMessage(@NotNull SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                "Unable to Update Bot Notifications",
                "There was an error updating the bot notification state. Please " +
                        "try again later."
        )).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Set whether to be updated when I am updated!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/bot-update notify true` or `/bot-update notify false`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.BOOLEAN, "option", "Set to 'true' to be notified when there is an update!", true));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.SETTINGS;
    }

    @NotNull
    @Override
    public String getName() {
        return "notify";
    }

}
