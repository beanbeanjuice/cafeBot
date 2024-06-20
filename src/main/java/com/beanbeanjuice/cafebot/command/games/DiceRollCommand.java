package com.beanbeanjuice.cafebot.command.games;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used for rolling dice.
 *
 * @author beanbeanjuice
 */
public class DiceRollCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        int number = event.getOption("number").getAsInt();

        event.getHook().sendMessageEmbeds(Helper.successEmbed(
                "Dice Roll!",
                "You rolled a `" + Helper.getRandomNumber(1, number + 1) + "`."
        )).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Roll a dice!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/dice-roll` or `/dice-roll 100`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.INTEGER, "number", "Any number.", true)
                .setMinValue(6));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.GAMES;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return true;
    }

}
