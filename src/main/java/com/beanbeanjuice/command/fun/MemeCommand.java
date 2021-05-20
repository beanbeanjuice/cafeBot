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
 * A command used for memes.
 *
 * @author beanbeanjuice
 */
public class MemeCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        WebUtils.ins.getJSONObject("https://apis.duncte123.me/meme").async((json) -> {
            if (!json.get("success").asBoolean()) {
                event.getChannel().sendMessage(cannotGetJSONEmbed()).queue();
                CafeBot.getLogManager().log(MemeCommand.class, LogLevel.ERROR, "Cannot get JSON.");
                return;
            }

            final JsonNode data = json.get("data");
            final String title = data.get("title").asText();
            final String url = data.get("url").asText();
            final String image = data.get("image").asText();

            event.getChannel().sendMessage(messageEmbed(title, url, image)).queue();
        });
    }

    @NotNull
    private MessageEmbed cannotGetJSONEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription("Unable to get JSON.");
        embedBuilder.setColor(Color.red);
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed messageEmbed(@NotNull String title, @NotNull String url, @NotNull String image) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title, url);
        embedBuilder.setImage(image);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "meme";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Get a random meme!";
    }

    @Override
    public String exampleUsage() {
        return "`!!meme`";
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