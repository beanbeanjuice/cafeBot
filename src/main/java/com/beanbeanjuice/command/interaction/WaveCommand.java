package com.beanbeanjuice.command.interaction;

import com.beanbeanjuice.cafeapi.cafebot.interactions.InteractionType;
import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.section.interaction.Interaction;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to wave at someone!
 *
 * @author beanbeanjuice
 */
public class WaveCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.WAVE,
                "**{sender}** *waved* at themselves! Ummm.. okay...",
                "**{sender}** *waved* at **{receiver}**!",
                "{sender} waved at others {amount_sent} times. {receiver} was waved at {amount_received} times.",
                "Hello! 👋",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Wave to someone!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/wave` or `/wave @beanbeanjuice` or `/wave @beanbeanjuice HI`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to wave at.", false, false));
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
