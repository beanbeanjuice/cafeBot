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
 * An {@link ICommand} used to sleep.
 *
 * @author beanbeanjuice
 */
public class SleepCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        String url = CafeBot.getInteractionHandler().getSleepImage();
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
            message = "**" + sender + "** is *sleeping* by themselves! Anyone want to join in?";
        } else {
            message = "**" + sender + "** is *sleeping* with **" + CafeBot.getInteractionHandler().getReceiverString(receivers) + "**! Everyone... shhhh...";

            if (receivers.size() == 1) {
                int sendAmount = CafeBot.getInteractionHandler().getSender(user.getId(), InteractionType.SLEEP) + 1;
                int receiveAmount = CafeBot.getInteractionHandler().getReceiver(receivers.get(0).getId(), InteractionType.SLEEP) + 1;

                CafeBot.getInteractionHandler().updateSender(user.getId(), InteractionType.SLEEP, sendAmount);
                CafeBot.getInteractionHandler().updateReceiver(receivers.get(0).getId(), InteractionType.SLEEP, receiveAmount);

                footer = user.getName() + " slept with others " + sendAmount + " times. " + receivers.get(0).getName() + " was slept with " + receiveAmount + " times.";
            }
        }

        if (stringBuilder.isEmpty()) {
            event.getChannel().sendMessage(message).embed(CafeBot.getInteractionHandler().actionEmbed(url, footer)).queue();
        } else {
            event.getChannel().sendMessage(message).embed(CafeBot.getInteractionHandler().actionWithDescriptionEmbed(url, stringBuilder.toString(), footer)).queue();
        }
    }

    @Override
    public String getName() {
        return "sleep";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Sleep with someone!";
    }

    @Override
    public String exampleUsage() {
        return "`!!sleep @beanbeanjuice` or `!!sleep @beanbeanjuice :O`";
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
