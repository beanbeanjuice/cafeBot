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
 * An {@link ICommand} used to tickle people.
 *
 * @author beanbeanjuice
 */
public class TickleCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.TICKLE,
                "**{sender}** *tickled* themselves! Ohhh... okay?",
                "**{sender}** *tickled* **{receiver}**!",
                "{sender} tickled others {amount_sent} times. {receiver} was tickled {amount_received} times.",
                "S- stop it! You know I'm ticklish! <:zerotwo_scream:841921420904497163>",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Tickle someone.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/tickle` or `/tickle @beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to tickle.", false, false));
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
