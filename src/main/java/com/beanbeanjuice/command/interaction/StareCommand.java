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
 * An {@link ICommand} used to stare at people.
 *
 * @author beanbeanjuice
 */
public class StareCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        new Interaction(InteractionType.STARE,
                "**{sender}** *stared* at themselves!",
                "**{sender}** *stared* at **{receiver}**!",
                "{sender} stared at others {amount_sent} times. {receiver} was stared at {amount_received} times.",
                user,
                args,
                event.getChannel());
    }

    @Override
    public String getName() {
        return "stare";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Stare at someone!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "stare @beanbeanjuice` or `" + prefix + "stare @beanbeanjuice :O`";
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
