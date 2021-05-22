package com.beanbeanjuice.command.games;

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
 * An {@link ICommand} used for rolling dice.
 *
 * @author beanbeanjuice
 */
public class DiceRollCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        System.out.println(Integer.parseInt(args.get(0)));
        event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                "Dice Roll!",
                "You rolled a `" + CafeBot.getGeneralHelper().getRandomNumber(1, Integer.parseInt(args.get(0)) + 1) + "`."
        )).queue();
    }

    @Override
    public String getName() {
        return "dice-roll";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("diceroll");
        arrayList.add("roll");
        arrayList.add("dice");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Roll a dice between 1 and the specified number.";
    }

    @Override
    public String exampleUsage() {
        return "`!!roll 200`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.NUMBER, "What number?", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.GAMES;
    }

}
