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
 * An {@link ICommand} used to nom at someone!
 *
 * @author beanbeanjuice
 */
public class NomCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.NOM,
                "**{sender}** *nommed* themselves! DOESN'T THAT HURT? <:zerotwo_scream:841921420904497163>",
                "**{sender}** *nommed* **{receiver}**!",
                "{sender} nommed others {amount_sent} times. {receiver} was nommed {amount_received} times.",
                "THAT HURTS <:madison_when_short:843673314990882836>",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Nom someone.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/nom` or `/nom @beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to nom.", false, false));
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
