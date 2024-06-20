package com.beanbeanjuice.cafebot.command.cafe;

import com.beanbeanjuice.cafebot.Bot;
import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.section.cafe.CafeCategory;
import com.beanbeanjuice.cafebot.utility.section.cafe.MenuHandler;
import com.beanbeanjuice.cafebot.utility.section.cafe.MenuItem;
import com.beanbeanjuice.cafebot.utility.section.cafe.ServeHandler;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import com.beanbeanjuice.cafeapi.wrapper.cafebot.cafe.CafeType;
import com.beanbeanjuice.cafeapi.wrapper.cafebot.cafe.CafeUser;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.CafeException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to order a {@link MenuItem MenuItem}.
 *
 * @author beanbeanjuice
 */
public class OrderCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        CafeUser orderer = ServeHandler.getCafeUser(event.getUser());

        // Checking if there was an error getting the CafeUser
        if (orderer == null) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Getting User",
                    "There has been an error getting the Cafe User."
            )).queue();
            return;
        }

        CafeUser receiver = ServeHandler.getCafeUser(event.getOption("user").getAsUser());
        if (receiver == null) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Getting User",
                    "There has been an error getting the Cafe User."
            )).queue();
            return;
        }

        int categoryIndex = event.getOption("category-number").getAsInt();
        int itemIndex = event.getOption("item-number").getAsInt();

        CafeCategory category = CafeCategory.values()[categoryIndex - 1];
        MenuItem item = MenuHandler.getItem(category, itemIndex - 1);

        // Checking if the menu item was NOT found.
        if (item == null) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Item Not Found",
                    "A menu item with that ID was not found."
            )).queue();
            return;
        }

        // Checking if they have enough money.
        double totalPrice = item.getPrice();

        if (orderer.getBeanCoins() < totalPrice) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "You Are Broke LMAO",
                    "You do not have enough money to purchase x1 `" + item.getName() + "`... To learn how to make money, do " +
                            "`/help serve`!"
            )).queue();
            return;
        }

        // Trying to update the orderer.
        try {
            Bot.getCafeAPI().CAFE_USER.updateCafeUser(orderer.getUserID(), CafeType.BEAN_COINS, orderer.getBeanCoins() - totalPrice);
            Bot.getCafeAPI().CAFE_USER.updateCafeUser(orderer.getUserID(), CafeType.ORDERS_BOUGHT, orderer.getOrdersBought() + 1);
        } catch (CafeException e) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Updating User",
                    e.getMessage()
            )).queue();
            Bot.getLogger().log(this.getClass(), LogLevel.ERROR, "There was an error updating the orderer: " + e.getMessage(), e);
            return;
        }

        // Trying to update the receiver.
        try {
            Bot.getCafeAPI().CAFE_USER.updateCafeUser(receiver.getUserID(), CafeType.ORDERS_RECEIVED, receiver.getOrdersReceived() + 1);
        } catch (CafeException e) {
            Bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error Updating Receiver: " + e.getMessage(), e);
            return;
        }

        try {
            event.getHook().sendMessageEmbeds(orderEmbed(orderer, receiver, item, event.getOption("optional-message").getAsString())).queue();
        } catch (NullPointerException e) {
            event.getHook().sendMessageEmbeds(orderEmbed(orderer, receiver, item, null)).queue();
        }

        if (receiver.getUserID().equals(Bot.getBot().getSelfUser().getId())) {
            event.getChannel().sendMessage(event.getUser().getAsMention() + " Awww! Thank you for buying for me! I really appreciate it ^-^").queue();
        }
    }

    @NotNull
    private MessageEmbed orderEmbed(@NotNull CafeUser orderer, @NotNull CafeUser receiver, @NotNull MenuItem item, @Nullable String message) {
        User ordererUser = Bot.getBot().getUserById(orderer.getUserID());
        User receiverUser = Bot.getBot().getUserById(receiver.getUserID());

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Order Bought!")
                .setColor(Helper.getRandomColor())
                .setDescription("Awww... " + ordererUser.getAsMention() + " bought a " + item.getName()
                + " for " + receiverUser.getAsMention() + " and lost `" + item.getPrice() + "bC`!")
                .setFooter("So far, " + ordererUser.getName() + " has bought a total of " + (orderer.getOrdersBought() + 1) + " menu items for other people. "
                + receiverUser.getName() + " has received a total of " + (receiver.getOrdersReceived() + 1) + " menu items from other people.")
                .setThumbnail(item.getImageURL());

        if (message != null) {
            embedBuilder.addField("Personalised Message", "\"" + message + "\"", false);
        }
        return embedBuilder.build();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Order an item from the menu!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/order 1 1` or `/order 1 1 @beanbeanjuice` or `/order 1 1 @beanbeanjuice you're so cool!`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.INTEGER, "category-number", "The category number for the menu item.", true, false)
                .setRequiredRange(1, CafeCategory.values().length));
        options.add(new OptionData(OptionType.INTEGER, "item-number", "The number of the menu item.", true, false));
        options.add(new OptionData(OptionType.USER, "user", "The user to order for.", true, false));
        options.add(new OptionData(OptionType.STRING, "optional-message", "An optional message to add.", false, false));
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

}
