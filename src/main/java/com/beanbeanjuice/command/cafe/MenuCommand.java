package com.beanbeanjuice.command.cafe;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.cafe.object.MenuItem;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
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

        if (args.size() == 0) {
            StringBuilder stringBuilder = new StringBuilder();
            ArrayList<MenuItem> menu = CafeBot.getMenuHandler().getMenu();

            for (int i = 0; i < menu.size(); i++) {
                stringBuilder.append(i + 1)
                        .append(" **").append(menu.get(i).getName()).append("** - `$")
                        .append(menu.get(i).getPrice()).append("`\n");
            }

            event.getChannel().sendMessage(menuEmbed(stringBuilder.toString())).queue();
        } else {
            int itemIndex = Integer.parseInt(args.get(0)) - 1;

            // Checking if the item exists
            if (itemIndex >= CafeBot.getMenuHandler().getMenu().size()) {
                event.getChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                        "Unknown Item",
                        "The item `" + args.get(0) + "` does not exist. " +
                                "To view the menu, do `" + ctx.getPrefix() + "menu`!"
                )).queue();
                return;
            }

            MenuItem item = CafeBot.getMenuHandler().getItem(itemIndex);
            event.getChannel().sendMessage(menuItemEmbed(item, itemIndex)).queue();
        }
    }

    @NotNull
    private MessageEmbed menuItemEmbed(@NotNull MenuItem menuItem, @NotNull Integer itemIndex) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Menu Item - " + menuItem.getName());
        embedBuilder.addField("Price", "`$" + menuItem.getPrice() + "` beanCoins", true);
        embedBuilder.addField("Item Number", String.valueOf(itemIndex+1), true);
        embedBuilder.setDescription(menuItem.getDescription());
        embedBuilder.setThumbnail(menuItem.getImageURL());
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed menuEmbed(@NotNull String description) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Menu Items");
        embedBuilder.setDescription(description);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setFooter("I hope you enjoy your stay!~");
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
    public String exampleUsage() {
        return "`!!menu` or `!!menu 9`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.NUMBER, "Menu Item Number", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.CAFE;
    }
}
