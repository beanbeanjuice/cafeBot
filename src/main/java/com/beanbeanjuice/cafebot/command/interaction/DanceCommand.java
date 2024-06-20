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
 * An {@link ICommand} used for dancing.
 *
 * @author beanbeanjuice
 */
public class DanceCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.DANCE,
                "**{sender}** *danced*! How cute!~ <a:wiggle:886217792578269236>",
                "**{sender}** *danced* with **{receiver}**! <a:wiggle:886217792578269236>",
                "{sender} danced with others {amount_sent} times. {receiver} was danced with {amount_received} times.",
                "Let's dance together uwu <a:wiggle:886217792578269236>",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Dance with someone.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/dance` or `/dance @beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to dance with.", false, false));
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
