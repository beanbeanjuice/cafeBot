package com.beanbeanjuice.command.moderation;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * An {@link ICommand} used to create custom {@link net.dv8tion.jda.api.entities.MessageEmbed MessageEmbed} for a server.
 *
 * @author beanbeanjuice
 */
public class CreateEmbedCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        if (!CafeBot.getGeneralHelper().isModerator(event.getMember(), event.getGuild(), event)) {
            return;
        }

        TextChannel embedChannel = CafeBot.getGeneralHelper().getTextChannel(event.getGuild(), args.remove(0));
        HashMap<String, String> embedMap = CafeBot.getGeneralHelper().createCommandTermMap(getCommandTerms(), args);

        if (embedMap.get("message") != null) {
            embedChannel.sendMessage(embedMap.get("message")).embed(createEmbed(embedMap)).queue();
        } else {
            embedChannel.sendMessage(createEmbed(embedMap)).queue();
        }

        try {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                    "Created the Custom Message Embed",
                    "Successfully created the custom embed in " + embedChannel.getAsMention() + "!"
            )).queue();
        } catch (ErrorResponseException e) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                    "Invalid URL",
                    "The URL you have entered for the image or thumbnail is not valid."
            )).queue();
        }
    }

    private ArrayList<String> getCommandTerms() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("color");
        arrayList.add("footer");
        arrayList.add("thumbnail");
        arrayList.add("image");
        arrayList.add("description");
        arrayList.add("author");
        arrayList.add("title");
        arrayList.add("message");
        return arrayList;
    }

    private MessageEmbed createEmbed(@NotNull HashMap<String, String> embedMap) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if (embedMap.containsKey("title")) {
            embedBuilder.setTitle(embedMap.get("title"));
        }

        if (embedMap.containsKey("author")) {
            embedBuilder.setAuthor(embedMap.get("author"));
        }

        if (embedMap.containsKey("description")) {
            embedBuilder.setDescription(embedMap.get("description").replace("\\n", "\n"));
        }

        if (embedMap.containsKey("image")) {
            embedBuilder.setImage(embedMap.get("image"));
        }

        if (embedMap.containsKey("thumbnail")) {
            embedBuilder.setThumbnail(embedMap.get("thumbnail"));
        }

        if (embedMap.containsKey("footer")) {
            embedBuilder.setFooter(embedMap.get("footer"));
        }

        if (embedMap.containsKey("color")) {
            embedBuilder.setColor(Color.decode(embedMap.get("color")));
        }
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "create-embed";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("createembed");
        arrayList.add("embed");
        arrayList.add("add-embed");
        arrayList.add("addembed");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Create a custom embed for your server! For the description, you can use `\\n` to make a new line!\n" +
                "Command terms are `title`, `author`, `message`, `description`, `image`, `thumbnail`, `footer`, and `color`.";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "add-embed #announcements message:@everyone, check this out! title:Cool Announcement author:people person color:#FFC0CB description:Wow awesome description!\\nWow so cool! image:https://cool_image.png thumbnail:https://cool_image.png footer:Cool Footer!`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.TEXT_CHANNEL, "Channel Mention/ID", true);
        usage.addUsage(CommandType.SENTENCE, "Embed Information from Example", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MODERATION;
    }

}
