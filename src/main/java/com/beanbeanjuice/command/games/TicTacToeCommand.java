package com.beanbeanjuice.command.games;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.sections.games.tictactoe.TicTacToeGame;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to play Tic-Tac-Toe.
 *
 * @author beanbeanjuice
 */
public class TicTacToeCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        TicTacToeGame game = new TicTacToeGame(user, CafeBot.getGeneralHelper().getUser(args.get(0)));
        if (!CafeBot.getTicTacToeHandler().createGame(event.getGuild().getId(), game)) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                    "Error Creating Tic-Tac-Toe Game",
                    "There is already an active tic tac toe game on this server. Please wait for it to end."
            )).queue();
        }
    }

    @Override
    public String getName() {
        return "tic-tac-toe";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("tictactoe");
        arrayList.add("ttt");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Play a tic tac toe game!";
    }

    @Override
    public String exampleUsage() {
        return "`!!ttt @beanbeanjuice`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.USER, "Opponent Mention", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.GAMES;
    }

}
