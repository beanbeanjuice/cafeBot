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

public class KissCommand extends Command implements ICommand, IInteractionCommand {

    public KissCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        this.handleInteraction(InteractionType.KISS, event, bot, ctx.getDefaultBundle());
    }

    @Override
    public String getName() {
        return "kiss";
    }

    @Override
    public String getDescriptionPath() {
        return "command.interaction.kiss.description";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.INTERACTION;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "command.interaction.kiss.arguments.user.description"),
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
        return true;
    }

    @Override
    public boolean allowDM() {
        return true;
    }

    @Override
    public String getSelfString() {
        return "command.interaction.kiss.self";
    }

    @Override
    public String getOtherString() {
        return "command.interaction.kiss.other";
    }

    @Override
    public String getBotString() {
        return "command.interaction.kiss.bot";
    }

    @Override
    public String getFooterString() {
        return "command.interaction.kiss.footer";
    }

}
