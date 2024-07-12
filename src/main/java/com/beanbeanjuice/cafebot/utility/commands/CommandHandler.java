package com.beanbeanjuice.cafebot.utility.commands;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.interactions.commands.Command.Choice;

import java.util.*;

public class CommandHandler extends ListenerAdapter {

    private final CafeBot cafeBot;
    @Getter private final HashMap<String, ICommand> commands;

    public CommandHandler(final CafeBot cafeBot) {
        this.cafeBot = cafeBot;
        commands = new HashMap<>();
    }

    public void addCommands(final ICommand... commands) {
        ArrayList<SlashCommandData> slashCommands = new ArrayList<>();
        Arrays.stream(commands).forEach((newCommand) -> {
            SlashCommandData commandData = getCommandData(newCommand);

            // Sub commands.
            SubcommandData[] subCommandDataArray = Arrays.stream(newCommand.getSubCommands())
                    .map(this::getSubcommandData)
                    .toArray(SubcommandData[]::new);
            commandData.addSubcommands(subCommandDataArray);

            // Groups.
            SubcommandGroupData[] groupDataArray = Arrays.stream(newCommand.getSubCommandGroups())
                    .map(this::getSubcommandGroupData)
                    .toArray(SubcommandGroupData[]::new);
            commandData.addSubcommandGroups(groupDataArray);

            slashCommands.add(commandData);
            this.commands.put(newCommand.getName(), newCommand);

            cafeBot.getLogger().log(CommandHandler.class, LogLevel.INFO, "Adding command: /" + newCommand.getName(), false, false);
        });

        cafeBot.getJDA()
                .updateCommands()
                .addCommands(slashCommands)
                .queue((e) -> cafeBot.getLogger().log(
                        this.getClass(),
                        LogLevel.INFO,
                        "Waiting for slash commands to propagate.",
                        false,
                        false)
                );
    }

    private SlashCommandData getCommandData(final ICommand command) {
        SlashCommandData commandData = Commands.slash(command.getName(), command.getDescription());
        commandData.addOptions(command.getOptions());
        commandData.setGuildOnly(!command.allowDM());
        commandData.setNSFW(command.isNSFW());
        commandData.setDefaultPermissions(DefaultMemberPermissions.enabledFor(command.getPermissions()));
        return commandData;
    }

    private SubcommandData getSubcommandData(final ISubCommand command) {
        SubcommandData subcommandData = new SubcommandData(command.getName(), command.getDescription());
        subcommandData.addOptions(command.getOptions());
        return subcommandData;
    }

    private SubcommandGroupData getSubcommandGroupData(final SubCommandGroup group) {
        SubcommandGroupData groupData = new SubcommandGroupData(group.getName(), group.getDescription());
        SubcommandData[] subcommandDataArray = Arrays.stream(group.getSubCommands()).map(this::getSubcommandData).toArray(SubcommandData[]::new);
        groupData.addSubcommands(subcommandDataArray);
        return groupData;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        if (!commands.containsKey(commandName)) return;
        ICommand command = commands.get(commandName);

        Optional<String> subCommandGroupName = Optional.ofNullable(event.getSubcommandGroup());
        Optional<String> subCommandName = Optional.ofNullable(event.getSubcommandName());

        logCommand(event);

        if (subCommandGroupName.isPresent()) {
            handleSubCommandWithGroup(subCommandName.orElseThrow(), subCommandGroupName.orElseThrow(), command, event);
            return;
        }

        if (subCommandName.isPresent()) {
            handleSubCommandWithoutGroup(subCommandName.orElseThrow(), command, event);
            return;
        }

        handleCommand(command, event);
    }

    private void handleSubCommandWithGroup(final String subCommandName, final String groupName, final ICommand command, final SlashCommandInteractionEvent event) {
        SubCommandGroup group = Arrays.stream(command.getSubCommandGroups()).filter((subGroup) -> subGroup.getName().equalsIgnoreCase(groupName)).findAny().orElseThrow();
        ISubCommand subCommand = getSubCommandFromGroup(subCommandName, group);
        handleSubCommand(subCommand, command, event);
    }

    private void handleSubCommandWithoutGroup(final String subCommandName, final ICommand command, final SlashCommandInteractionEvent event) {
        ISubCommand subCommand = getSubCommandFromList(subCommandName, command.getSubCommands());
        handleSubCommand(subCommand, command, event);
    }

    private void handleSubCommand(final ISubCommand subCommand, final ICommand command, final SlashCommandInteractionEvent event) {
        if (subCommand.isModal()) {
            event.replyModal(subCommand.getModal()).queue();
        } else {
            event.deferReply(command.isEphemeral()).queue();
            subCommand.handle(event);
        }

        cafeBot.increaseCommandsRun();
    }

    private void handleCommand(final ICommand command, final SlashCommandInteractionEvent event) {
        if (command.isModal()) {
            event.replyModal(command.getModal()).queue();
        } else {
            event.deferReply(command.isEphemeral()).queue();
            command.handle(event);
        }

        cafeBot.increaseCommandsRun();
    }

    private ISubCommand getSubCommandFromGroup(final String subCommandName, final SubCommandGroup group) {
        return Arrays.stream(group.getSubCommands())
                .filter((subCommand) -> subCommand.getName().equalsIgnoreCase(subCommandName))
                .findAny()
                .orElseThrow();
    }

    private ISubCommand getSubCommandFromList(final String subCommandName, final ISubCommand[] subCommands) {
        return Arrays.stream(subCommands)
                .filter((subCommand) -> subCommand.getName().equalsIgnoreCase(subCommandName))
                .findAny()
                .orElseThrow();
    }

    private void logCommand(final SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        StringBuilder commandString = new StringBuilder();

        commandString.append("<").append(event.getUser().getId()).append("> /").append(commandName);
        if (event.getSubcommandName() != null) commandString.append(" ").append(event.getSubcommandName());

        event.getOptions().forEach((optionMapping) -> {
            // Check if the mapping is null
            if (optionMapping == null) return;

            String type = optionMapping.getType().toString();
            String value = optionMapping.getAsString();
            commandString.append(" <").append(type).append(":").append(value).append(">");
        });

        cafeBot.getLogger().log(commands.get(event.getName()).getClass(), LogLevel.DEBUG, commandString.toString(), false, false);
    }

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        String commandName = event.getName();
        String option = event.getFocusedOption().getName();

        if (!commands.containsKey(commandName)) return;
        ICommand command = commands.get(commandName);

        Optional<String> subCommandGroupName = Optional.ofNullable(event.getSubcommandGroup());
        Optional<String> subCommandName = Optional.ofNullable(event.getSubcommandName());

        if (subCommandGroupName.isPresent()) {
            handleSubCommandWithGroupAutoComplete(subCommandName.orElseThrow(), subCommandGroupName.orElseThrow(), command, option, event);
            return;
        }

        if (subCommandName.isPresent()) {
            handleSubCommandWithoutGroupAutoComplete(subCommandName.orElseThrow(), command, option, event);
            return;
        }

        handleCommandAutoComplete(command, option, event);
    }

    private void handleSubCommandWithGroupAutoComplete(final String subCommandName, final String groupName, final ICommand command, final String option, final CommandAutoCompleteInteractionEvent event) {
        SubCommandGroup group = Arrays.stream(command.getSubCommandGroups()).filter((subGroup) -> subGroup.getName().equalsIgnoreCase(groupName)).findAny().orElseThrow();
        ISubCommand subCommand = getSubCommandFromGroup(subCommandName, group);
        handleSubCommandAutoComplete(subCommand, option, event);
    }

    private void handleSubCommandWithoutGroupAutoComplete(final String subCommandName, final ICommand command, final String option, final CommandAutoCompleteInteractionEvent event) {
        ISubCommand subCommand = getSubCommandFromList(subCommandName, command.getSubCommands());
        handleSubCommandAutoComplete(subCommand, option, event);
    }

    private void handleSubCommandAutoComplete(final ISubCommand subCommand, final String option, final CommandAutoCompleteInteractionEvent event) {
        List<Choice> options = getOptions(subCommand.getAutoComplete().get(option), event.getFocusedOption().getValue());
        handleAutoComplete(options, event);
    }

    private void handleCommandAutoComplete(final ICommand command, final String option, final CommandAutoCompleteInteractionEvent event) {
        List<Choice> options = getOptions(command.getAutoComplete().get(option), event.getFocusedOption().getValue());
        handleAutoComplete(options, event);
    }

    private void handleAutoComplete(final List<Choice> options, final CommandAutoCompleteInteractionEvent event) {
        event.replyChoices(options).queue();
    }

    private List<Choice> getOptions(final ArrayList<String> autoCompleteOptions, final String focusedOptionValue) {
        return autoCompleteOptions
                .stream()
                .filter((choiceString) -> choiceString.startsWith(focusedOptionValue))
                .map((string) -> new Choice(string, string))
                .limit(25)
                .toList();
    }

    public List<ICommand> getCommandsForCategory(final CommandCategory category) {
        return commands.values().stream()
                .filter((command) -> command.getCategory().equals(category))
                .toList();
    }

}
