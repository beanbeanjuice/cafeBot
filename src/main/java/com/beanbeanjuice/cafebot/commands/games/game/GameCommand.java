package com.beanbeanjuice.cafebot.commands.games.game;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import net.dv8tion.jda.api.Permission;

public class GameCommand extends Command implements ICommand {

    public GameCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public String getName() {
        return "game";
    }

    @Override
    public String getDescription() {
        return "Something to do with games!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.GAME;
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
                new GameStatsSubCommand(bot)
        };
    }

}
