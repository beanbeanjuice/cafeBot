package com.beanbeanjuice.cafebot.command.fun.rate;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.command.ISubCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to rate people.
 *
 * @author beanbeanjuice
 * @since v3.1.0
 */
public class RateCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) { }

    @NotNull
    @Override
    public String getDescription() {
        return "Rate someone!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/rate simp` or `/rate insane @beanbeanjuice`";
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.FUN;
    }

    @NotNull
    @Override
    public ArrayList<ISubCommand> getSubCommands() {
        ArrayList<ISubCommand> subCommands = new ArrayList<>();
        subCommands.add(new RateSimpSubCommand());
        subCommands.add(new RateGaySubCommand());
        subCommands.add(new RateSmartSubCommand());
        subCommands.add(new RatePoorSubCommand());
        subCommands.add(new RateInsaneSubCommand());
        return subCommands;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return true;
    }

}
