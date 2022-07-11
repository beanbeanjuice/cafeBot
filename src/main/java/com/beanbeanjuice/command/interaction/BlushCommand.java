package com.beanbeanjuice.command.interaction;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.section.interaction.Interaction;
import io.github.beanbeanjuice.cafeapi.cafebot.interactions.InteractionType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to blush at people.
 *
 * @author beanbeanjuice
 */
public class BlushCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.BLUSH,
                "**{sender}** *blushed*! How cute!~",
                "**{sender}** *blushed* at **{receiver}**!",
                "{sender} blushed at others {amount_sent} times. {receiver} was blushed at {amount_received} times.",
                "Aww you're blushing at me? <:flushed_open:841922879465455646>",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Blush at someone.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/blush` or `/blush @beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to blush at.", false, false));
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
        return false;
    }

}
