package com.beanbeanjuice.cafebot.utility.listeners;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.MenuCategory;
import com.beanbeanjuice.cafebot.api.wrapper.api.exception.ApiRequestException;
import com.beanbeanjuice.cafebot.api.wrapper.type.MenuItem;
import com.beanbeanjuice.cafebot.api.wrapper.type.MenuOrder;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.handlers.MenuHandler;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.selections.EntitySelectMenu;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MenuListener extends ListenerAdapter {

    private final CafeBot cafeBot;

    public MenuListener(CafeBot cafeBot) {
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

    private void handleCategory(StringSelectInteractionEvent event) {
        List<String> values = event.getValues(); // the values the user selected
        String value = values.getFirst();

        MenuHandler menuHandler = cafeBot.getMenuHandler();
        boolean isHome = value.equalsIgnoreCase("all");

        menuHandler.getCategoryMenuEmbed(isHome ? null : MenuCategory.valueOf(value)).thenAccept((categoryMessageEmbed) -> {
            categoryMessageEmbed.ifPresentOrElse(
                    (menuEmbed) -> {
                        List<ActionRow> rows = new ArrayList<>();
                        rows.add(ActionRow.of(menuHandler.getAllStringSelectMenu()));

                        menuHandler.getItemStringSelectMenu(MenuCategory.valueOf(value)).thenAccept((row) -> {
                            if (!isHome) rows.add(ActionRow.of(row));

                            event.editMessageEmbeds(menuEmbed)
                                    .setComponents(rows)
                                    .setReplace(true).queue();
                        });
                    },
                    () -> {
                        MessageEmbed menuEmbed = menuHandler.getAllMenuEmbed();
                        List<ActionRow> rows = new ArrayList<>();
                        rows.add(ActionRow.of(menuHandler.getAllStringSelectMenu()));
                        event.editMessageEmbeds(menuEmbed)
                                .setComponents(rows)
                                .setReplace(true).queue();
                    }
            );

        });

    }

    private void handleItem(StringSelectInteractionEvent event) {
        cafeBot.getCafeAPI().getUserApi().getUser(event.getUser().getId()).thenAccept((cafeUser) -> {
            List<String> values = event.getValues(); // the values the user selected
            String itemString = values.getFirst();
            MenuHandler menuHandler = cafeBot.getMenuHandler();

            CompletableFuture<EntitySelectMenu> f1 = menuHandler.getItemEntitySelectMenu(itemString);
            CompletableFuture<MessageEmbed> f2 = menuHandler.getItemEmbed(itemString, cafeUser);

            f1.thenAcceptBoth(f2, (entitySelectMenu, messageEmbed) -> {
                List<ActionRow> rows = new ArrayList<>();
                rows.add(ActionRow.of(menuHandler.getAllStringSelectMenu()));
                rows.add(ActionRow.of(entitySelectMenu));

                event.editMessageEmbeds(messageEmbed)
                        .setComponents(rows)
                        .setReplace(true).queue();
            });
        });
    }

    private void handlePurchase(EntitySelectInteractionEvent event) {
        int itemId = Integer.parseInt(event.getComponentId().split(":")[3]);

        cafeBot.getCafeAPI().getMenuApi().getMenuItem(itemId).thenAccept((item) -> {
            User sender = event.getUser();
            IMentionable receiver = event.getValues().getFirst();

            cafeBot.getCafeAPI().getOrderApi().createOrder(sender.getId(), receiver.getId(), itemId).thenAccept((newOrder) -> {
                CompletableFuture<MenuOrder[]> ordersBoughtFuture = cafeBot.getCafeAPI().getOrderApi().getOrdersSent(sender.getId());
                CompletableFuture<MenuOrder[]> ordersReceivedFuture = cafeBot.getCafeAPI().getOrderApi().getOrdersReceived(receiver.getId());

                ordersBoughtFuture.thenAcceptBoth(ordersReceivedFuture, (ordersBought, ordersReceived) -> {
                    event.getChannel()
                            .sendMessage(String.format("%s just bought a **%s** for %s!", sender.getAsMention(), item.getName(), receiver.getAsMention()))
                            .addEmbeds(orderPurchased(sender, receiver, item, ordersBought.length, ordersReceived.length))
                            .queue();
                    event.editMessageEmbeds(Helper.successEmbed("Order Up!", "They should have received their order by now."))
                            .setReplace(true)
                            .queue();
                });

            }).exceptionally((e) -> {
                Throwable cause = e.getCause();

                if (cause instanceof ApiRequestException requestException) {
                    JsonNode error = requestException.getBody();

                    if (error.get("from") != null && error.get("from").get(0).asString().equals("Insufficient balance")) {
                        event.editMessageEmbeds(Helper.errorEmbed(
                                "Not Enough Money",
                                "Sorry... you don't have enough money for that!"
                        )).setReplace(true).queue();
                        return null;
                    }

                }

                return null;
            });
        });

    }

    private MessageEmbed orderPurchased(IMentionable sender, IMentionable receiver, MenuItem item,
                                        int ordersBought, int ordersReceived) {
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
                .setImage(item.getImageUrl())
                .setFooter("Use \"/menu\" to browse the menu and order for someone!")
                .build();
    }

}
