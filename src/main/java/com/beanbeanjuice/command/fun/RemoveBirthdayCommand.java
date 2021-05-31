package com.beanbeanjuice.command.fun;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to remove your birthday.
 *
 * @author beanbeanjuice
 */
public class RemoveBirthdayCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        if (CafeBot.getBirthdayHandler().removeBirthday(user.getId())) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                    "Removed Birthday",
                    "Successfully removed your birthday!"
            )).queue();
            return;
        }

        event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
    }

    @Override
    public String getName() {
        return "remove-birthday";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("removebirthday");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Remove your birthday.";
    }

    @Override
    public String exampleUsage() {
        return "`!!remove-birthday";
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
