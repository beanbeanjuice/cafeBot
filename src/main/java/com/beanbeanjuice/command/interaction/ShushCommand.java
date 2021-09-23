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
 * An {@link ICommand} used to shush people.
 */
public class ShushCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        Interaction interaction = new Interaction(InteractionType.SHUSH,
                "**{sender}** *shushed* themselves! :zipper_mouth:",
                "**{sender}** *shushed* **{receiver}**!",
                "{sender} shushed others {amount_sent} times. {receiver} was shushed {amount_received} times.",
                user,
                args,
                event.getChannel());

        if (interaction.containsCafeBot()) {
            event.getMessage().reply("Ooop ~~I'm sorry I'll shush...~~").queue();
        }
    }

    @Override
    public String getName() {
        return "shush";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("shh");
        arrayList.add("shhh");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Shush someone!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "shush @beanbeanjuice` or `" + prefix + "shush @beanbeanjuice SHUT UP`";
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
