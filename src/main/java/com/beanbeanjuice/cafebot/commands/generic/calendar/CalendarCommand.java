package com.beanbeanjuice.cafebot.commands.generic.calendar;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import net.dv8tion.jda.api.Permission;

public class CalendarCommand extends Command implements ICommand {

    public CalendarCommand(CafeBot bot) {
        super(bot);
    }

    @Override
    public String getName() {
        return "calendar";
    }

    @Override
    public String getDescriptionPath() {
        return "All things to do with calendars!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.GENERIC;
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
        return true;
    }

    @Override
    public ISubCommand[] getSubCommands() {
        return new ISubCommand[] {
                new CalendarGetSubCommand(bot),
                new CalendarListSubCommand(bot),
                new CalendarAddSubCommand(bot),
                new CalendarDeleteSubCommand(bot)
        };
    }

}
