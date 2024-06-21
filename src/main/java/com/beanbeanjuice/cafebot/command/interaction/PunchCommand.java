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
 * An {@link ICommand} used for punching.
 *
 * @author beanbeanjuice
 */
public class PunchCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.PUNCH,
                "**{sender}** *punched* themselves! STOP!!! <:madison_when_short:843673314990882836>",
                "**{sender}** *punched* **{receiver}**!",
                "{sender} punched others {amount_sent} times. {receiver} was punched {amount_received} times.",
                "Congratulations... you punched a robot. Your hands are now broken. <:madison_moment:843672933176311808>",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Punch someone... HUH";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/punch` or `/punch @beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to punch.", false, false));
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
