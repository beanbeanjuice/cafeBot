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

public class BiteCommand extends Command implements ICommand, IInteractionCommand {

    public BiteCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        this.handleInteraction(InteractionType.BITE, event, bot, ctx.getDefaultBundle());
    }

    @Override
    public String getName() {
        return "bite";
    }

    @Override
    public String getDescriptionPath() {
        return "command.interaction.bite.description";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.INTERACTION;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "command.interaction.bite.arguments.user.description"),
                new OptionData(OptionType.STRING, "message", "command.interaction.common.arguments.message.description")
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
        return "command.interaction.bite.self";
    }

    @Override
    public String getOtherString() {
        return "command.interaction.bite.other";
    }

    @Override
    public String getBotString() {
        return "command.interaction.bite.bot";
    }

    @Override
    public String getFooterString() {
        return "command.interaction.bite.footer";
    }

}
