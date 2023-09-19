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
 * An {@link ICommand} used to UwU at people.
 *
 * @author beanbeanjuice
 */
public class UwUCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.UWU,
                "**{sender}** is being cute!",
                "**{receiver}** caused **{sender}** to UwU! How cute!!",
                "{sender} did an UwU {amount_sent} times. {receiver} caused {amount_received} UwUs.",
                "So cute... <a:catpats:950514533875720232>",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "UwU at someone.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/uwu` or `/uwu @beanbeanjuice` or `/uwu @beanbeanjuice owo`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to uwu at.", false, false));
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
