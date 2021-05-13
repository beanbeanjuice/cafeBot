package com.beanbeanjuice.command.cafe;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.cafe.object.CafeCustomer;
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
 * A command used to order {@link com.beanbeanjuice.utility.cafe.object.MenuItem MenuItem}s.
 *
 * @author beanbeanjuice
 */
public class OrderCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        CafeCustomer orderer = BeanBot.getServeHandler().getCafeCustomer(user);

        // Checking if the person who ordered is null.
        if (orderer == null) {
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().sqlServerError()).queue();
            return;
        }

        CafeCustomer receiver = BeanBot.getServeHandler().getCafeCustomer(BeanBot.getGeneralHelper().getUser(args.get(1)));

        // Checking if the receiver is null.
        if (receiver == null) {
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().sqlServerError()).queue();
            return;
        }

        int itemIndex = Integer.parseInt(args.get(0)) - 1;

        // Checking if the menu item is null.
        if (itemIndex >= BeanBot.getMenuHandler().getMenu().size()) {
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().errorEmbed(
                    "Unknown Item",
                    "The item `" + args.get(0) + "` does not exist. " +
                            "To view the menu, do `" + ctx.getPrefix() + "menu`!"
            )).queue();
            return;
        }

        MenuItem item = BeanBot.getMenuHandler().getItem(itemIndex);

        // Checking if they have enough money.
        if (orderer.getBeanCoinAmount() < item.getPrice()) {
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().errorEmbed(
                    "You Are Broke",
                    "You do not have enough money to purchase a `" + item.getName() + "`..."
            )).queue();
            return;
        }

        // Updating the person who ordered the item.
        if (!BeanBot.getMenuHandler().updateOrderer(orderer, item.getPrice())) {
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().sqlServerError()).queue();
            return;
        }

        // Updating the person who received the order.
        if (!BeanBot.getMenuHandler().updateReceiver(receiver)) {
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().sqlServerError()).queue();
            return;
        }

        event.getChannel().sendMessage(orderEmbed(orderer, receiver, item)).queue();
    }

    @NotNull
    private MessageEmbed orderEmbed(@NotNull CafeCustomer orderer, @NotNull CafeCustomer receiver, @NotNull MenuItem item) {
        User ordererUser = BeanBot.getJDA().getUserById(orderer.getUserID());
        User receiverUser = BeanBot.getJDA().getUserById(receiver.getUserID());

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Order Bought!");
        embedBuilder.setColor(BeanBot.getGeneralHelper().getRandomColor());
        embedBuilder.setDescription("Awww... " + ordererUser.getAsMention() + " bought a " + item.getName()
        + " for " + receiverUser.getAsMention() + " and lost `$" + item.getPrice() + "`!");
        embedBuilder.setFooter("So far, " + ordererUser.getName() + " has bought a total of " + (orderer.getOrdersBought()+1) + " menu items for other people. "
        + receiverUser.getName() + " has received a total of " + (receiver.getOrdersReceived()+1) + " menu items from other people.");
        embedBuilder.setThumbnail(item.getImageURL());
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "order";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Order something for someone!";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.NUMBER, "The Menu Item Number", true);
        usage.addUsage(CommandType.USER, "The Person You Are Ordering For", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.CAFE;
    }
}
