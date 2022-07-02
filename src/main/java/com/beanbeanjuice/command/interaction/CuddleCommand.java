package com.beanbeanjuice.command.interaction;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.CommandOption;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.section.interaction.Interaction;
import io.github.beanbeanjuice.cafeapi.cafebot.interactions.InteractionType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to cuddle people.
 *
 * @author beanbeanjuice
 */
public class CuddleCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.CUDDLE,
                "**{sender}** *cuddled* themselves! That's... kind of sad... <:madison_moment:843672933176311808>",
                "**{sender}** *cuddled* **{receiver}**! That's adorable! <a:wowowow:886217210010431508>",
                "{sender} cuddled others {amount_sent} times. {receiver} was cuddled {amount_received} times.",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Cuddle someone.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/cry` or `/cry @beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<CommandOption> getOptions() {
        ArrayList<CommandOption> options = new ArrayList<>();
        options.add(new CommandOption(OptionType.USER, "receiver", "The person to cuddle.", false, false));
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
