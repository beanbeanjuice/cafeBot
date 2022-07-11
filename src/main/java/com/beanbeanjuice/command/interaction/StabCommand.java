package com.beanbeanjuice.command.interaction;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.section.interaction.Interaction;
import io.github.beanbeanjuice.cafeapi.cafebot.interactions.InteractionType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to stab people.
 *
 * @author beanbeanjuice
 */
public class StabCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.STAB,
                "**{sender}** *stabbed* themselves! STOP??? DON'T DO THAT IN THE CAFE?!? <:madison_pissed:842061821774004304>",
                "**{sender}** *stabbed* **{receiver}**!",
                "{sender} stabbed others {amount_sent} times. {receiver} was stabbed at {amount_received} times.",
                "I'm a robot. Stabbing me does nothing. But, you're not a robot. <:stab_u:886216384864997406>",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Stab someone. CALM DOWN!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/stab` or `/stab @beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to stab.", false, false));
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
