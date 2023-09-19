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

public class NosebleedCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.NOSEBLEED,
                "**{sender}** is having a nosebleed!",
                "**{receiver}** caused **{sender}** to have a nosebleed!",
                "{sender} had {amount_sent} nosebleeds. {receiver} gave others nosebleeds {amount_received} times.",
                "Y- you're having a nosebleed! 😱 Here! Have a tissue...",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Have a nosebleed.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/nosebleed` or `/nosebleed @beanbeanjuice` or `/nosebleed @beanbeanjuice YOU CAUSED THIS!`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person who caused your nosebleed.", false, false));
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
