package com.beanbeanjuice.command.fun;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.Date;
import java.util.ArrayList;

/**
 * An {@link ICommand} to set your birthday.
 *
 * @author beanbeanjuice
 */
public class SetBirthdayCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        Date date = CafeBot.getGeneralHelper().parseDate(args.get(0));
        if (CafeBot.getBirthdayHandler().updateBirthday(user.getId(), date)) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                    "Updated Birthday",
                    "Successfully updated your birthday to " + date.toString() + " (YYYY-MM-DD)"
            )).queue();
            return;
        }
        event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
    }

    @Override
    public String getName() {
        return "set-birthday";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("setbirthday");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Set your birthday! Use the format `(YYYY-MM-DD)`!";
    }

    @Override
    public String exampleUsage() {
        return "`!!set-birthday 2000-02-02` - This sets it to February 2, 2002.";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.DATE, "Birthdate (YYYY-MM-DD)", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.FUN;
    }
}
