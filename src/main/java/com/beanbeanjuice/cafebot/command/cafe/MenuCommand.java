package com.beanbeanjuice.cafebot.command.cafe;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.section.cafe.CafeCategory;
import com.beanbeanjuice.cafebot.utility.section.cafe.MenuHandler;
import com.beanbeanjuice.cafebot.utility.section.cafe.MenuItem;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to check the Cafe menu.
 *
 * @author beanbeanjuice
 */
public class MenuCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {

        // If both options are null.
        if (event.getOption("category-number") == null && event.getOption("item-number") == null) {
            event.getHook().sendMessageEmbeds(fullMenu()).queue();
            return;
        }

        // Cannot have an item number without a category number.
        if (event.getOption("category-number") == null && event.getOption("item-number") != null) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Item Without Category",
                    "If you choose an item, you must also choose a category."
            )).queue();
            return;
        }

        int categoryIndex;
        categoryIndex = event.getOption("category-number").getAsInt();

        // If you only have a category number.
        if (event.getOption("category-number") != null && event.getOption("item-number") == null) {

            CafeCategory category = CafeCategory.values()[categoryIndex - 1];
            ArrayList<MenuItem> itemsInCategory = MenuHandler.getMenu(category);

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(category.getTitle());
            embedBuilder.setFooter("For information on a single item, do /menu " + categoryIndex + " (item number)");
            embedBuilder.setColor(Helper.getRandomColor());
            StringBuilder descriptionBuilder = new StringBuilder();

            for (int i = 0; i < itemsInCategory.size(); i++) {
                MenuItem item = itemsInCategory.get(i);
                descriptionBuilder.append("**").append((i+1)).append("** ")
                        .append("*").append(item.getName()).append("* - `")
                        .append(item.getPrice()).append("bC` -- (").append(categoryIndex).append(" ").append(i + 1).append(")\n");
            }
            embedBuilder.setDescription(descriptionBuilder.toString());
            embedBuilder.setThumbnail(category.getImageURL());
            event.getHook().sendMessageEmbeds(embedBuilder.build()).queue();
            return;
        }

        int itemNumber = event.getOption("item-number").getAsInt();
        CafeCategory category = CafeCategory.values()[categoryIndex - 1];
        MenuItem item = MenuHandler.getItem(category, itemNumber - 1);

        // Checking if the menu item was NOT found.
        if (item == null) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Item Not Found",
                    "A menu item with that ID was not found."
            )).queue();
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(item.getName());
        embedBuilder.setDescription(item.getDescription());
        embedBuilder.addField("Price", "`" + item.getPrice() + "bC`", true);
        embedBuilder.addField("Item ID", "(" + categoryIndex + " " + itemNumber + ")", true);
        embedBuilder.setImage(item.getImageURL());
        embedBuilder.setColor(Helper.getRandomColor());
        embedBuilder.setFooter("To order this item, do /order " + categoryIndex + " " + itemNumber + " (User)");
        event.getHook().sendMessageEmbeds(embedBuilder.build()).queue();
    }

    @NotNull
    private MessageEmbed fullMenu() {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Cafe Menu")
                .setColor(Helper.getRandomColor())
                .setFooter("If you're stuck, use /menu (category number)");

        StringBuilder descriptionBuilder = new StringBuilder();
        int count = 1;

        for (CafeCategory category : CafeCategory.values()) {

            // Don't show the "Secret Menu"
            if (!category.getTitle().equals("Secret Menu")) {
                descriptionBuilder.append("**").append(count++).append("** `")
                        .append(category.getTitle()).append("`\n");
            }
        }
        embedBuilder.setDescription(descriptionBuilder.toString());
        return embedBuilder.build();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Get the cafe menu!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/menu` or `/menu 1` or `/menu 1 2`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.INTEGER, "category-number", "Category number for the menu item.", false, false)
                .setRequiredRange(1, CafeCategory.values().length));
        options.add(new OptionData(OptionType.INTEGER, "item-number", "Item number for the menu item.", false, false));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.CAFE;
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
