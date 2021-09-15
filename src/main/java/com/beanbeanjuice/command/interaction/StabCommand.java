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
 * An {@link ICommand} used to stab people.
 *
 * @author beanbeanjuice
 */
public class StabCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        Interaction interaction = new Interaction(InteractionType.STAB,
                "**{sender}** *stabbed* themselves! STOP??? DON'T DO THAT IN THE CAFE?!? <:madison_pissed:842061821774004304>",
                "**{sender}** *stabbed* **{receiver}**!",
                "{sender} stabbed others {amount_sent} times. {receiver} was stabbed at {amount_received} times.",
                user,
                args,
                event.getChannel());

        if (interaction.containsCafeBot()) {
            event.getMessage().reply("Knives don't work on me.").queue();
        }
    }

    @Override
    public String getName() {
        return "stab";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Stab someone!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "stab @beanbeanjuice` or `" + prefix + "stab @beanbeanjuice get stabbed`";
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
