package com.beanbeanjuice.command.interaction;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.section.interaction.Interaction;
import com.beanbeanjuice.cafeapi.cafebot.interactions.InteractionType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to lick people.
 *
 * @author beanbeanjuice
 */
public class LickCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.LICK,
                "**{sender}** *licked* themselves! You at least washed whatever you licked right?",
                "**{sender}** *licked* **{receiver}**! :flushed:",
                "{sender} licked others {amount_sent} times. {receiver} was licked {amount_received} times.",
                "That's unhygienic... <:stab_u:886216384864997406>",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Lick someone.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/lick` or `/lick @beanbeanjuice HAI`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to lick.", false, false));
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
