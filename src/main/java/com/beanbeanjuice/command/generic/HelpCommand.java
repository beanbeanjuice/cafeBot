package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.CommandOption;
import com.beanbeanjuice.utility.command.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HelpCommand implements ICommand {

    @Override
    public void handle(@NotNull ArrayList<OptionMapping> args, @NotNull SlashCommandInteractionEvent event) {
        event.getHook().sendMessage("Worked!").setEphemeral(true).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Command list + how to use the commands.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/help` or `/help (section)` or `/help (command name)`";
    }

    @NotNull
    @Override
    public ArrayList<CommandOption> getOptions() {
        ArrayList<CommandOption> options = new ArrayList<>();
        options.add(new CommandOption(OptionType.STRING, "section-or-command", "Sub-argument for the help command.", false, false));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.GENERIC;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return true;
    }

}
