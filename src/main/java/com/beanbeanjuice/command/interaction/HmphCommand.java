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
 * An {@link ICommand} used to hmph at people.
 *
 * @author beanbeanjuice
 */
public class HmphCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        new Interaction(InteractionType.HMPH,
                "**{sender}** *hmphed*! What's wrong **{sender}**?",
                "**{sender}** *hmphed* at **{receiver}**!",
                "{sender} hmphed at others {amount_sent} times. {receiver} was hmphed at {amount_received} times.",
                user,
                args,
                event.getChannel());
    }

    @Override
    public String getName() {
        return "hmph";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("hmpf");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Hmph at someone!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "hmph @beanbeanjuice` or `" + prefix + "hmph @beanbeanjuice Go away!`";
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
