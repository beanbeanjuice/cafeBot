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
 * An {@link ICommand} used to cry at people.
 *
 * @author beanbeanjuice
 */
public class CryCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.CRY,
                "**{sender}** *is crying*! Oh no! Someone come give them a hug <:stab_u:886216384864997406>...",
                "**{sender}** *is crying* at **{receiver}**! **{receiver}**, why are they crying?",
                "{sender} cried at others {amount_sent} times. {receiver} caused people to cry {amount_received} times.",
                "How did I make you cry? <a:catpats:950514533875720232>",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Cry at someone.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/cry` or `/cry @beanbeanjuice WHY`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to cry at.", false, false));
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
        return false;
    }

}
