package com.beanbeanjuice.command.games;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.sections.games.MiniGame;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to get {@link MiniGame} win streaks for a {@link User}.
 *
 * @author beanbeanjuice
 */
public class GetGameDataCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        if (args.size() == 0) {
            event.getChannel().sendMessageEmbeds(gameDataEmbed(user)).queue();
        } else {
            event.getChannel().sendMessageEmbeds(gameDataEmbed(CafeBot.getGeneralHelper().getUser(args.get(0)))).queue();
        }
    }

    @NotNull
    private MessageEmbed gameDataEmbed(@NotNull User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(user.getName() + "'s Game Win Streaks");
        StringBuilder descriptionBuilder = new StringBuilder();

        for (MiniGame miniGame : MiniGame.values()) {
            descriptionBuilder.append("**").append(miniGame.getName()).append("**: ");
            descriptionBuilder.append("*").append(CafeBot.getWinStreakHandler().getUserWinStreak(user.getId(), miniGame)).append("*\n");
        }

        embedBuilder.setDescription(descriptionBuilder.toString());
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "get-game-data";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("getgamedata");
        arrayList.add("get-gamedata");
        arrayList.add("getgame-data");
        arrayList.add("game-win-streaks");
        arrayList.add("gamewinstreaks");
        arrayList.add("win-streaks");
        arrayList.add("winstreaks");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Get your game data! This shows your win streaks for games that have winstreaks enabled!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "get-game-data` or `" + prefix + "get-game-data @beanbeanjuice`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.USER, "Discord Mention", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.GAMES;
    }
}
