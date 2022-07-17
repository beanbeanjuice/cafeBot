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
 * An {@link ICommand} used to stare at people.
 *
 * @author beanbeanjuice
 */
public class StareCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.STARE,
                "**{sender}** *stared* at themselves! Ummm.. how?",
                "**{sender}** *stared* at **{receiver}**!",
                "{sender} stared at others {amount_sent} times. {receiver} was stared at {amount_received} times.",
                "S- stop staring... I get nervous when you stare... <:flushed_nervous:841923862202548224>",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Stare at someone. *stare*";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/stare` or `/stare @beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to stare at.", false, false));
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
