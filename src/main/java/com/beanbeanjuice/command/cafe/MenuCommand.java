package com.beanbeanjuice.command.cafe;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.cafe.object.MenuItem;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * A command used for seeing the menu.
 *
 * @author beanbeanjuice
 */
public class MenuCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        StringBuilder stringBuilder = new StringBuilder();

        ArrayList<MenuItem> menu = BeanBot.getMenuHandler().getMenu();

        for (int i = 0; i < menu.size(); i++) {

            stringBuilder.append(i+1)
                    .append(" **").append(menu.get(i).getName()).append("** - `$")
                    .append(menu.get(i).getPrice()).append("` ").append(menu.get(i).getDescription()).append("\n");

        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Menu Items");
        embedBuilder.setDescription(stringBuilder.toString());
        embedBuilder.setColor(BeanBot.getGeneralHelper().getRandomColor());
        embedBuilder.setFooter("I hope you enjoy your stay!~");

        event.getChannel().sendMessage(embedBuilder.build()).queue();
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
    public Usage getUsage() {
        return new Usage();
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.CAFE;
    }
}
