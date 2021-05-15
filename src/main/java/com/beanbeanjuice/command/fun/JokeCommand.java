package com.beanbeanjuice.command.fun;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.logger.LogLevel;
import com.fasterxml.jackson.databind.JsonNode;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

/**
 * A command used for sending jokes.
 *
 * @author beanbeanjuice
 */
public class JokeCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        WebUtils.ins.getJSONObject("https://apis.duncte123.me/joke").async((json) -> {
            if (!json.get("success").asBoolean()) {
                event.getChannel().sendMessage(cannotGetJSONEmbed()).queue();
                CafeBot.getLogManager().log(JokeCommand.class, LogLevel.ERROR, "Cannot get JSON.");
                return;
            }

            final JsonNode data = json.get("data");
            final String title = data.get("title").asText();
            final String url = data.get("url").asText();
            final String body = data.get("body").asText();

            event.getChannel().sendMessage(messageEmbed(title, url, body)).queue();
        });
    }

    @NotNull
    private MessageEmbed messageEmbed(@NotNull String title, @NotNull String url, @NotNull String body) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(title, url);
        embedBuilder.setDescription(body);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed cannotGetJSONEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Error");
        embedBuilder.setColor(Color.red);
        embedBuilder.setDescription("Unable to get JSON.");
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "joke";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "say a random joke!";
    }

    @Override
    public String exampleUsage() {
        return "`!!joke`";
    }

    @Override
    public Usage getUsage() {
        return new Usage();
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.FUN;
    }
}