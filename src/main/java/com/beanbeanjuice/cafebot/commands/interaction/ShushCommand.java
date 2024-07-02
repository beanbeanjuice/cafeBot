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

public class ShushCommand extends Command implements ICommand, ICommandInteraction {

    public ShushCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        this.handleInteraction(InteractionType.SHUSH, event, cafeBot);
    }

    @Override
    public String getName() {
        return "shush";
    }

    @Override
    public String getDescription() {
        return "Shush someone!";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The user you want to shush."),
                new OptionData(OptionType.STRING, "message", "An optional message you can send.")
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
        return "%s just **shushed** themself...";
    }

    @Override
    public String getOtherString() {
        return "%s just **shushed** %s...";
    }

    @Override
    public String getBotString() {
        return "Don't you *dare* tell me to be quiet. <:moon_stare:1165826168428183562>";
    }

    @Override
    public String getFooterString() {
        return "%s shushed others %d times. %s was shushed %d times.";
    }

}
