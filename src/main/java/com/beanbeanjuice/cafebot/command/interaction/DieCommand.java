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
 * An {@link ICommand} used to die.
 *
 * @author beanbeanjuice
 */
public class DieCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.DIE,
                "**{sender}** *died*! SOMEONE HELP! <:madison_when_short:843673314990882836>",
                "**{sender}** *died* because of **{receiver}**! <:madison_when_short:843673314990882836>",
                "{sender} died {amount_sent} times. {receiver} was someone's cause of death {amount_received} times.",
                "You're not dead... <:madison_moment:843672933176311808>",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Die with someone.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/die` or `/die @beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to die toward.", false, false));
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
