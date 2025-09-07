package com.beanbeanjuice.cafebot.utility.commands;

import com.beanbeanjuice.cafebot.CafeBot;

public abstract class Command {

    protected final CafeBot bot;

    public Command(final CafeBot bot) {
        this.bot = bot;
    }

}
