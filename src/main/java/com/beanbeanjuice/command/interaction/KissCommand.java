package com.beanbeanjuice.command.interaction;

import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.sections.interaction.Interaction;
import io.github.beanbeanjuice.cafeapi.cafebot.interactions.InteractionType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to kiss people.
 *
 * @author beanbeanjuice
 */
public class KissCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        new Interaction(InteractionType.KISS,
                "**{sender}** *kissed themselves*! You kissed your hand right **{sender}**? Right?!?",
                "**{sender}** *kissed* **{receiver}**!",
                "{sender} kissed others {amount_sent} times. {receiver} was kissed {amount_received} times.",
                user,
                args,
                event.getChannel());
    }

    @Override
    public String getName() {
        return "kiss";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("mwah");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Kiss someone!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "kiss @beanbeanjuice` or `" + prefix + "kiss @beanbeanjuice :O`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.SENTENCE, "Users + Extra Message", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.INTERACTION;
    }

}
