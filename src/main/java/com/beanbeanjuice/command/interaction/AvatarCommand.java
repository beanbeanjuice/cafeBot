package com.beanbeanjuice.command.interaction;

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
 * A command used for Discord avatars.
 *
 * @author beanbeanjuice
 */
public class AvatarCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        User avatarUser;
        if (args.isEmpty()) {
            avatarUser = user;
        } else {
            avatarUser = CafeBot.getGeneralHelper().getUser(args.get(0));
        }
        event.getChannel().sendMessage(avatarEmbed(avatarUser)).queue();
    }

    @NotNull
    private MessageEmbed avatarEmbed(@NotNull User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(user.getName() + "'s Avatar", user.getAvatarUrl());
        embedBuilder.setImage(user.getAvatarUrl() + "?size=512");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "avatar";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("av");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Get someone's avatar!";
    }

    @Override
    public String exampleUsage() {
        return "`!!avatar` or `!!avatar @beanbeanjuice`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.USER, "Discord Mention", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.INTERACTION;
    }
}
