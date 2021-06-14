package com.beanbeanjuice.command.cafe;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.sections.cafe.object.CafeCustomer;
import com.beanbeanjuice.utility.sections.cafe.object.MenuItem;
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
 * A command used to order {@link com.beanbeanjuice.utility.sections.cafe.object.MenuItem MenuItem}s.
 *
 * @author beanbeanjuice
 */
public class OrderCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        CafeCustomer orderer = CafeBot.getServeHandler().getCafeCustomer(user);

        // Checking if the person who ordered is null.
        if (orderer == null) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
            return;
        }

        CafeCustomer receiver = CafeBot.getServeHandler().getCafeCustomer(CafeBot.getGeneralHelper().getUser(args.get(1)));

        // Checking if the receiver is null.
        if (receiver == null) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
            return;
        }

        int itemIndex = Integer.parseInt(args.get(0)) - 1;

        // Checking if the menu item is null.
        if (itemIndex >= CafeBot.getMenuHandler().getMenu().size()) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                    "Unknown Item",
                    "The item `" + args.get(0) + "` does not exist. " +
                            "To view the menu, do `" + ctx.getPrefix() + "menu`!"
            )).queue();
            return;
        }

        MenuItem item = CafeBot.getMenuHandler().getItem(itemIndex);

        // Checking if they have enough money.
        if (orderer.getBeanCoinAmount() < item.getPrice()) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                    "You Are Broke",
                    "You do not have enough money to purchase a `" + item.getName() + "`..."
            )).queue();
            return;
        }

        // Updating the person who ordered the item.
        if (!CafeBot.getMenuHandler().updateOrderer(orderer, item.getPrice())) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
            return;
        }

        // Updating the person who received the order.
        if (!CafeBot.getMenuHandler().updateReceiver(receiver)) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
            return;
        }

        event.getChannel().sendMessage(orderEmbed(orderer, receiver, item, args)).queue();
    }

    @NotNull
    private MessageEmbed orderEmbed(@NotNull CafeCustomer orderer, @NotNull CafeCustomer receiver, @NotNull MenuItem item, @NotNull ArrayList<String> args) {
        User ordererUser = CafeBot.getJDA().getUserById(orderer.getUserID());
        User receiverUser = CafeBot.getJDA().getUserById(receiver.getUserID());

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Order Bought!");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());

        // Adding a personalised message.
        if (args.size() >= 3) {
            StringBuilder descriptionBuilder = new StringBuilder();
            descriptionBuilder.append("\"");
            for (int i = 2; i < args.size(); i++) {
                descriptionBuilder.append(args.get(i)).append(" ");
            }
            descriptionBuilder.append("\"");
            embedBuilder.addField("Personalised Message", descriptionBuilder.toString(), false);
        }

        embedBuilder.setDescription("Awww... " + ordererUser.getAsMention() + " bought a " + item.getName()
                + " for " + receiverUser.getAsMention() + " and lost `" + item.getPrice() + "bC`!");
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
    public String exampleUsage(String prefix) {
        return "`" + prefix + "order 9 @beanbeanjuice`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.NUMBER, "The Menu Item Number", true);
        usage.addUsage(CommandType.USER, "The Person You Are Ordering For", true);
        usage.addUsage(CommandType.SENTENCE, "Extra Message", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.CAFE;
    }
}
