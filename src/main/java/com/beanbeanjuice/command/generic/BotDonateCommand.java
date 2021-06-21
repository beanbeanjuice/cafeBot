package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to donate to the bot.
 *
 * @author beanbeanjuice
 */
public class BotDonateCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                "Bot Donation",
                "HellO! If you want to donate, follow this [link](https://streamelements.com/beanbeanjuice/tip)! Don't feel pressured though, even if " +
                        "you don't donate, you will still get all of the features as the people who do, you just will get less of it. For example, " +
                        "instead of 10 polls, as a non-donator, you only get 3."
        )).queue();
    }

    @Override
    public String getName() {
        return "bot-donate";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("botdonate");
        arrayList.add("donate-bot");
        arrayList.add("donatebot");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Donate to the creator of this bot! This will help keep the project running in the future! Additionally, " +
                "this will give donator-only benefits in the future. HOWEVER, these will only be perks like, having 5 extra polls that can be run at once instead of " +
                "just only 3. You won't be losing much by not donating, so don't feel pressured!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "bot-donate`";
    }

    @Override
    public Usage getUsage() {
        return new Usage();
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.GENERIC;
    }
}
