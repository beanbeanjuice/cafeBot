package com.beanbeanjuice.cafebot.commands.fun.rate;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import net.dv8tion.jda.api.Permission;

public class RateCommand extends Command implements ICommand {

    public RateCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public String getName() {
        return "rate";
    }

    @Override
    public String getDescription() {
        return "Rate something!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
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
    public ISubCommand[] getSubCommands() {
        return new ISubCommand[] {
                new RateCaffeinatedSubCommand(cafeBot),
                new RateInsaneSubCommand(cafeBot),
                new RatePoorSubCommand(cafeBot),
                new RateSimpSubCommand(cafeBot),
                new RateSmartSubCommand(cafeBot)
        };
    }
}
