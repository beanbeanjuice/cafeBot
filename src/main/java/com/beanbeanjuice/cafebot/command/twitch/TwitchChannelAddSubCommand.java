package com.beanbeanjuice.cafebot.command.twitch;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ISubCommand;
import com.beanbeanjuice.cafebot.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.section.twitch.TwitchHandler;
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
        String channelName = event.getOption("twitch_channel").getAsString();

        if (!channelName.matches("[a-zA-Z0-9]*") || !TwitchHandler.getTwitchListener().channelExists(channelName) || channelName.length() > 25) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Channel Does Not Exist",
                    "The twitch channel `" + channelName + "` does not exist."
            )).queue();
            return;
        }

        if (!GuildHandler.getCustomGuild(event.getGuild()).addTwitchChannel(channelName)) {
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
    public String getName() {
        return "add";
    }

}
