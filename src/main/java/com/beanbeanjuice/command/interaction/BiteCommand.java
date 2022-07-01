package com.beanbeanjuice.command.interaction;

import com.beanbeanjuice.utility.section.interaction.Interaction;
import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.CommandOption;
import com.beanbeanjuice.utility.command.ICommand;
import io.github.beanbeanjuice.cafeapi.cafebot.interactions.InteractionType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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
    public ArrayList<CommandOption> getOptions() {
        ArrayList<CommandOption> options = new ArrayList<>();
        options.add(new CommandOption(OptionType.USER, "receiver", "The person to bite.", false, false));
        options.add(new CommandOption(OptionType.STRING, "message", "An optional message to add.", false, false));
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
