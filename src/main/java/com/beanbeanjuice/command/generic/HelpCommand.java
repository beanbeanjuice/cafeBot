package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class HelpCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        // Checking if the arguments is empty.
        if (event.getOption("section-or-command") == null) {
            event.getHook().sendMessageEmbeds(categoryEmbed()).setEphemeral(true).queue(); // Sends the list of categories.
            return;
        }

        // Setting the Search Term
        String search = event.getOption("section-or-command").getAsString();
        int count = 1;

        // Goes through each category. If the first argument is equal to the name, then print commands for that category.
        for (CommandCategory categoryType : CommandCategory.values()) {
            if (categoryType.toString().equalsIgnoreCase(search) || String.valueOf(count++).equals(search)) {
                event.getHook().sendMessageEmbeds(searchCategoriesEmbed(categoryType)).setEphemeral(true).queue();
                return;
            }
        }

        ICommand command = Bot.getCommandHandler().getCommands().get(search.toLowerCase());

        // Checks to see if any commands exist for that command.
        if (command == null) {
            event.getHook().sendMessageEmbeds(noCommandFoundEmbed(search)).setEphemeral(true).queue();
            return;
        }

        // Logic to show command and optional parameters.
        event.getHook().sendMessageEmbeds(commandEmbed(command, search)).setEphemeral(true).queue();
    }

    @NotNull
    private MessageEmbed commandEmbed(@NotNull ICommand command, @NotNull String commandName) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("`/").append(commandName);
        StringBuilder paramBuilder = new StringBuilder();
        embedBuilder.setTitle(commandName.toUpperCase() + "");
        ArrayList<OptionData> options = command.getOptions();

        // Adding command options, if any.
        for (int i = 0; i < options.size(); i++) {
            OptionData option = options.get(i);
            stringBuilder.append(" <").append(option.getType()).append(">");

            paramBuilder.append("***").append(i + 1).append("***. ").append(option.getName());

            if (option.isRequired()) {
                paramBuilder.append(" - *__REQUIRED__*\n");
            } else {
                paramBuilder.append(" - *__OPTIONAL__*\n");
            }
        }
        stringBuilder.append("`");

        embedBuilder.addField("Usage", stringBuilder.toString(), false);

        if (!options.isEmpty()) {
            embedBuilder.addField("Arguments", paramBuilder.toString(), false);
        }

        embedBuilder.addField("Example", command.exampleUsage(), false)
                .addField("Description", command.getDescription(), false)
                .setColor(Helper.getRandomColor())
                .setFooter("If you need more help with commands, visit https://www.github.com/beanbeanjuice/cafeBot!");
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed searchCategoriesEmbed(@NotNull CommandCategory category) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(category.getMessage() + "\n\n");
        int count = 1;

        for (Map.Entry<String, ICommand> commandSet : Bot.getCommandHandler().getCommands().entrySet()) {
            if (commandSet.getValue().getCategoryType().equals(category)) {
                stringBuilder
                        .append("**").append(count++).append("** `/").append(commandSet.getKey())
                        .append("`\n");
            }
        }

        if (count == 1) {
            stringBuilder.append("There are no commands here right now :( but if this section is here, that means I'm working on it!");
        }

        embedBuilder
                .setTitle(category.toString())
                .setDescription(stringBuilder.toString())
                .setThumbnail(category.getLink())
                .setColor(Helper.getRandomColor())
                .setFooter("For help with a specific command, do /help (command name).");
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed categoryEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        int count = 1;

        for (CommandCategory category : CommandCategory.values()) {
            stringBuilder.append("**").append(count++).append("** `").append(category.toString());

            stringBuilder.append("`\n");
        }

        embedBuilder
                .setDescription(stringBuilder.toString())
                .setTitle("Command Categories")
                .setColor(Helper.getRandomColor())
                .setFooter("If you're stuck, use /help (category name/category number).");
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed noCommandFoundEmbed(@NotNull String commandName) {
        return new EmbedBuilder()
                .setTitle("No Command Found")
                .setDescription("No command has been found for `" + commandName + "`.")
                .setColor(Color.red)
                .setFooter("Please see /help!")
                .build();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Command list + how to use the commands.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/help` or `/help (section/section number)` or `/help (command name)`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "section-or-command", "Sub-argument for the help command.", false, false));
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

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }

}
