package com.beanbeanjuice.cafebot.utility.commands;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.i18n.I18N;
import com.beanbeanjuice.cafebot.i18n.YamlControl;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.*;
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
                    .map((subCommand) -> this.getSubcommandData(subCommand, newCommand))
                    .toArray(SubcommandData[]::new);
            commandData.addSubcommands(subCommandDataArray);

            // Groups.
            SubcommandGroupData[] groupDataArray = Arrays.stream(newCommand.getSubCommandGroups())
                    .map((subCommand) -> this.getSubcommandGroupData(subCommand, newCommand))
                    .toArray(SubcommandGroupData[]::new);
            commandData.addSubcommandGroups(groupDataArray);

            slashCommands.add(commandData);
            this.commands.put(newCommand.getName(), newCommand);

            cafeBot.getLogger().log(CommandHandler.class, LogLevel.INFO, "Adding command: /" + newCommand.getName(), false, false);
        });

        cafeBot.getShardManager()
                .getShards()
                .getFirst()
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

    private SlashCommandData getCommandData(ICommand command) {
        String originalDescription = I18N.getBundle().getString(command.getDescriptionPath());
        SlashCommandData commandData = Commands.slash(command.getName(), originalDescription);

        Locale.availableLocales().forEach((locale) -> {
            ResourceBundle i18n = ResourceBundle.getBundle("messages", locale, YamlControl.INSTANCE);
            DiscordLocale discordLocale = DiscordLocale.valueOf(i18n.getString("info.bot.discord-locale"));

            String localizedDescription = I18N.getBundle(locale).getString(command.getDescriptionPath());
            commandData.setDescriptionLocalization(discordLocale, localizedDescription);
        });

        OptionData[] commandOptions = command.getOptions();

        commandOptions = Arrays.stream(commandOptions).peek((commandOption) -> {
            // Set original (english)
            String path = commandOption.getDescription();  // Description is originally the path.
            String originalCommandOptionDescription = I18N.getBundle().getString(commandOption.getDescription());
            commandOption.setDescription(originalCommandOptionDescription);

            // Set localization
            // Loop through all locales, map to discord locale, set localized description
            Locale.availableLocales().map((locale) -> {
                ResourceBundle i18n = ResourceBundle.getBundle("messages", locale, YamlControl.INSTANCE);

                try { return DiscordLocale.valueOf(i18n.getString("info.bot.discord-locale")); }
                catch (MissingResourceException e) { return null; }
            }).forEach((locale) -> {
                if (locale == null) return;

                String localizedCommandOptionDescription = I18N.getBundle(locale.toLocale()).getString(path);
                commandOption.setDescriptionLocalization(locale, localizedCommandOptionDescription);
            });

        }).toArray(OptionData[]::new);

        commandData.addOptions(commandOptions);

        ArrayList<InteractionContextType> contexts = new ArrayList<>();
        contexts.add(InteractionContextType.GUILD);
        if (command.allowDM()) contexts.add(InteractionContextType.BOT_DM);
        commandData.setContexts(contexts);

        commandData.setNSFW(command.isNSFW());
        commandData.setDefaultPermissions(DefaultMemberPermissions.enabledFor(command.getPermissions()));
        return commandData;
    }

    private SubcommandData getSubcommandData(ISubCommand command, ICommand parent) {
        String originalDescription = I18N.getBundle().getString(command.getDescriptionPath());
        SubcommandData subcommandData = new SubcommandData(command.getName(), originalDescription);

        Locale.availableLocales().forEach((locale) -> {
            ResourceBundle i18n = ResourceBundle.getBundle("messages", locale, YamlControl.INSTANCE);
            DiscordLocale discordLocale = DiscordLocale.valueOf(i18n.getString("info.bot.discord-locale"));

            String localizedDescription = I18N.getBundle(locale).getString(command.getDescriptionPath());
            subcommandData.setDescriptionLocalization(discordLocale, localizedDescription);
        });

        OptionData[] commandOptions = command.getOptions();

        commandOptions = Arrays.stream(commandOptions).peek((commandOption) -> {
            // Set original (english)
            String path = commandOption.getDescription();  // Description is originally the path.
            String originalCommandOptionDescription = I18N.getBundle().getString(commandOption.getDescription());
            commandOption.setDescription(originalCommandOptionDescription);

            // Set localization
            // Loop through all locales, map to discord locale, set localized description
            Locale.availableLocales().map((locale) -> {
                ResourceBundle i18n = ResourceBundle.getBundle("messages", locale, YamlControl.INSTANCE);

                try { return DiscordLocale.valueOf(i18n.getString("info.bot.discord-locale")); }
                catch (MissingResourceException e) { return null; }
            }).forEach((locale) -> {
                if (locale == null) return;

                String localizedCommandOptionDescription = I18N.getBundle(locale.toLocale()).getString(path);
                commandOption.setDescriptionLocalization(locale, localizedCommandOptionDescription);
            });

        }).toArray(OptionData[]::new);

        subcommandData.addOptions(commandOptions);
        return subcommandData;
    }

    private SubcommandGroupData getSubcommandGroupData(SubCommandGroup group, ICommand parent) {
        SubcommandGroupData groupData = new SubcommandGroupData(group.getName(), group.getDescription());
        SubcommandData[] subcommandDataArray = Arrays.stream(group.getSubCommands()).map((subCommand) -> this.getSubcommandData(subCommand, parent)).toArray(SubcommandData[]::new);
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

    private void handleSubCommand(ISubCommand subCommand, ICommand command, SlashCommandInteractionEvent event) {
        if (!subCommand.isModal()) event.deferReply(command.isEphemeral()).queue();

        Locale guildLocale = event.isFromGuild() ? event.getGuildLocale().toLocale() : event.getUserLocale().toLocale(); // Use user locale if not from guild
        ResourceBundle guildBundle = ResourceBundle.getBundle("messages", guildLocale, YamlControl.INSTANCE);
        ResourceBundle userBundle = ResourceBundle.getBundle("messages", event.getUserLocale().toLocale(), YamlControl.INSTANCE);

        subCommand.handle(event, new CommandContext(guildBundle, userBundle));
        cafeBot.increaseCommandsRun();
    }

    private void handleCommand(final ICommand command, final SlashCommandInteractionEvent event) {
        if (!command.isModal()) event.deferReply(command.isEphemeral()).queue();

        Locale guildLocale = event.isFromGuild() ? event.getGuildLocale().toLocale() : event.getUserLocale().toLocale(); // Use user locale if not from guild
        ResourceBundle guildBundle = ResourceBundle.getBundle("messages", guildLocale, YamlControl.INSTANCE);
        ResourceBundle userBundle = ResourceBundle.getBundle("messages", event.getUserLocale().toLocale(), YamlControl.INSTANCE);

        command.handle(event, new CommandContext(guildBundle, userBundle));
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
        subCommand.getAutoComplete(event).thenApply((map) -> map.get(option)).thenAccept((options) -> {
            List<Choice> choices = getOptions(options, event.getFocusedOption().getValue());
            handleAutoComplete(choices, event);
        });
    }

    private void handleCommandAutoComplete(final ICommand command, final String option, final CommandAutoCompleteInteractionEvent event) {
        command.getAutoComplete(event).thenApply((map) -> map.get(option)).thenAccept((options) -> {
            List<Choice> choices = getOptions(options, event.getFocusedOption().getValue());
            handleAutoComplete(choices, event);
        });
    }

    private void handleAutoComplete(final List<Choice> options, final CommandAutoCompleteInteractionEvent event) {
        event.replyChoices(options).queue();
    }

    private List<Choice> getOptions(final List<String> autoCompleteOptions, final String focusedOptionValue) {
        return autoCompleteOptions
                .stream()
                .filter((choiceString) -> choiceString.toUpperCase().contains(focusedOptionValue.toUpperCase()))
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
