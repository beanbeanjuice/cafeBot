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
                "Wh- why would you slap me... <:when_AHHH:842062279372701737>",
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
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to slap.", false, false));
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
