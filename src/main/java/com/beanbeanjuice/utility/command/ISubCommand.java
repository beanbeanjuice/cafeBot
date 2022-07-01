package com.beanbeanjuice.utility.command;

import org.jetbrains.annotations.NotNull;

public interface ISubCommand extends ICommand {

    @NotNull
    public String getName();

}
