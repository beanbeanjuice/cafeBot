package com.beanbeanjuice.command.moderation;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * An {@link ICommand} to get all of the {@link com.beanbeanjuice.utility.guild.CustomChannel CustomChannels} in the {@link net.dv8tion.jda.api.entities.Guild Guild}.
 *
 * @author beanbeanjuice
 */
public class GetCustomChannelsCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Custom Channels");
        StringBuilder descriptionBuilder = new StringBuilder();
        CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).getCustomChannelIDs().forEach((customChannel, channelID) -> {
            descriptionBuilder.append("**").append(customChannel.getName()).append("**: ");
            try {
                TextChannel channel = event.getGuild().getTextChannelById(channelID);
                descriptionBuilder.append(channel.getAsMention()).append("\n");
            } catch (NullPointerException e) {
                descriptionBuilder.append("`Not Set`\n");
            }
        });
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setDescription(descriptionBuilder.toString());
        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
    }

    @Override
    public String getName() {
        return "get-custom-channels";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("getcustomchannels");
        arrayList.add("getchannels");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Gets the custom channels currently being used with the bot.";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "get-custom-channels";
    }

    @Override
    public Usage getUsage() {
        return new Usage();
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MODERATION;
    }
}
