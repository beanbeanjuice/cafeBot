package com.beanbeanjuice.command.twitch;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

/**
 * A command to set the live channel.
 *
 * @author beanbeanjuice
 */
public class SetLiveChannelCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        if (!CafeBot.getGeneralHelper().isModerator(event.getMember(), event.getGuild(), event)) {
            return;
        }

        if (!CafeBot.getGuildHandler().getCustomGuild(event.getGuild().getId()).updateTwitchDiscordChannel(event.getChannel().getId())) {
            event.getChannel().sendMessage(unsuccessfulEmbed()).queue();
            return;
        }

        event.getChannel().sendMessage(successfulEmbed()).queue();
    }

    @NotNull
    private MessageEmbed successfulEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Successfully set the Live Channel");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setDescription("Successfully set the live channel to this channel!");
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed unsuccessfulEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.red);
        embedBuilder.setDescription("Unable to set the current channel to the twitch live channel.");
        embedBuilder.setTitle("Error Setting Live Channel");
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "set-live-channel";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("setlivechannel");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Set the current channel to a live channel.";
    }

    @Override
    public String exampleUsage() {
        return "`!!setlivechannel`";
    }

    @Override
    public Usage getUsage() {
        return new Usage();
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.TWITCH;
    }
}
