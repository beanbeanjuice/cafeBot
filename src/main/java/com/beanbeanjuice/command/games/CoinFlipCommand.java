package com.beanbeanjuice.command.games;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * An {@link ICommand} used for flipping coins.
 *
 * @author beanbeanjuice
 */
public class CoinFlipCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        int num = CafeBot.getGeneralHelper().getRandomNumber(1, 3);

        if (num == 1) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                    "Coin Toss!",
                    "Heads"
            )).queue();
            return;
        }

        if (num == 2) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                    "Coin Toss!",
                    "Tails"
            )).queue();
            return;
        }
    }

    @Override
    public String getName() {
        return "coin-flip";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("coinflip");
        arrayList.add("coin-toss");
        arrayList.add("cointoss");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Flip a coin!";
    }

    @Override
    public String exampleUsage() {
        return "`!!coinflip`";
    }

    @Override
    public Usage getUsage() {
        return new Usage();
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.GAMES;
    }

}
