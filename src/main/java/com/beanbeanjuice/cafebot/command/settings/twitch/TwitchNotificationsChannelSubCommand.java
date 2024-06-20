package com.beanbeanjuice.cafebot.command.settings.twitch;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ISubCommand;
import com.beanbeanjuice.cafebot.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ISubCommand} used to set/remove the {@link TextChannel TextChannel}
 * that receives live notifications for the specified {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class TwitchNotificationsChannelSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {

        // Checking if a text channel.
        if (!Helper.isTextChannel(event.getChannel())) {
            event.getHook().sendMessageEmbeds(Helper.notATextChannelEmbed(event.getChannelType())).queue();
            return;
        }

        String command = event.getOption("option").getAsString();

        if (command.equalsIgnoreCase("set")) {  // Setting the twitch notifications channel

            TextChannel textChannel = event.getChannel().asTextChannel();

            // If the channel is already set, notify them that this cannot be done.
            if (GuildHandler.getCustomGuild(event.getGuild()).isDailyChannel(textChannel.getId())) {
                event.getHook().sendMessageEmbeds(Helper.alreadyDailyChannel()).queue();
                return;
            }

            if (GuildHandler.getCustomGuild(event.getGuild()).updateTwitchDiscordChannel(textChannel.getId())) {
                event.getHook().sendMessageEmbeds(Helper.successEmbed(
                        "Set Live Channel",
                        "Successfully set the live channel to this channel!"
                )).queue();
                return;
            }

            event.getHook().sendMessageEmbeds(Helper.sqlServerError()).queue();
        } else {  // Removing the twitch notifications channel
            if (GuildHandler.getCustomGuild(event.getGuild()).updateTwitchDiscordChannel("0")) {
                event.getHook().sendMessageEmbeds(Helper.successEmbed(
                        "Removed Live Channel",
                        "Successfully removed the live channel!"
                )).queue();
                return;
            }
            event.getHook().sendMessageEmbeds(Helper.sqlServerError()).queue();
        }
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Set or remove the twitch channel to receive live notifications for!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/twitch-notifications channel set` or `/twitch-notifications channel remove`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "option", "Set or remove the twitch notifications channel!", true)
                .addChoice("set", "set")
                .addChoice("remove", "remove"));
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
        return "channel";
    }

}
