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
 * An {@link ICommand} used for dabbing at people.
 *
 * @author beanbeanjuice
 */
public class DabCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.DAB,
                "**{sender}** *dabbed*! Umm... this isn't 2016. <:madison_moment:843672933176311808>",
                "**{sender}** *dabbed* at **{receiver}**! <:madison_moment:843672933176311808>",
                "{sender} dabbed at others {amount_sent} times. {receiver} was dabbed at {amount_received} times.",
                "STOP DABBING <:madison_when_short:843673314990882836>",
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
