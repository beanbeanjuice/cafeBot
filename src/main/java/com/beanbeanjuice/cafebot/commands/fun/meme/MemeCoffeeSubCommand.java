package com.beanbeanjuice.cafebot.commands.fun.meme;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;

public class MemeCoffeeSubCommand extends Command implements IMemeSubCommand {

    public MemeCoffeeSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public String[] getSubreddits() {
        return new String[] {
                "coffeeporn",
                "coffeememes",
                "coffeewithaview"
        };
    }

    @Override
    public String getName() {
        return "coffee";
    }

    @Override
    public String getDescriptionPath() {
        return "command.meme.subcommand.coffee.description";
    }

}
