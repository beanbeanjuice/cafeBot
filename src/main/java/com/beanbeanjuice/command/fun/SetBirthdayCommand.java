package com.beanbeanjuice.command.fun;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import io.github.beanbeanjuice.cafeapi.cafebot.birthdays.BirthdayMonth;
import io.github.beanbeanjuice.cafeapi.exception.TeaPotException;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * An {@link ICommand} to set your birthday.
 *
 * @author beanbeanjuice
 */
public class SetBirthdayCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        BirthdayMonth birthdayMonth = getBirthdayMonth(args.get(0));

        // Checks if the month is valid.
        if (birthdayMonth == BirthdayMonth.ERROR || birthdayMonth == null) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Invalid Month",
                    "The month specified is not valid."
            )).queue();
            return;
        }

        // TODO: Not catching the TeaPotException
        try {
            CafeBot.getBirthdayHandler().updateBirthday(user.getId(), birthdayMonth, Integer.parseInt(args.get(1)));
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().successEmbed(
                    "Updated Birthday",
                    "Successfully updated your birthday to `" + birthdayMonth + ", " + args.get(1) + "` (Month, Day)\n\n*By setting your birthday, " +
                            "you are agreeing to be notified in EVERY server that this bot is in and that you are in, granted that they have enabled " +
                            "birthday notifications. To opt out, do `" + ctx.getPrefix() + "remove-birthday`.*"
            )).queue();
            return;
        } catch (TeaPotException e) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Invalid Month/Day",
                    "The month/day specified is invalid. If you have chosen more days than there are in the month, it will cause an error."
            )).queue();
            return;
        }
    }

    /**
     * Gets the {@link BirthdayMonth} from a {@link String}.
     * @param month The {@link String} to parse.
     * @return The parsed {@link BirthdayMonth}. Null, if there is an error.
     */
    @Nullable
    private BirthdayMonth getBirthdayMonth(@NotNull String month) {
        for (BirthdayMonth birthdayMonth : BirthdayMonth.values()) {
            if (birthdayMonth.toString().toLowerCase().equals(month.toLowerCase())) {
                return birthdayMonth;
            }
        }

        return null;
    }

    @Override
    public String getName() {
        return "set-birthday";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("setbirthday");
        arrayList.add("add-birthday");
        arrayList.add("addbirthday");
        arrayList.add("update-birthday");
        arrayList.add("updatebirthday");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Set your birthday! Use the format `(MM-DD)`!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "set-birthday february 2` - This sets it to February 2.";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.TEXT, "Month", true);
        usage.addUsage(CommandType.NUMBER, "Day", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.FUN;
    }
    
}
