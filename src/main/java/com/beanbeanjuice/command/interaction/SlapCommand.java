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
 * An {@link ICommand} used to slap people.
 *
 * @author beanbeanjuice
 */
public class SlapCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.SLAP,
                "**{sender}** *slapped* themselves! DON'T DO THAT! <a:man_scream:841921434732724224>",
                "**{sender}** *slapped* **{receiver}**!",
                "{sender} slapped others {amount_sent} times. {receiver} was slapped {amount_received} times.",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Slap someone.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/slap` or `/slap @beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<CommandOption> getOptions() {
        ArrayList<CommandOption> options = new ArrayList<>();
        options.add(new CommandOption(OptionType.USER, "receiver", "The person to slap.", false, false));
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
