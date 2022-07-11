package com.beanbeanjuice.command.settings.daily;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ISubCommand;
import com.beanbeanjuice.utility.handler.guild.CustomChannel;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An {@link ISubCommand} used to set the daily {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}
 * for the specified {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class SetDailyChannelSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        AtomicBoolean alreadySet = new AtomicBoolean(false);
        GuildHandler.getCustomGuild(event.getGuild()).getCustomChannelIDs().forEach((customChannel, channelID) -> {
            if (!customChannel.equals(CustomChannel.DAILY)) {
                if (channelID.equals(event.getChannel().getId())) {
                    alreadySet.set(true);
                }
            }
        });

        if (alreadySet.get()) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed("Channel Already Set",
                    "This current channel is already set to something. For a list of channels you are already using with this bot, do `/" +
                            "list-custom-channels`")).queue();
            return;
        }

        if (GuildHandler.getCustomGuild(event.getGuild()).setDailyChannelID(event.getChannel().getId())) {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Updated Daily Channel",
                    "Successfully set the daily channel to this channel. " +
                            "To remove the daily channel, just delete the channel. Just a reminder, " +
                            "any integrations you have with this channel will be deleted upon the copy of this channel."
            )).queue();

            return;
        }
        event.getHook().sendMessageEmbeds(Helper.sqlServerError()).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Set the current channel to the daily channel!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/daily-channel set`";
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
