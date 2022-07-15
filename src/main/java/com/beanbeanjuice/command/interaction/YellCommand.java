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
 * An {@link ICommand} used to yell at people.
 *
 * @author beanbeanjuice
 */
public class YellCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.YELL,
                "**{sender}** *yelled*! SHUSH... some people are trying to work! <:madison_moment:843672933176311808>",
                "**{sender}** *yelled* at **{receiver}**!",
                "{sender} yelled at others {amount_sent} times. {receiver} was yelled at {amount_received} times.",
                "WHY ARE YOU YELLING AT ME???? <:zerotwo_scream:841921420904497163>",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Yell at someone. Why would you do that?";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/yell` or `/yell @beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to yell at.", false, false));
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
