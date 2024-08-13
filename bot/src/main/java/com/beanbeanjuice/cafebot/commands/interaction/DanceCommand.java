package com.beanbeanjuice.cafebot.commands.interaction;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.InteractionType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.sections.interactions.ICommandInteraction;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class DanceCommand extends Command implements ICommand, ICommandInteraction {

    public DanceCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        this.handleInteraction(InteractionType.DANCE, event, cafeBot);
    }

    @Override
    public String getName() {
        return "dance";
    }

    @Override
    public String getDescription() {
        return "Dance with someone!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.INTERACTION;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The user you want to dance with!"),
                new OptionData(OptionType.STRING, "message", "An optional message you want to send.")
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
        return "%s is **dancing** all alone... how pathetic. <:disgusted:1257142116539301909>";
    }

    @Override
    public String getOtherString() {
        return "%s is **dancing** with %s! Everyone join in! <a:wiggle:886217792578269236>";
    }

    @Override
    public String getBotString() {
        return "DANCE PARTY!!! <a:Dance:1140048902301679666><a:Dance:1140048902301679666><a:Dance:1140048902301679666>";
    }

    @Override
    public String getFooterString() {
        return "%s danced %d times. %s was danced with %d times.";
    }

}
