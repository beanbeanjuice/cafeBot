package com.beanbeanjuice.command.twitch;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ISubCommand;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

/**
 * An {@link ISubCommand} used to remove a twitch channel from being notified in
 * the {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class TwitchChannelRemoveSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        if (!GuildHandler.getCustomGuild(event.getGuild()).removeTwitchChannel(event.getOption("twitch_channel").getAsString())) {
            event.getHook().sendMessageEmbeds(notAddedEmbed()).queue();
            return;
        }

        event.getHook().sendMessageEmbeds(successfulRemoveEmbed(event.getOption("twitch_channel").getAsString())).queue();
    }

    @NotNull
    private MessageEmbed successfulRemoveEmbed(@NotNull String twitchName) {
        return new EmbedBuilder()
                .setTitle("Successfully Removed the Twitch Channel")
                .setColor(Helper.getRandomColor())
                .setDescription("Successfully removed `" + twitchName + "`!")
                .build();
    }

    @NotNull
    private MessageEmbed notAddedEmbed() {
        return new EmbedBuilder()
                .setColor(Color.red)
                .setDescription("Unable to remove the twitch channel. The twitch channel may not be added. " +
                        "To see which channels are added, do `/twitch-channel list`")
                .setTitle("Error Removing Twitch Channel")
                .build();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Remove a twitch channel from being notified when they are live!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/twitch-channel remove beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "twitch_channel", "The twitch channel to remove.", true));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.TWITCH;
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
