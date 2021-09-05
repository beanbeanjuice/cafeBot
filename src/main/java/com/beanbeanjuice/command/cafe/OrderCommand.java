package com.beanbeanjuice.command.cafe;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.cafeapi.cafebot.cafe.CafeType;
import com.beanbeanjuice.cafeapi.cafebot.cafe.CafeUser;
import com.beanbeanjuice.cafeapi.exception.*;
import com.beanbeanjuice.utility.logger.LogLevel;
import com.beanbeanjuice.utility.sections.cafe.CafeCategory;
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
 * A command used to order a {@link com.beanbeanjuice.utility.sections.cafe.object.MenuItem MenuItem}.
 *
 * @author beanbeanjuice
 */
public class OrderCommand implements ICommand {

    int argumentIndex = 0;

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        CafeUser orderer = CafeBot.getServeHandler().getCafeUser(user);

        if (orderer == null) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Error Getting User",
                    "There has been an error getting the Cafe User."
            )).queue();
            return;
        }

        ArrayList<CafeUser> receivers = getReceivers(new ArrayList<>(args));

        // Checking if there are no receivers.
        if (receivers.isEmpty()) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Invalid Users",
                    "Any users mentioned must be entered first."
            )).queue();
            return;
        }

        // Checking if the Category Index is out of bounds.
        int categoryIndex = Integer.parseInt(args.get(0));

        if (categoryIndex > CafeCategory.values().length || categoryIndex <= 0) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Unknown Category",
                    "Unknown category for \"" + categoryIndex + "\". Please use an existing category."
            )).queue();
            return;
        }

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

        // Checking if they have enough money.
        double totalPrice = receivers.size()*item.getPrice();

        if (orderer.getBeanCoins() < totalPrice) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "You Are Broke",
                    "You do not have enough money to purchase " + receivers.size() + "x `" + item.getName() + "`..."
            )).queue();
            return;
        }

        try {
            CafeBot.getCafeAPI().cafeUsers().updateCafeUser(orderer.getUserID(), CafeType.BEAN_COINS, orderer.getBeanCoins() - totalPrice);
            CafeBot.getCafeAPI().cafeUsers().updateCafeUser(orderer.getUserID(), CafeType.ORDERS_BOUGHT, orderer.getOrdersBought() + 1);
        } catch (CafeException e) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Error Updating User",
                    e.getMessage()
            )).queue();
            CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "There was an error updating the orderer: " + e.getMessage(), e);
            return;
        }

        // Updating the people who received the order.
        for (CafeUser receiver : receivers) {

            try {
                CafeBot.getCafeAPI().cafeUsers().updateCafeUser(receiver.getUserID(), CafeType.ORDERS_RECEIVED, receiver.getOrdersReceived() + 1);
            } catch (CafeException e) {
                CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Updating Receiver: " + e.getMessage(), e);
                return;
            }
        }

        event.getChannel().sendMessageEmbeds(orderEmbed(orderer, receivers, item, args)).queue();

        for (CafeUser receiver : receivers) {
            if (receiver.getUserID().equals(ctx.getSelfMember().getId())) {
                event.getMessage().reply("Awww! Thank you for buying for me! I really appreciate it ^-^").queue();
            }
        }
    }

    @NotNull
    private ArrayList<CafeUser> getReceivers(@NotNull ArrayList<String> arguments) {
        ArrayList<CafeUser> receivers = new ArrayList<>();
        ArrayList<String> receiversIDs = new ArrayList<>();

        // Removes the first 2 indexes in the arguments. This is the category and item number.
        arguments.remove(0);
        arguments.remove(0);

        argumentIndex = 2;
        for (String argument : arguments) {
            try {
                User user = CafeBot.getGeneralHelper().getUser(argument);

                // Checks if the receiver is already in the receiver ArrayList.
                // This is to prevent duplicate orders.
                if (!receiversIDs.contains(user.getId())) {
                    CafeUser newReceiver = CafeBot.getServeHandler().getCafeUser(user.getId());
                    receivers.add(newReceiver);
                    argumentIndex += 1;
                    receiversIDs.add(user.getId());
                } else {
                    argumentIndex += 1;
                }
            } catch (NullPointerException e) {
                return receivers;
            }
        }
        return receivers;
    }

    @NotNull
    private MessageEmbed orderEmbed(@NotNull CafeUser orderer, @NotNull ArrayList<CafeUser> receivers, @NotNull MenuItem item, @NotNull ArrayList<String> args) {
        User ordererUser = CafeBot.getJDA().getUserById(orderer.getUserID());

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Order Bought!");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());

        // Adding a personalised message.
        try {
            if (args.size() >= 1) {
                if (argumentIndex != args.size()) {
                    StringBuilder descriptionBuilder = new StringBuilder();
                    descriptionBuilder.append("\"");
                    for (int i = argumentIndex; i < args.size(); i++) {
                        descriptionBuilder.append(args.get(i));

                        if (i != args.size() - 1) {
                            descriptionBuilder.append(" ");
                        }
                    }
                    descriptionBuilder.append("\"");
                    embedBuilder.addField("Personalised Message", descriptionBuilder.toString(), false);
                }
            }
        } catch (IndexOutOfBoundsException ignored) {}

        StringBuilder receiversBuilder = new StringBuilder();

        for (int i = 0; i < receivers.size(); i++) {
            receiversBuilder.append(CafeBot.getJDA().getUserById(receivers.get(i).getUserID()).getAsMention());

            if (i != receivers.size() - 1) {
                receiversBuilder.append(", and ");
            } else {
                receiversBuilder.append(", ");
            }
        }

        embedBuilder.setDescription("Awww... " + ordererUser.getAsMention() + " bought a " + item.getName()
                + " for " + receiversBuilder + " and lost `" + item.getPrice()*receivers.size() + "bC`!");

        // Only show this if they only bought for one person.
        if (receivers.size() == 1) {
            User receiverUser = CafeBot.getJDA().getUserById(receivers.get(0).getUserID());
            embedBuilder.setFooter("So far, " + ordererUser.getName() + " has bought a total of " + (orderer.getOrdersBought() + 1) + " menu items for other people. "
                    + receiverUser.getName() + " has received a total of " + (receivers.get(0).getOrdersReceived() + 1) + " menu items from other people.");
        }
        embedBuilder.setThumbnail(item.getImageURL());
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "order";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("buy");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Order something for someone!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "order 1 2 @beanbeanjuice` or `" + prefix + "order 1 2 @beanbeanjuice @testUser1 You're so cool :sunglasses:`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.NUMBER, "Category Number", true);
        usage.addUsage(CommandType.NUMBER, "Item Number", true);
        usage.addUsage(CommandType.USER, "Discord Mentions", true);
        usage.addUsage(CommandType.SENTENCE, "Extra Message", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.CAFE;
    }
}
