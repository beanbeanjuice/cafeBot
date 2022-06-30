package com.beanbeanjuice.utility.command;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.command.generic.PingCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

public class CommandHandler extends ListenerAdapter {
    private ArrayList<ICommand> commands;

    public CommandHandler(@NotNull JDA jda) {
        commands = new ArrayList<>();

        commands.add(new PingCommand());

        for (ICommand command : commands) {
            CommandCreateAction createCommand = jda.upsertCommand(command.getName(), command.getDescription());
            for (CommandOption option : command.getOptions()) {
                createCommand.addOption(option.getOptionType(), option.getName(), option.getDescription(), option.isRequired(), option.hasAutoComplete());
            }
            createCommand.queue();
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        super.onSlashCommandInteraction(event);
        event.deferReply().queue();

        for (ICommand command : commands) {
             /*
                This checks if the command is runnable.
                This is because if the command is not allowed to be
                run in a direct message, then it sends an error message.
             */
            if (!command.allowDM() && !event.isFromGuild()) {
                event.getHook().sendMessageEmbeds(
                        new EmbedBuilder()
                                .setAuthor("Error!")
                                .setColor(Color.RED)
                                .setDescription("Sorry, this command can only be used in servers!")
                                .build()
                ).setEphemeral(true).queue();
                return;
            }

            if (event.getName().equals(command.getName())) {
                ArrayList<OptionMapping> options = new ArrayList<>(event.getOptions());
                command.handle(options, event);
                Bot.commandsRun++;
                return;
            }
        }
    }
}
