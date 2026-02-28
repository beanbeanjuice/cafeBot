package com.beanbeanjuice.cafebot.commands.fun.meme;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;

public class MemeTeaSubCommand extends Command implements IMemeSubCommand {

    public MemeTeaSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public String[] getSubreddits() {
        return new String[] {
                "teaporn",
                "teapictures"
        };
    }

    @Override
    public String getName() {
        return "tea";
    }

    @Override
    public String getDescriptionPath() {
        return "command.meme.subcommand.tea.description";
    }

}
