package com.beanbeanjuice.cafebot.utility.listeners;

import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.CommandHandler;
import com.beanbeanjuice.cafebot.utility.handlers.HelpHandler;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class HelpListener extends ListenerAdapter {

    private final CommandHandler commandHandler;
    private final HelpHandler helpHandler;

    public HelpListener(CommandHandler commandHandler, HelpHandler helpHandler) {
        this.commandHandler = commandHandler;
        this.helpHandler = helpHandler;
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getComponentId().equals("cafeBot:help:commands")) handleCommand(event);
        else if (event.getComponentId().startsWith("cafeBot:help:")) handleCategories(event);
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getComponentId().startsWith("cafeBot:help:button:")) return;

        String[] splitString = event.getComponentId().split(":");
        String direction = splitString[3];
        CommandCategory category = CommandCategory.valueOf(splitString[4]);
        int index = Integer.parseInt(event.getComponentId().split(":")[5]);
        index = direction.equals("left") ? index - 1 : index + 1;

        MessageEmbed categoryEmbed = helpHandler.getCategoryEmbed(category, event.getUserLocale(), index);

        List<ActionRow> rows = new ArrayList<>();
        rows.add(ActionRow.of(helpHandler.getAllCategoriesSelectMenu(0)));

        int maxIndices = commandHandler.getCommandsForCategory(category).size() / 25;
        rows.add(ActionRow.of(helpHandler.getCommandSelectionMenu(category, index)));
        rows.add(helpHandler.getCommandButtons(index, maxIndices, category));

        event.editMessageEmbeds(categoryEmbed)
                .setComponents(rows)
                .setReplace(true).queue();
    }

    private void handleCategories(final StringSelectInteractionEvent event) {
        List<String> values = event.getValues(); // the values the user selected
        String value = values.getFirst();

        int index = Integer.parseInt(event.getComponentId().split(":")[2]);

        boolean isHome = value.equalsIgnoreCase("all");
        MessageEmbed categoryEmbed = isHome ? helpHandler.getCategoriesEmbed() : helpHandler.getCategoryEmbed(CommandCategory.valueOf(value), event.getUserLocale(), 0);

        List<ActionRow> rows = new ArrayList<>();
        rows.add(ActionRow.of(helpHandler.getAllCategoriesSelectMenu(0)));

        if (!isHome) {
            CommandCategory category = CommandCategory.valueOf(value);
            int maxIndices = commandHandler.getCommandsForCategory(category).size() / 25;
            rows.add(ActionRow.of(helpHandler.getCommandSelectionMenu(category, index)));
            rows.add(helpHandler.getCommandButtons(index, maxIndices, category));
        }

        event.editMessageEmbeds(categoryEmbed)
                .setComponents(rows)
                .setReplace(true).queue();
    }

    private void handleCommand(final StringSelectInteractionEvent event) {
        List<String> values = event.getValues(); // the values the user selected
        String commandName = values.getFirst();

        List<ActionRow> rows = new ArrayList<>();
        rows.add(ActionRow.of(helpHandler.getAllCategoriesSelectMenu(0)));

        event.editMessageEmbeds(helpHandler.getCommandEmbed(commandName, event.getUserLocale()))
                .setComponents(rows)
                .setReplace(true).queue();
    }

}
