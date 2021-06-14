package com.beanbeanjuice.command.fun;

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

import java.sql.Date;
import java.util.ArrayList;

/**
 * An {@link ICommand} used to get someone's birthday.
 *
 * @author beanbeanjuice
 */
public class GetBirthdayCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        User birthdayUser;

        if (args.size() == 0) {
            birthdayUser = user;
        } else {
            birthdayUser = CafeBot.getGeneralHelper().getUser(args.get(0));
        }
        Date birthday = CafeBot.getBirthdayHandler().getBirthday(birthdayUser.getId());

        // Checking if the user's birthday is null.
        if (birthday == null) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                    "No Birthday Set",
                    "The user specified does not have a birthday set."
            )).queue();
            return;
        }
        event.getChannel().sendMessage(birthdayEmbed(birthdayUser, birthday)).queue();
    }

    @NotNull
    private MessageEmbed birthdayEmbed(@NotNull User user, @NotNull Date birthday) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setTitle(user.getName() + "'s Birthday");
        embedBuilder.setDescription("Their birthday is on `" + birthday + "`. (YYYY/MM/DD)");
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "get-birthday";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("getbirthday");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Get yours or someones birthday!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "getbirthday` or `" + prefix + "getbirthday @beanbeanjuice`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.USER, "Discord Mention", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.FUN;
    }

}
