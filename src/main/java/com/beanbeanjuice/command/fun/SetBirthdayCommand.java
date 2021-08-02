package com.beanbeanjuice.command.fun;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * An {@link ICommand} to set your birthday.
 *
 * @author beanbeanjuice
 */
public class SetBirthdayCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        String pattern = "MMMM, d";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = CafeBot.getGeneralHelper().parseDate("2000-" + args.get(0));
        if (CafeBot.getBirthdayHandler().updateBirthday(user.getId(), date)) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().successEmbed(
                    "Updated Birthday",
                    "Successfully updated your birthday to " + simpleDateFormat.format(date) + " (Month, Day)\n\n*By setting your birthday, " +
                            "you are agreeing to be notified in EVERY server that this bot is in and that you are in, granted that they have enabled " +
                            "birthday notifications. To opt out, do `" + ctx.getPrefix() + "remove-birthday`.*"
            )).queue();
            return;
        }
        event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().sqlServerError()).queue();
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
        return "Set your birthday! Use the format `(MM-DD)`!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "set-birthday 02-02` - This sets it to February 2.";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.DATE, "Birthday (MM-DD)", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.FUN;
    }
    
}
