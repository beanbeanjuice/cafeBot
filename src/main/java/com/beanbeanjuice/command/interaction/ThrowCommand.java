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
 * An {@link ICommand} used to throw people.
 *
 * @author beanbeanjuice
 */
public class ThrowCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.THROW,
                "**{sender}** *threw* themselves! I-... how? <:bean_moment:841922879166742529>",
                "**{sender}** *threw* **{receiver}**!",
                "{sender} threw others {amount_sent} times. {receiver} was thrown {amount_received} times.",
                "Umm... I'm a robot... made out of heavy metal... you can't throw me <:really_why:842061369381748756>",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Throw someone. HOW ARE YOU SO STRONG?";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/stare` or `/stare @beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to throw.", false, false));
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
