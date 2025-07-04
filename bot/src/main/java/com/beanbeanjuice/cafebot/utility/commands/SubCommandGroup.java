package com.beanbeanjuice.cafebot.utility.commands;

import lombok.Getter;

public class SubCommandGroup {

    @Getter private String name;
    @Getter private String description;
    @Getter private ISubCommand[] subCommands;

    public SubCommandGroup(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    public void addSubCommands(final ISubCommand[] subCommands) {
        this.subCommands = subCommands;
    }

}
