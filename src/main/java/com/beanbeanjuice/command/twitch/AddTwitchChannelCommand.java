package com.beanbeanjuice.command.twitch;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

/**
 * A command to add a twitch channel.
 *
 * @author beanbeanjuice
 */
public class AddTwitchChannelCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        if (!CafeBot.getGeneralHelper().isModerator(event.getMember(), event.getGuild(), event)) {
            return;
        }

        if (!ctx.getCustomGuild().addTwitchChannel(args.get(0))) {
            event.getChannel().sendMessage(alreadyAddedEmbed()).queue();
            return;
        }

        event.getChannel().sendMessage(successfulAddEmbed(args.get(0))).queue();
    }

    @NotNull
    private MessageEmbed successfulAddEmbed(@NotNull String twitchName) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Successfully Added Twitch Channel");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setDescription("Successfully added `" + twitchName + "`!");
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed alreadyAddedEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.red);
        embedBuilder.setDescription("Unable to add the twitch channel. The channel may already be added.");
        embedBuilder.setTitle("Error Adding Twitch Channel");
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "add-twitch-channel";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("addtwitchchannel");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Add a twitch channel to the live channel!";
    }

    @Override
    public String exampleUsage() {
        return "`!!addtwitchchannel beanbeanjuice`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.TEXT, "Twitch Username", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.TWITCH;
    }
}
