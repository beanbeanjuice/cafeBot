package com.beanbeanjuice.cafebot.commands.interaction;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.InteractionType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class PunchCommand extends Command implements ICommand, IInteractionCommand {

    public PunchCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        this.handleInteraction(InteractionType.PUNCH, event, bot);
    }

    @Override
    public String getName() {
        return "punch";
    }

    @Override
    public String getDescriptionPath() {
        return "Punch someone!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.INTERACTION;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The user you want to punch."),
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
        return "%s **punched** themself?! WHY?! <:zerotwo_scream2:843673314990882836>";
    }

    @Override
    public String getOtherString() {
        return "%s **punched** %s!? DON'T FIGHT! <:zerotwo_scream2:843673314990882836>";
    }

    @Override
    public String getBotString() {
        return "Well.. I'm made of metal so... your fist is broken. <:stab_u:886216384864997406>";
    }

    @Override
    public String getFooterString() {
        return "%s punched others %d times. %s was punched %d times.";
    }

}
