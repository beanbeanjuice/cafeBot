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

public class AmazedCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.AMAZED,
                "**{sender}** is amazed!",
                "**{sender}** is amazed at **{receiver}**! <a:wowowow:886217210010431508>",
                "{sender} was amazed because of others {amount_sent} times. People thought {receiver} was amazing {amount_received} times.",
                "You think I'm amazing?? 🥺",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Be amazed at someone or something!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/amaze` or `/amaze @beanbeanjuice` or `/amaze @beanbeanjuice hi`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to be amazed at.", false, false));
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
