package com.beanbeanjuice.command.twitch;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ISubCommand;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link ISubCommand} used to list all of the twitch channels
 * in a specified {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class TwitchChannelListSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Helper.getRandomColor())
                .setTitle("List of Current Twitch Channels");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("`");

        for (String channel : GuildHandler.getCustomGuild(event.getGuild()).getTwitchChannels()) {
            stringBuilder.append(channel).append("\n");
        }

        if (GuildHandler.getCustomGuild(event.getGuild()).getTwitchChannels().isEmpty()) {
            stringBuilder.append("No channels added.");
        }

        stringBuilder.append("`");
        embedBuilder.setDescription(stringBuilder.toString());
        event.getHook().sendMessageEmbeds(embedBuilder.build()).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "List all of the twitch channels in this server!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/twitch-channel list`";
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
        return "list";
    }

}
