package com.beanbeanjuice.cafebot.commands.interaction;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.InteractionType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.sections.interactions.ICommandInteraction;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class AskCommand extends Command implements ICommand, ICommandInteraction {

    public AskCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        this.handleInteraction(InteractionType.ASK, event, cafeBot);
    }

    @Override
    public String getName() {
        return "ask";
    }

    @Override
    public String getDescription() {
        return "Ask someone something!";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The person who you want to ask a question to."),
                new OptionData(OptionType.STRING, "message", "An optional message you want to add!")
        };
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[0];
    }

    @Override
    public boolean isEphemeral() {
        return false;
    }

    @Override
    public boolean isNSFW() {
        return false;
    }

    @Override
    public boolean allowDM() {
        return true;
    }

    @Override
    public String getSelfString() {
        return "%s just... **asked** themself a question? Okay then... 🙄";
    }

    @Override
    public String getOtherString() {
        return "%s **asked** %s a question! Hopefully they answer... 🥺";
    }

    @Override
    public String getBotString() {
        return "Hmm? What is it? <:kuromi_question:841921649132568576>";
    }

    @Override
    public String getFooterString() {
        return "%s asked someone something %d times. %s was asked something %d times.";
    }

}
