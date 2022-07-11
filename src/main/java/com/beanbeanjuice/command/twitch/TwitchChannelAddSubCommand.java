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
 * An {@link ISubCommand} to add a twitch channel to the listener.
 *
 * @author beanbeanjuice
 */
public class TwitchChannelAddSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        if (!GuildHandler.getCustomGuild(event.getGuild()).addTwitchChannel(event.getOption("twitch_channel").getAsString())) {
            event.getHook().sendMessageEmbeds(alreadyAddedEmbed()).queue();
            return;
        }

        event.getHook().sendMessageEmbeds(successfulAddEmbed(event.getOption("twitch_channel").getAsString())).queue();
    }

    @NotNull
    private MessageEmbed successfulAddEmbed(@NotNull String twitchName) {
        return new EmbedBuilder()
                .setTitle("Successfully Added Twitch Channel")
                .setColor(Helper.getRandomColor())
                .setDescription("Successfully added `" + twitchName + "`!")
                .build();
    }

    @NotNull
    private MessageEmbed alreadyAddedEmbed() {
        return new EmbedBuilder()
                .setColor(Color.red)
                .setDescription("Unable to add the twitch channel. The channel may already be added. " +
                        "To see which channels are added, do `/twitch-channel list`.")
                .setTitle("Error Adding Twitch Channel")
                .build();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Add a twitch channel to be notified of when they are live!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/twitch-channel add beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "twitch_channel", "The twitch channel name. NOT the link.", true));
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
        return "add";
    }

}
