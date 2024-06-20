package com.beanbeanjuice.cafebot.command.fun;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to choose a random variable.
 *
 * @author beanbeanjuice
 */
public class ChooseRandom implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        String[] variables = Helper.removeCommaSpace(event.getOption("choices").getAsString());  // Will not be null.

        int randomNum = Helper.getRandomNumber(0, variables.length);
        String chosenVariable = variables[randomNum];

        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Options: ```");

        for (int i = 0; i < variables.length; i++) {
            messageBuilder.append(variables[i]);

            if (i != variables.length - 1) messageBuilder.append(", ");
        }

        messageBuilder.append("```\n\n🎲 (").append(randomNum + 1).append("/").append(variables.length).append("): ")
                .append("**").append(chosenVariable).append("**");

        event.getHook().sendMessageEmbeds(
                Helper.successEmbed(
                        "Random Variable Chooser",
                        messageBuilder.toString()
                )
        ).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Choose a random variable!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/choose-random bacon,lettuce,pineapple`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "choices", "The random variables to choose from, separated by commas.", true));
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
