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
 * An {@link ICommand} used to cuddle people.
 *
 * @author beanbeanjuice
 */
public class CuddleCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        Interaction interaction = new Interaction(InteractionType.CUDDLE,
                "**{sender}** *cuddled* themselves! That's... kind of sad... <:madison_moment:843672933176311808>",
                "**{sender}** *cuddled* **{receiver}**! That's adorable! <a:wowowow:886217210010431508>",
                "{sender} cuddled others {amount_sent} times. {receiver} was cuddled {amount_received} times.",
                user,
                args,
                event.getChannel());

        if (interaction.containsCafeBot()) {
            event.getMessage().reply("So cute! Come here~").queue();
        }
    }

    @Override
    public String getName() {
        return "cuddle";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Cuddle someone!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "cuddle @beanbeanjuice` or `" + prefix + "cuddle @beanbeanjuice :O`";
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
