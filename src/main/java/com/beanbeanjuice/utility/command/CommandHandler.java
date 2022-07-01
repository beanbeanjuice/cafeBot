package com.beanbeanjuice.utility.command;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.command.generic.HelpCommand;
import com.beanbeanjuice.command.generic.PingCommand;
import com.beanbeanjuice.command.interaction.BiteCommand;
import com.beanbeanjuice.command.moderation.counting.CountingChannelCommand;
import com.beanbeanjuice.utility.logging.LogLevel;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class CommandHandler extends ListenerAdapter {
    private final TreeMap<String, ICommand> commands;

    public CommandHandler(@NotNull JDA jda) {
        commands = new TreeMap<>();
        List<SlashCommandData> slashCommands = new ArrayList<>();

        /*
            Commands Go Here
         */
        commands.put("ping", new PingCommand());
        commands.put("help", new HelpCommand());

        commands.put("bite", new BiteCommand());

        commands.put("counting-channel", new CountingChannelCommand());

        commands.forEach((commandName, command) -> {
            SlashCommandData slashCommandData = Commands.slash(commandName, command.getDescription());
            slashCommandData.setGuildOnly(!command.allowDM());

            // Checking if options are null and should be skipped
            if (command.getOptions() != null) {
                for (CommandOption option : command.getOptions()) {
                    slashCommandData.addOption(option.getOptionType(), option.getName(), option.getDescription(), option.isRequired(), option.hasAutoComplete());
                }
            }

            List<SubcommandData> subCommands = new ArrayList<>();

            // Checking if sub commands are null and should be skipped
            if (command.getSubCommands() != null) {

                for (ISubCommand subCommand : command.getSubCommands()) {
                    SubcommandData subCommandData = new SubcommandData(subCommand.getName(), subCommand.getDescription());

                    // Checking if the sub commands options are null and should be skipped

                    if (command.getOptions() != null) {
                        for (CommandOption option : command.getOptions()) {
                            subCommandData.addOption(option.getOptionType(), option.getName(), option.getDescription(), option.isRequired(), option.hasAutoComplete());
                        }
                    }
                    subCommands.add(subCommandData);
                }

            }

            // Finally, adding the commands together.
            slashCommandData.addSubcommands(subCommands);
            slashCommands.add(slashCommandData);
        });

        jda.updateCommands().addCommands(slashCommands).queue((e) -> {
            Bot.getLogger().log(this.getClass(), LogLevel.INFO, "Waiting for slash commands to propagate.", false, false);
        });
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        super.onSlashCommandInteraction(event);
        event.deferReply().queue();

        if (commands.containsKey(event.getName())) {
            commands.get(event.getName()).handle(event);
            Bot.commandsRun++;
        }
    }

    @NotNull
    public TreeMap<String, ICommand> getCommands() {
        return commands;
    }
}
