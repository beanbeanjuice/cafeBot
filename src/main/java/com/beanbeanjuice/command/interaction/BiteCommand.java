package com.beanbeanjuice.command.interaction;

import com.beanbeanjuice.utility.section.interaction.Interaction;
import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import io.github.beanbeanjuice.cafeapi.cafebot.interactions.InteractionType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to bite people!
 *
 * @author beanbeanjuice
 */
public class BiteCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.BITE,
                "**{sender}** *bit* themselves! Ow!",
                "**{sender}** *bit* **{receiver}**! What did they do?!?!?!?",
                "{sender} bit others {amount_sent} times. {receiver} was bitten {amount_received} times.",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Bite someone or something!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/bite @beanbeanjuice HA!` or `/bite OW` or `/bite`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to bite.", false, false));
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
