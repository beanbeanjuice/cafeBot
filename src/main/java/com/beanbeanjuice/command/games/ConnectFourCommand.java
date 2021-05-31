package com.beanbeanjuice.command.games;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.sections.games.connectfour.ConnectFourGame;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * An {@link ICommand} used for Connect Four games.
 *
 * @author beanbeanjuice
 */
public class ConnectFourCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        User player1 = user;
        User player2 = CafeBot.getGeneralHelper().getUser(args.get(0));

        if (player1.equals(player2)) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                    "Cannot Play Yourself",
                    "You cannot play a connect four game against yourself!"
            )).queue();
            return;
        }

        if (player2.isBot()) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                    "Cannot Play Against Bot",
                    "You cannot play this game against a bot!"
            )).queue();
            return;
        }

        ConnectFourGame game = new ConnectFourGame(player1, player2, event.getChannel());
        if (!CafeBot.getConnectFourHandler().createGame(event.getGuild().getId(), game)) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                    "Error Creating Connect Four Game",
                    "There is already an active connect four game on this server. Please wait for it to end."
            )).queue();
        }
    }

    @Override
    public String getName() {
        return "connect-four";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("connectfour");
        arrayList.add("connect-4");
        arrayList.add("connect4");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Play connect four with someone!";
    }

    @Override
    public String exampleUsage() {
        return "`!!connect4 @beanbeanjuice`";
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
