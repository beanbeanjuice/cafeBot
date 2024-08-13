package com.beanbeanjuice.cafebot.utility.commands;

import com.beanbeanjuice.cafebot.CafeBot;

public abstract class Command {

    protected final CafeBot cafeBot;

    public Command(final CafeBot cafeBot) {
        this.cafeBot = cafeBot;
    }

}
