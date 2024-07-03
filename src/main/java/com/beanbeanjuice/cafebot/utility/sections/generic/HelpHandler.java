package com.beanbeanjuice.cafebot.utility.sections.generic;

import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.CommandHandler;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.*;
import java.util.stream.Collectors;

public class HelpHandler {

    private final CommandHandler commandHandler;

    public HelpHandler(final CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public MessageEmbed getCategoriesEmbed() {
        String categoriesString = Arrays.stream(CommandCategory.values()).map((category) -> String.format(
                """
                ### %d. %s
                > *%s*
                """, category.ordinal() + 1, category.name(), category.getDescription()
        )).collect(Collectors.joining());

        String description = String.format(
                """
                # Command Categories
                *Please select one of the categories below!*
                
                %s
                """, categoriesString);

        return new EmbedBuilder()
                .setDescription(description)
                .setColor(Helper.getRandomColor())
                .setFooter("Use the tabs below to select the category!")
                .build();
    }

    public MessageEmbed getCategoryEmbed(final CommandCategory category, final int index) {
        int skipAmount = 25 * index;
        String description = String.format("# Commands for %s", category.name());

        EmbedBuilder embedBuilder = new EmbedBuilder();

        commandHandler.getCommands().values()
                .stream()
                .filter(command -> command.getCategory() == category)
                .sorted(Comparator.comparing(ICommand::getName))
                .skip(skipAmount)
                .limit(25)
                .forEach((command) -> {
                    embedBuilder.addField(
                            String.format("**/%s**", command.getName()),
                            String.format("> *%s*", command.getDescription()),
                            true
                    );
                });

        return embedBuilder
                .setDescription(description)
                .setColor(Helper.getRandomColor())
                .setFooter("Select a command below!")
                .setThumbnail(category.getLink())
                .build();
    }

    public MessageEmbed getCommandEmbed(final String commandName) {
        ICommand command = commandHandler.getCommands().get(commandName);

        String subCommandsString = Arrays.stream(command.getSubCommands()).map((subCommand) -> {
            return String.format(
                    """
                    `%s %s`
                    > %s
                    
                    """, subCommand.getName(), this.getOptionsString(subCommand.getOptions()), subCommand.getDescription()
            );
        }).collect(Collectors.joining("\n"));
        subCommandsString = (subCommandsString.isBlank()) ? "*None*" : subCommandsString;

        String optionsString = this.getOptionsString(command.getOptions());
        optionsString = (optionsString.isBlank()) ? "*None*" : optionsString;

        String commandString = String.format(
                """
                # /%s
                > *%s*
                
                ### Arguments
                %s
                ### Subcommands
                %s
                """,
                command.getName(),
                command.getDescription(),
                optionsString,
                subCommandsString
        );

        return new EmbedBuilder()
                .setDescription(commandString)
                .setColor(Helper.getRandomColor())
                .build();
    }

    public String getOptionsString(final OptionData[] options) {
        return Arrays.stream(options).map((option) -> {
            String requiredString = (option.isRequired()) ? "REQUIRED" : "OPTIONAL";
            return String.format(
                    """
                    <%s:%s:%s>
                    """, option.getName(), option.getType().name(), requiredString);
        }).collect(Collectors.joining(" "));
    }

    public StringSelectMenu getAllCategoriesSelectMenu(int index) {
        StringSelectMenu.Builder builder = StringSelectMenu.create("cafeBot:help:" + index);
        builder.addOption("ALL", "ALL");
        Arrays.stream(CommandCategory.values()).forEach((category) -> builder.addOption(category.name(), category.toString()));
        return builder.setPlaceholder("Choose Category").setMaxValues(1).setMinValues(1).build();
    }

    public StringSelectMenu getCommandSelectionMenu(final CommandCategory category, final int index) {
        int skipAmount = 25 * index;
        StringSelectMenu.Builder builder = StringSelectMenu.create("cafeBot:help:commands");

        commandHandler.getCommandsForCategory(category)
                .stream()
                .sorted(Comparator.comparing(ICommand::getName))
                .skip(skipAmount)
                .limit(25)
                .forEach((item) -> builder.addOption("/" + item.getName(), item.getName()));
        return builder.setPlaceholder("Choose Command").setMaxValues(1).setMinValues(1).build();
    }

    public ActionRow getCommandButtons(final int index, final int max, final CommandCategory category) {
        Button left = Button.primary(String.format("cafeBot:help:button:left:%s:%d", category.name(), index), Emoji.fromFormatted("⬅️"));
        Button right = Button.primary(String.format("cafeBot:help:button:right:%s:%d", category.name(), index), Emoji.fromFormatted("➡️"));

        if (index <= 0) left = left.asDisabled();
        if (index >= max) right = right.asDisabled();

        return ActionRow.of(left, right);
    }

}
