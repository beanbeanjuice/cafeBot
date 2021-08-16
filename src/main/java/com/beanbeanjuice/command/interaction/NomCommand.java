package com.beanbeanjuice.command.interaction;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.sections.interaction.InteractionType;
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

        ArrayList<User> receivers = new ArrayList<>();
        int count = 0;

        if (!args.isEmpty()) {
            while (CafeBot.getGeneralHelper().getUser(args.get(count)) != null) {
                receivers.add(CafeBot.getGeneralHelper().getUser(args.get(count++)));
                if (count == args.size()) {
                    break;
                }
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = count; i < args.size(); i++) {
            stringBuilder.append(args.get(i));
            if (i != args.size() - 1) {
                stringBuilder.append(" ");
            }
        }

        String message;
        String footer = null;

        if (receivers.size() == 0) {
            message = "**" + sender + "** *nommed* themselves! Doesn't that... hurt?";
        } else {
            message = "**" + sender + "** *nommed* at **" + CafeBot.getInteractionHandler().getReceiverString(receivers) + "**!";

            if (receivers.size() == 1) {
                int sendAmount = CafeBot.getInteractionHandler().getSender(user.getId(), InteractionType.NOM) + 1;
                int receiveAmount = CafeBot.getInteractionHandler().getReceiver(receivers.get(0).getId(), InteractionType.NOM) + 1;

                CafeBot.getInteractionHandler().updateSender(user.getId(), InteractionType.NOM, sendAmount);
                CafeBot.getInteractionHandler().updateReceiver(receivers.get(0).getId(), InteractionType.NOM, receiveAmount);

                footer = user.getName() + " nommed others " + sendAmount + " times. " + receivers.get(0).getName() + " was nommed " + receiveAmount + " times.";
            }
        }

        if (stringBuilder.isEmpty()) {
            event.getChannel().sendMessage(message).setEmbeds(CafeBot.getInteractionHandler().actionEmbed(url, footer)).queue();
        } else {
            event.getChannel().sendMessage(message).setEmbeds(CafeBot.getInteractionHandler().actionWithDescriptionEmbed(url, stringBuilder.toString(), footer)).queue();
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
    public String exampleUsage(String prefix) {
        return "`" + prefix + "nom @beanbeanjuice` or `" + prefix + "nom @beanbeanjuice wow here's some nom nom`";
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
