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

        HashMap<String, String> embedMap = new HashMap<>();

        for (String string : args) {

            if (string.startsWith("title:")) {
                embedMap.put("title", CafeBot.getGeneralHelper().removeUnderscores(string.split("title:")[1]));
            }

            else if (string.startsWith("author:")) {
                embedMap.put("author", CafeBot.getGeneralHelper().removeUnderscores(string.split("author:")[1]));
            }

            else if (string.startsWith("description:")) {
                embedMap.put("description", CafeBot.getGeneralHelper().removeUnderscores(string.split("description:")[1]));
            }

            else if (string.startsWith("image:")) {
                embedMap.put("image", string.split("image:")[1]);
            }

            else if (string.startsWith("thumbnail:")) {
                embedMap.put("thumbnail", string.split("thumbnail:")[1]);
            }

            else if (string.startsWith("footer:")) {
                embedMap.put("footer", CafeBot.getGeneralHelper().removeUnderscores(string.split("footer:")[1]));
            }

            else if (string.startsWith("color:")) {
                embedMap.put("color", string.split("color:")[1]);
            }
        }

        TextChannel embedChannel = CafeBot.getGeneralHelper().getTextChannel(event.getGuild(), args.get(0));

        embedChannel.sendMessage(createEmbed(embedMap)).queue();
        event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                "Created the Custom Message Embed",
                "Successfully created the custom embed in " + embedChannel.getAsMention() + "!"
        )).queue();
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
        return "Create a custom embed for your server! For the description, you can use `\\n` to make a new line!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "add-embed #announcements title:Cool_Announcement author:people_person color:#FFC0CB description:Wow_awesome_description!\\nWow_so_cool! image:https://cool_image.png thumbnail:https://cool_image.png footer:Cool_Footer!`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.TEXTCHANNEL, "Channel to Send the Embed", true);
        usage.addUsage(CommandType.SENTENCE, "Embed Information from Example", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MODERATION;
    }

}
