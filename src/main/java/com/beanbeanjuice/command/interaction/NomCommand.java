package com.beanbeanjuice.command.interaction;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to nom at someone!
 *
 * @author beanbeanjuice
 */
public class NomCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        String url = CafeBot.getInteractionHandler().getNomImage();
        String sender = user.getName();
        String receiver = CafeBot.getGeneralHelper().getUser(args.get(0)).getName();
        String message = "**" + sender + "** *nommed* at **" + receiver + "**!";

        if (args.size() == 1) {
            event.getChannel().sendMessage(message).embed(CafeBot.getInteractionHandler().actionEmbed(url)).queue();
        } else {
            event.getChannel().sendMessage(message).embed(CafeBot.getInteractionHandler().actionWithDescriptionEmbed(url, args)).queue();
        }
    }

    @Override
    public String getName() {
        return "nom";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Nom at someone!";
    }

    @Override
    public String exampleUsage() {
        return "`!!nom @beanbeanjuice` or `!!nom @beanbeanjuice wow here's some nom nom`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.USER, "User Mention", true);
        usage.addUsage(CommandType.SENTENCE, "Extra Message", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.INTERACTION;
    }

}
