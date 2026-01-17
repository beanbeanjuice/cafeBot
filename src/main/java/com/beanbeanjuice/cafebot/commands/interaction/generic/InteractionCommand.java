package com.beanbeanjuice.cafebot.commands.interaction.generic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import net.dv8tion.jda.api.Permission;

public class InteractionCommand extends Command implements ICommand {

    public InteractionCommand(CafeBot bot) {
        super(bot);
    }

    @Override
    public String getName() {
        return "interaction";
    }

    @Override
    public String getDescription() {
        return "Block, unblock, and enable/disable interactions!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.INTERACTION;
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[0];
    }

    @Override
    public boolean isEphemeral() {
        return true;
    }

    @Override
    public boolean isNSFW() {
        return false;
    }

    @Override
    public boolean allowDM() {
        return false;
    }

    @Override
    public ISubCommand[] getSubCommands() {
        return new ISubCommand[] {
                new InteractionStatusSubCommand(bot),
                new InteractionBlockSubCommand(bot),
                new InteractionUnblockSubCommand(bot)
        };
    }

}
