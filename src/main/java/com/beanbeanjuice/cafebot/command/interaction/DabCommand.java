package com.beanbeanjuice.cafebot.command.interaction;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.section.interaction.Interaction;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.InteractionType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used for dabbing at people.
 *
 * @author beanbeanjuice
 */
public class DabCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.DAB,
                "**{sender}** *dabbed*! Umm... this isn't 2016. <:cafeBot_angry:1171726164092518441>",
                "**{sender}** *dabbed* at **{receiver}**! <:cafeBot_angry:1171726164092518441>",
                "{sender} dabbed at others {amount_sent} times. {receiver} was dabbed at {amount_received} times.",
                "STOP DABBING <:zerotwo_scream:841921420904497163>",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Dab at someone.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/dab` or `/dab @beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to dab at.", false, false));
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
