package com.beanbeanjuice.cafebot.commands.interaction;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.InteractionType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class RageCommand extends Command implements ICommand, IInteractionCommand {

    public RageCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        this.handleInteraction(InteractionType.RAGE, event, bot);
    }

    @Override
    public String getName() {
        return "rage";
    }

    @Override
    public String getDescription() {
        return "Rage at someone!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.INTERACTION;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The user you want to rage at."),
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
        return "%s is **raging**! CALM DOWN! <:pissed:842061821774004304>";
    }

    @Override
    public String getOtherString() {
        return "%s is **raging** at %s! What did they do?! <a:man_scream:841921434732724224>";
    }

    @Override
    public String getBotString() {
        return "I'm going to have to ask you to leave. 😊";
    }

    @Override
    public String getFooterString() {
        return "%s raged %d times. %s was raged at %d times.";
    }

}
