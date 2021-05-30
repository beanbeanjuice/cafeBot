package com.beanbeanjuice.command.games;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used for asking yes or no questions.
 *
 * @author beanbeanjuice
 */
public class EightBallCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                "8 Ball",
                getAnswer()
        )).queue();
    }

    @NotNull
    private String getAnswer() {
        ArrayList<String> yesAnswers = new ArrayList<>();
        yesAnswers.add("It is likely...");
        yesAnswers.add("Probably.");
        yesAnswers.add("Without a doubt.");
        yesAnswers.add("Of course.");
        yesAnswers.add("More than likely.");
        yesAnswers.add("YES ABSOLUTELY.");

        ArrayList<String> noAnswers = new ArrayList<>();
        noAnswers.add("Of course not.");
        noAnswers.add("Probably not.");
        noAnswers.add("It is not likely...");
        noAnswers.add("There is some doubt...");
        noAnswers.add("Less than likely.");
        noAnswers.add("ABSOLUTELY NOT.");

        if (CafeBot.getGeneralHelper().getRandomNumber(1, 3) == 1) {
            return yesAnswers.get(CafeBot.getGeneralHelper().getRandomNumber(0, yesAnswers.size()));
        } else {
            return noAnswers.get(CafeBot.getGeneralHelper().getRandomNumber(0, noAnswers.size()));
        }
    }

    @Override
    public String getName() {
        return "8-ball";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("8ball");
        arrayList.add("eight-ball");
        arrayList.add("eightball");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Ask a yes or no question!";
    }

    @Override
    public String exampleUsage() {
        return "`!!8ball Am I going to win a million dollars today?`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.SENTENCE, "A yes or no question.", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.GAMES;
    }

}
