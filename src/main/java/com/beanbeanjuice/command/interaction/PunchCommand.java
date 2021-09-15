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
 * An {@link ICommand} used for punching.
 *
 * @author beanbeanjuice
 */
public class PunchCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        Interaction interaction = new Interaction(InteractionType.PUNCH,
                "**{sender}** *punched* themselves! STOP!!! <:madison_when_short:843673314990882836>",
                "**{sender}** *punched* **{receiver}**!",
                "{sender} punched others {amount_sent} times. {receiver} was punched {amount_received} times.",
                user,
                args,
                event.getChannel());

        if (interaction.containsCafeBot()) {
            event.getMessage().reply("You punched a robot. Your hands are now broken.").queue();
        }
    }

    @Override
    public String getName() {
        return "punch";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Punch someone!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "punch @beanbeanjuice` or `" + prefix + "punch @beanbeanjuice WHY WOULD YOU DO THIS?`";
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
