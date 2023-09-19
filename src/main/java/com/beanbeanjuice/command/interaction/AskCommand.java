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
 * An {@link ICommand} used to ask questions!
 *
 * @author beanbeanjuice
 */
public class AskCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.ASK,
                "**{sender}** is asking a question!",
                "**{sender}** is asking **{receiver}** a question!",
                "{sender} asked {amount_sent} questions. {receiver} was questioned {amount_received} times.",
                "Umm... I don't wanna answer any questions... 😡",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Ask something!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/ask` or `/ask @beanbeanjuice` or `/ask @beanbeanjuice How is your day?`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to ask the question toward.", false, false));
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
