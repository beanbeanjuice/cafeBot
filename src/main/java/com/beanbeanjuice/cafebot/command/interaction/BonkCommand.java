package com.beanbeanjuice.cafebot.command.interaction;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.section.interaction.Interaction;
import com.beanbeanjuice.cafeapi.wrapper.cafebot.interactions.InteractionType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to bonk people.
 *
 * @author beanbeanjuice
 */
public class BonkCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.BONK,
                "**{sender}** *bonked* themselves! ~~**H O R N I B L O C K**~~",
                "**{sender}** *bonked* **{receiver}**! They sent them to ~~**H O R N I**~~ jail!",
                "{sender} bonked others {amount_sent} times. {receiver} was bonked {amount_received} times.",
                "WHAT DID I DO?!?!? <:madison_pissed:842061821774004304>",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Bonk someone.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/bonk` or `/bonk @beanbeanjuice :O`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to bonk.", false, false));
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
