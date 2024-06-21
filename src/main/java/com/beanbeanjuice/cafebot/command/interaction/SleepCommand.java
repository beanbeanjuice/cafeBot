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
 * An {@link ICommand} used to sleep.
 *
 * @author beanbeanjuice
 */
public class SleepCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.SLEEP,
                "**{sender}** *is sleeping*! Anyone wanna join in? <:kuromi_question:841921649132568576>",
                "**{sender}** *is sleeping* with **{receiver}**!",
                "{sender} slept with others {amount_sent} times. {receiver} was slept with {amount_received} times.",
                "ZZzz \uD83D\uDE34",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Sleep with someone. owo";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/sleep` or `/sleep @beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to sleep with.", false, false));
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
