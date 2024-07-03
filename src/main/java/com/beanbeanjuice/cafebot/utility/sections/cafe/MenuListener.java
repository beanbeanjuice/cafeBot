package com.beanbeanjuice.cafebot.utility.sections.cafe;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.cafe.CafeType;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.cafe.CafeUser;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.cafe.CafeUsersEndpoint;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class MenuListener extends ListenerAdapter {

    private final CafeBot cafeBot;

    public MenuListener(final CafeBot cafeBot) {
        this.cafeBot = cafeBot;
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getComponentId().equals("cafeBot:menu")) handleCategory(event);
        if (event.getComponentId().equals("cafeBot:menu:item")) handleItem(event);
    }

    @Override
    public void onEntitySelectInteraction(EntitySelectInteractionEvent event) {
        if (event.getComponentId().startsWith("cafeBot:menu:user:")) handlePurchase(event);
    }

    private void handleCategory(final StringSelectInteractionEvent event) {
        List<String> values = event.getValues(); // the values the user selected
        String value = values.getFirst();

        MenuHandler menuHandler = cafeBot.getMenuHandler();
        boolean isHome = value.equalsIgnoreCase("all");
        MessageEmbed menuEmbed = isHome ? menuHandler.getAllMenuEmbed() : menuHandler.getCategoryMenuEmbed(CafeCategory.valueOf(value));

        List<ActionRow> rows = new ArrayList<>();
        rows.add(ActionRow.of(menuHandler.getAllStringSelectMenu()));
        if (!isHome) rows.add(ActionRow.of(menuHandler.getItemStringSelectMenu(CafeCategory.valueOf(value))));

        event.editMessageEmbeds(menuEmbed)
                .setComponents(rows)
                .setReplace(true).queue();
    }

    private void handleItem(final StringSelectInteractionEvent event) {
        cafeBot.getCafeAPI().getCafeUsersEndpoint().getAndCreateCafeUser(event.getUser().getId())
                .thenAcceptAsync((cafeUser) -> {
                    List<String> values = event.getValues(); // the values the user selected
                    String itemString = values.getFirst();

                    MenuHandler menuHandler = cafeBot.getMenuHandler();

                    List<ActionRow> rows = new ArrayList<>();
                    rows.add(ActionRow.of(menuHandler.getAllStringSelectMenu()));
                    rows.add(ActionRow.of(menuHandler.getItemEntitySelectMenu(itemString)));

                    event.editMessageEmbeds(menuHandler.getItemEmbed(itemString, cafeUser))
                            .setComponents(rows)
                            .setReplace(true).queue();
                });
    }

    private void handlePurchase(final EntitySelectInteractionEvent event) {
        int itemID = Integer.parseInt(event.getComponentId().split(":")[3]);
        MenuItem item = cafeBot.getMenuHandler().getAllItems().get(itemID);

        User sender = event.getUser();
        IMentionable receiver = event.getValues().getFirst();

        CafeUsersEndpoint usersEndpoint = cafeBot.getCafeAPI().getCafeUsersEndpoint();
        CompletableFuture<CafeUser> getSenderFuture = usersEndpoint.getAndCreateCafeUser(sender.getId());
        CompletableFuture<CafeUser> getReceiverFuture = usersEndpoint.getAndCreateCafeUser(receiver.getId());

        getSenderFuture.thenCombineAsync(getReceiverFuture, (senderCafeUser, receiverCafeUser) -> {
            if (senderCafeUser.getBeanCoins() < item.getPrice()) {
                event.editMessageEmbeds(Helper.errorEmbed(
                        "Not Enough Money",
                        "Sorry... you don't have enough money for that!"
                )).setReplace(true).queue();
                return false;
            }

            double newBalance = senderCafeUser.getBeanCoins() - item.getPrice();
            int newOrdersBought = senderCafeUser.getOrdersBought() + 1;
            int newOrdersReceived = receiverCafeUser.getOrdersReceived() + 1;

            usersEndpoint.updateCafeUser(sender.getId(), CafeType.BEAN_COINS, newBalance);
            usersEndpoint.updateCafeUser(sender.getId(), CafeType.ORDERS_BOUGHT, newOrdersBought);
            usersEndpoint.updateCafeUser(receiver.getId(), CafeType.ORDERS_RECEIVED, newOrdersReceived);

            event.getChannel()
                    .sendMessage(String.format("%s just bought a **%s** for %s!", sender.getAsMention(), item.getName(), receiver.getAsMention()))
                    .addEmbeds(orderPurchased(sender, receiver, item, newOrdersBought, newOrdersReceived))
                    .queue();
            event.editMessageEmbeds(Helper.successEmbed("Order Up!", "They should have received their order by now."))
                    .setReplace(true)
                    .queue();

            return true;
        });
    }

    private MessageEmbed orderPurchased(final IMentionable sender, final IMentionable receiver, final MenuItem item,
                                        final int ordersBought, final int ordersReceived) {
        return new EmbedBuilder()
                .setColor(Helper.getRandomColor())
                .setDescription(String.format(
                                """
                                # %s
                                *%s*
                                
                                
                                ||%s has bought %d items.
                                %s has received %d items.||
                                """, item.getName(), item.getDescription(),
                        sender.getAsMention(), ordersBought, receiver.getAsMention(), ordersReceived
                ))
                .setImage(item.getImageURL())
                .build();
    }

}
