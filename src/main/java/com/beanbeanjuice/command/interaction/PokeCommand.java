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
 * An {@link ICommand} used to poke people.
 *
 * @author beanbeanjuice
 */
public class PokeCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.POKE,
                "**{sender}** *poked* themselves! Why?",
                "**{sender}** *poked* **{receiver}**!",
                "{sender} poked others {amount_sent} times. {receiver} was poked {amount_received} times.",
                "Hmm? Is there something you need? <:kuromi_question:841921649132568576>",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Poke someone.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/poke` or `/poke @beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to poke.", false, false));
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
