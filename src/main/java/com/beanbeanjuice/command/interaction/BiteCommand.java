package com.beanbeanjuice.command.interaction;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.logger.LogLevel;
import com.beanbeanjuice.utility.sections.interaction.Interaction;
import io.github.beanbeanjuice.cafeapi.cafebot.interactions.InteractionType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to bite people!
 *
 * @author beanbeanjuice
 */
public class BiteCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        Interaction interaction = new Interaction(InteractionType.BITE,
                "**{sender}** *bit* themselves! Ow!",
                "**{sender}** *bit* **{receiver}**! What did they do?!?!?!?",
        "{sender} bit others {amount_sent} times. {receiver} was bitten {amount_received} times.",
                user,
                args,
                event.getChannel());

        CafeBot.getLogManager().log(this.getClass(), LogLevel.DEBUG, ctx.getSelfMember().getId());
        CafeBot.getLogManager().log(this.getClass(), LogLevel.DEBUG, user.getId());

        if (interaction.containsCafeBot()) {
            event.getMessage().reply("Ow! Why would you do that to me?!?").queue();
        }
    }

    @Override
    public String getName() {
        return "bite";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Bite someone!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "bite @beanbeanjuice` or `" + prefix + "bite @beanbeanjuice :O`";
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
