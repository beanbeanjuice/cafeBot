package com.beanbeanjuice.cafebot.command.interaction;

import com.beanbeanjuice.cafeapi.wrapper.cafebot.interactions.InteractionType;
import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.section.interaction.Interaction;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to wink at someone!
 *
 * @author beanbeanjuice
 */
public class WinkCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.WINK,
                "**{sender}** *winked* at themselves! Okay... weird...",
                "**{sender}** *winked* at **{receiver}**! 🫢",
                "{sender} winked at others {amount_sent} times. {receiver} was winked at {amount_received} times.",
                "https://cdn.beanbeanjuice.com/images/cafeBot/cafeBot.gif",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Wink at someone!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/wink` or `/wink @beanbeanjuice` or `/wink @beanbeanjuice hi`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to wink at.", false, false));
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
