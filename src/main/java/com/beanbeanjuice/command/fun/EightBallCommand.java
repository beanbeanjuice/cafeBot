package com.beanbeanjuice.command.fun;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used for asking yes or no questions.
 *
 * @author beanbeanjuice
 */
public class EightBallCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(
                getAnswerEmbed(event.getOption("question").getAsString(), getAnswer())
        ).queue();
    }

    @NotNull
    private MessageEmbed getAnswerEmbed(@NotNull String question, @NotNull String answer) {
        return new EmbedBuilder()
                .setDescription("\"" + question + "\"")
                .addField("My Mystical Answer", "\"" + answer + "\"", false)
                .setColor(Helper.getRandomColor())
                .build();
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
        yesAnswers.add("Hmmm... I think so.");

        ArrayList<String> noAnswers = new ArrayList<>();
        noAnswers.add("Of course not.");
        noAnswers.add("Probably not.");
        noAnswers.add("It is not likely...");
        noAnswers.add("There is some doubt...");
        noAnswers.add("Less than likely.");
        noAnswers.add("ABSOLUTELY NOT.");
        noAnswers.add("Are you kidding? No!");

        if (Helper.getRandomNumber(0, 2) == 1)
            return yesAnswers.get(Helper.getRandomNumber(0, yesAnswers.size()));
        else
            return noAnswers.get(Helper.getRandomNumber(0, noAnswers.size()));
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Ask the 8 ball a yes or no question!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/8ball Will I ever be real?`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "question", "The question you want to ask the magic 8 ball!", true));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.FUN;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return true;
    }

}
