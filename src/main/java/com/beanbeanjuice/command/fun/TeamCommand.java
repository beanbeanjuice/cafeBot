package com.beanbeanjuice.command.fun;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to choose/make teams.
 *
 * @author beanbeanjuice
 */
public class TeamCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {

        // TODO: Default - Any amount of users.
        // TODO: Allow infinite players if no player amount specified.
        // TODO: Default - 2 teams

    }

    @NotNull
    @Override
    public String getDescription() {
        return null;
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/team` or `/team 5` or `/team 5 2`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();

        options.add(new OptionData(OptionType.INTEGER, "players", "The amount of players per team.", false));
        options.add(new OptionData(OptionType.INTEGER, "teams", "The number of teams.", false));

        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.FUN;
    }

}
