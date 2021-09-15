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
 * An {@link ICommand} used to die.
 *
 * @author beanbeanjuice
 */
public class DieCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        Interaction interaction = new Interaction(InteractionType.DIE,
                "**{sender}** *died*! SOMEONE HELP! <:madison_when_short:843673314990882836>",
                "**{sender}** *died* because of **{receiver}**! <:madison_when_short:843673314990882836>",
                "{sender} died {amount_sent} times. {receiver} was someone's cause of death {amount_received} times.",
                user,
                args,
                event.getChannel());

        if (interaction.containsCafeBot()) {
            event.getMessage().reply("Wh- why'd you die?!? Someone? Get help please!").queue();
        }
    }

    @Override
    public String getName() {
        return "die";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Die with someone!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "die @beanbeanjuice` or `" + prefix + "die @beanbeanjuice :O`";
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
