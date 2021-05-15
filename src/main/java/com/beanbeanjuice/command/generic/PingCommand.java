package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.main.CafeBot;
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

import java.util.ArrayList;

/**
 * A general ping command to show bot information.
 *
 * @author beanbeanjuice
 */
public class PingCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        CafeBot.getJDA().getRestPing().queue(
                (ping) -> event.getChannel()
                        .sendMessage(messageEmbed(ping, CafeBot.getJDA().getGatewayPing())).queue()
        );
    }

    @NotNull
    private MessageEmbed messageEmbed(@NotNull Long botPing, @NotNull Long gatewayPing) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(getName() + "!", "https://www.beanbeanjuice.com/cafeBot.html");
        StringBuilder descriptionBuilder = new StringBuilder();
        descriptionBuilder.append("**Rest Ping** - `").append(botPing.toString()).append("`\n")
                .append("**Gateway Ping** - `").append(gatewayPing.toString()).append("`\n")
                .append("**Current Version** - `").append(CafeBot.getBotVersion()).append("`\n\n")
                .append("Hello there! How are you? Would you like to order some coffee?");
        embedBuilder.setDescription(descriptionBuilder.toString());
        embedBuilder.setFooter("Author: beanbeanjuice - " + "https://github.com/beanbeanjuice/cafeBot");
        embedBuilder.setThumbnail(CafeBot.getJDA().getSelfUser().getAvatarUrl());
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Ping the bot!";
    }

    @Override
    public String exampleUsage() {
        return "`!!ping`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.TEXT, "Any Text", false);
        usage.addUsage(CommandType.LINK, "Any Link", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.GENERIC;
    }
}