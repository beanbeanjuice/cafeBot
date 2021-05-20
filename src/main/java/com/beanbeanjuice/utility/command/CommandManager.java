package com.beanbeanjuice.utility.command;

import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.types.CommandErrorType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * A class used for handling {@link ICommand}.
 *
 * @author beanbeanjuice
 */
public class CommandManager {

    private final ArrayList<ICommand> commands;

    /**
     * Creates a new instance of the {@link CommandManager} object.
     */
    public CommandManager() {
        commands = new ArrayList<>();
    }

    /**
     * Adds a new {@link ICommand} to be listened for.
     * @param command The {@link ICommand} to be added.
     */
    public void addCommand(@NotNull ICommand command) {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(command.getName()));

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name already exists.");
        }

        commands.add(command);
    }

    /**
     * Adds a new {@link ICommand} to be listened for.
     * @param commands The {@link ICommand} to be added.
     */
    public void addCommands(@NotNull ICommand... commands) {
        for (ICommand command : commands) {
            addCommand(command);
        }
    }

    /**
     * Gets the current {@link ICommand} that has that alias.
     * @param search The name of the {@link ICommand} to be searched for.
     * @return The {@link ICommand} that contains that.
     */
    @Nullable
    public ICommand getCommand(@NotNull String search) {
        String searchLower = search.toLowerCase();

        for (ICommand command : this.commands) {
            if (command.getName().equals(searchLower) || command.getAliases().contains(searchLower)) {
                return command;
            }
        }

        return null;
    }

    /**
     * Gets all of the {@link ICommand} in the {@link CommandManager}.
     * @return The {@link ArrayList<ICommand>} that contains all of the {@link ICommand}.
     */
    @NotNull
    public ArrayList<ICommand> getCommands() {
        return commands;
    }

    /**
     * The method to be run when there is a {@link GuildMessageReceivedEvent}.
     * @param event The {@link GuildMessageReceivedEvent} to be checked.
     * @param prefix THe prefix that was run during the {@link GuildMessageReceivedEvent}.
     */
    public void handle(@NotNull GuildMessageReceivedEvent event, @NotNull String prefix) {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand command = this.getCommand(invoke);

        if (command != null) {

            Usage usage = command.getUsage();
            ArrayList<String> args = new ArrayList<>(Arrays.asList(split).subList(1, split.length));
            CommandErrorType errorType = usage.getERROR(args, event.getGuild());

            if (errorType.equals(CommandErrorType.SUCCESS)) {
                CommandContext ctx = new CommandContext(event, args, prefix);
                command.handle(ctx, args, event.getAuthor(), event);
            } else {
                event.getChannel().sendMessage(indexMessageEmbed(errorType, usage.getIncorrectIndex(), args)).queue();
            }
        } else {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Color.red);
            embedBuilder.setTitle("Command Not Found");
            embedBuilder.setDescription("The command you are trying to run has not been found...");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        }
    }

    /**
     * Creates a new message embed for the user.
     * @param errorType The {@link com.beanbeanjuice.utility.command.usage.types.CommandErrorType CommandErrorType} of the {@link ICommand}.
     * @param incorrectIndex The index of the {@link com.beanbeanjuice.utility.command.usage.types.CommandErrorType CommandErrorType}.
     * @param args The {@link ArrayList<String> arguments} of the {@link ICommand}.
     * @return The {@link MessageEmbed} to be sent.
     */
    @NotNull
    private MessageEmbed indexMessageEmbed(@NotNull CommandErrorType errorType, @NotNull Integer incorrectIndex, @NotNull ArrayList<String> args) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.addField(errorType.name(), errorType.getDescription(), true);
        if (args.size() > 0) {
            embedBuilder.addField("Location", "There is an error at variable **" + args.get(incorrectIndex) + "**", false);
        }
        embedBuilder.setColor(Color.red);
        return embedBuilder.build();
    }

}
