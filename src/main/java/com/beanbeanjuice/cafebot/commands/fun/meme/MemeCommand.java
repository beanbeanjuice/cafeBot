package com.beanbeanjuice.cafebot.commands.fun.meme;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import net.dv8tion.jda.api.Permission;

public class MemeCommand extends Command implements ICommand {

    public MemeCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public String getName() {
        return "meme";
    }

    @Override
    public String getDescription() {
        return "Get a meme!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[] {
                Permission.MESSAGE_SEND
        };
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
                new MemeCoffeeSubCommand(bot),
                new MemeRandomSubCommand(bot),
                new MemeTeaSubCommand(bot),
        };
    }

}
