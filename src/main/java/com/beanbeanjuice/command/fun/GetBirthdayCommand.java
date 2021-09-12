package com.beanbeanjuice.command.fun;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import io.github.beanbeanjuice.cafeapi.cafebot.birthdays.Birthday;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

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

        Birthday birthday = CafeBot.getBirthdayHandler().getBirthday(birthdayUser.getId());

        // Checks if the birthday exists.
        if (birthday == null) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Error Getting Birthday",
                    "There has either been an error getting the birthday, or the birthday has not been set."
            )).queue();
            return;
        }

        event.getChannel().sendMessageEmbeds(birthdayEmbed(birthdayUser, birthday)).queue();
    }

    /**
     * Creates a {@link MessageEmbed} for the {@link Birthday} for a {@link User}.
     * @param user The specified {@link User}.
     * @param birthday The {@link Birthday} for the {@link User}.
     * @return The created {@link MessageEmbed} for the {@link Birthday}.
     */
    @NotNull
    private MessageEmbed birthdayEmbed(@NotNull User user, @NotNull Birthday birthday) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setTitle(user.getName() + "'s Birthday");

        embedBuilder.setDescription("Birthday is on `" + birthday.getMonth() + ", " + birthday.getDay() + "`.");
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
