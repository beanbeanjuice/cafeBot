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

public class BonkCommand extends Command implements ICommand, IInteractionCommand {

    public BonkCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        this.handleInteraction(InteractionType.BONK, event, bot);
    }

    @Override
    public String getName() {
        return "bonk";
    }

    @Override
    public String getDescriptionPath() {
        return "Bonk someone!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.INTERACTION;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The person you want to bonk!"),
                new OptionData(OptionType.STRING, "message", "An optional message to send!")
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
        return "%s just... **bonked** themself. <:disgusted:1257142116539301909>";
    }

    @Override
    public String getOtherString() {
        return "%s just **bonked** %s?! <:gasp:1257142832230170674>";
    }

    @Override
    public String getBotString() {
        return "Ow! Th- that.. that hurt! <:cafeBot_sad:1171726165040447518>";
    }

    @Override
    public String getFooterString() {
        return "%s bonked others %d times. %s was bonked %d times.";
    }

}
