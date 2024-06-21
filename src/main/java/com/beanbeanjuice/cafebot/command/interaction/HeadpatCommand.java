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
 * An {@link ICommand} used to give headpats to people.
 *
 * @author beanbeanjuice
 */
public class HeadpatCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.HEADPAT,
                "**{sender}** *headpat* themselves! Ummm... what?",
                "**{sender}** *headpat* **{receiver}**! <:madison_when_short:843673314990882836>",
                "{sender} headpat others {amount_sent} times. {receiver} was headpat {amount_received} times.",
                "Thank you... \uD83E\uDD7A",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Headpat someone.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/headpat` or `/headpat @beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to give headpats to.", false, false));
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
