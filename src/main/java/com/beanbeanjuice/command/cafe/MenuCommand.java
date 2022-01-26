package com.beanbeanjuice.command.cafe;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.sections.cafe.CafeCategory;
import com.beanbeanjuice.utility.sections.cafe.object.MenuItem;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A command used for seeing the menu.
 *
 * @author beanbeanjuice
 */
public class MenuCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        // If no arguments, show menu sections
        if (args.size() == 0) {
            event.getChannel().sendMessageEmbeds(cafeMenu(event.getGuild())).queue();
            return;
        }

        int categoryIndex = Integer.parseInt(args.get(0));

        // Checking if the Category Index is out of bounds.
        if (categoryIndex > CafeCategory.values().length || categoryIndex <= 0) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Unknown Category",
                    "Unknown category for \"" + categoryIndex + "\". Please use an existing category."
            )).queue();
            return;
        }

        // If argument, make sure it is a number in the range
        if (args.size() == 1) {
            event.getChannel().sendMessageEmbeds(cafeCategoryMenu(event.getGuild(), categoryIndex)).queue();
            return;
        }

        // If 2, make sure both are numbers in the range
        if (args.size() == 2) {
            int itemNumber = Integer.parseInt(args.get(1));
            CafeCategory category = CafeCategory.values()[categoryIndex - 1];
            MenuItem item = CafeBot.getMenuHandler().getItem(category, itemNumber - 1);

            // Checking if the menu item was NOT found.
            if (item == null) {
                event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                        "Item Not Found",
                        "A menu item with that ID was not found."
                )).queue();
                return;
            }

            event.getChannel().sendMessageEmbeds(cafeItemMenu(event.getGuild(), item, itemNumber, categoryIndex)).queue();
            return;
        }
    }

    /**
     * Creates the {@link MessageEmbed menu}.
     * @param guild THe {@link Guild guild} it is running for.
     * @return The finalised {@link MessageEmbed} to be sent.
     */
    @NotNull
    private MessageEmbed cafeMenu(Guild guild) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Cafe Menu");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setFooter("If you're stuck, use " + CafeBot.getGuildHandler().getCustomGuild(guild).getPrefix() + "menu (category number)");
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

    /**
     * Creates the {@link MessageEmbed categoryMenu}.
     * @param guild The {@link Guild guild} to be run for.
     * @param categoryIndex The {@link Integer categoryIndex} of the category specified.
     * @return The finalised {@link MessageEmbed} to be sent.
     */
    @NotNull
    private MessageEmbed cafeCategoryMenu(@NotNull Guild guild, @NotNull Integer categoryIndex) {
        CafeCategory category = CafeCategory.values()[categoryIndex - 1];
        ArrayList<MenuItem> itemsInCategory = CafeBot.getMenuHandler().getMenu(category);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(category.getTitle());
        embedBuilder.setFooter("For information on a single item, do " + CafeBot.getGuildHandler().getCustomGuild(guild).getPrefix() + "menu " + categoryIndex + " (item number)");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        StringBuilder descriptionBuilder = new StringBuilder();

        for (int i = 0; i < itemsInCategory.size(); i++) {
            MenuItem item = itemsInCategory.get(i);
            descriptionBuilder.append("**").append((i+1)).append("** ")
                    .append("*").append(item.getName()).append("* - `")
                    .append(item.getPrice()).append("bC` -- (").append(categoryIndex).append(" ").append(i + 1).append(")\n");
        }
        embedBuilder.setDescription(descriptionBuilder.toString());
        embedBuilder.setThumbnail(category.getImageURL());
        return embedBuilder.build();
    }

    /**
     * Creates the {@link MessageEmbed itemMenu}.
     * @param guild The {@link Guild guild} to be run for.
     * @param item The {@link MenuItem item} specified.
     * @param itemNumber The {@link Integer itemNumber} for the {@link MenuItem item}.
     * @param categoryIndex The {@link Integer categoryIndex} of the {@link MenuItem}.
     * @return The finalised {@link MessageEmbed} to be sent.
     */
    @NotNull
    private MessageEmbed cafeItemMenu(@NotNull Guild guild, @NotNull MenuItem item, @NotNull Integer itemNumber, @NotNull Integer categoryIndex) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(item.getName());
        embedBuilder.setDescription(item.getDescription());
        embedBuilder.addField("Price", "`" + item.getPrice() + "bC`", true);
        embedBuilder.addField("Item ID", "(" + categoryIndex + " " + itemNumber + ")", true);
        embedBuilder.setImage(item.getImageURL());
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setFooter("To order this item, do " + CafeBot.getGuildHandler().getCustomGuild(guild).getPrefix() + "order " + categoryIndex + " " + itemNumber + " (User)");
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "menu";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "See the cafe menu!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "menu` or `" + prefix + "menu 2` or `" + prefix + "menu 2 3`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.NUMBER, "Category Number", false);
        usage.addUsage(CommandType.NUMBER, "Item Number", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.CAFE;
    }
}
