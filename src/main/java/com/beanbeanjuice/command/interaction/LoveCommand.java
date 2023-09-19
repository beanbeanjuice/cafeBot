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

public class LoveCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        new Interaction(InteractionType.LOVE,
                "**{sender}** *loves* themselves! Narcissism much?",
                "**{sender}** *loves* **{receiver}**! Awww 🥰",
                "{sender} loved others {amount_sent} times. {receiver} was loved {amount_received} times.",
                "Y- you love me? 😳",
                event);
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Show someone or something that you love them!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/love` or `/love @beanbeanjuice` or `/love @beanbeanjuice ILY`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "receiver", "The person to show your love toward.", false, false));
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
