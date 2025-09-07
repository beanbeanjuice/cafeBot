package com.beanbeanjuice.cafebot.commands.fun.meme;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;

public class MemeRandomSubCommand extends Command implements IMemeSubCommand {

    public MemeRandomSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public String[] getSubreddits() {
        return new String[] {
                "memes",
                "dankmemes",
                "me_irl",
                "prequelmemes",
                "thathappened"
        };
    }

    @Override
    public String getName() {
        return "random";
    }

    @Override
    public String getDescription() {
        return "Get a random meme!";
    }

}
