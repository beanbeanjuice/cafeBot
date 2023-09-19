package com.beanbeanjuice.command.interaction;

import com.beanbeanjuice.cafeapi.cafebot.interactions.InteractionType;
import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.section.interaction.Interaction;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to shoot people (in-game)!
 *
 * @author beanbeanjuice
 */
public class ShootCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.SHOOT,
                "**{sender}** *SHOT* themselves (in-game)!! WH- WHY WOULD YOU DO THAT??",
                "**{sender}** *shot* **{receiver}** (in-game)! SOMEONE STOP THEM!!??",
                "{sender} shot others (in-game) {amount_sent} times. {receiver} was shot (in-game) {amount_received} times.",
                "YOU CAN TRY AND TAKE ME OUT BUT ANOTHER ONE WILL TAKE MY PLACE, I WON'T FORGET EITHER... <:stab_u:886216384864997406>",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Shoot (in-game) someone!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/shoot` or `/shoot @beanbeanjuice` or `/shoot @beanbeanjuice HA!`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to shoot (in-game).", false, false));
        options.add(new OptionData(OptionType.STRING, "message", "An optional message to add.", false, false));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.INTERACTION;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return true;
    }

}
