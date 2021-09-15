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
 * An {@link ICommand} used to lick people.
 *
 * @author beanbeanjuice
 */
public class LickCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        Interaction interaction = new Interaction(InteractionType.LICK,
                "**{sender}** *licked* themselves! You at least washed whatever you licked right?",
                "**{sender}** *licked* **{receiver}**! :flushed:",
                "{sender} licked others {amount_sent} times. {receiver} was licked {amount_received} times.",
                user,
                args,
                event.getChannel());

        if (interaction.containsCafeBot()) {
            event.getMessage().reply("Th- that's not sanitary! B- but I don't mind...").queue();
        }
    }

    @Override
    public String getName() {
        return "lick";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Lick someone!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "lick @beanbeanjuice` or `" + prefix + "lick @beanbeanjuice :O`";
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
