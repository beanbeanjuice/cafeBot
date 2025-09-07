package com.beanbeanjuice.cafebot.utility.handlers;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.MenuCategory;
import com.beanbeanjuice.cafebot.api.wrapper.type.DiscordUser;
import com.beanbeanjuice.cafebot.api.wrapper.type.MenuItem;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.listeners.MenuListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.selections.EntitySelectMenu;
import net.dv8tion.jda.api.components.selections.EntitySelectMenu.Builder;
import net.dv8tion.jda.api.components.selections.EntitySelectMenu.SelectTarget;
import net.dv8tion.jda.api.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MenuHandler {

    private final CafeBot cafeBot;

    public MenuHandler(CafeBot cafeBot) {
        this.cafeBot = cafeBot;
        cafeBot.getShardManager().addEventListener(new MenuListener(cafeBot));
    }
    
    public CompletableFuture<Map<MenuCategory, List<MenuItem>>> getFullMenu() {
        return cafeBot.getCafeAPI().getMenuApi().getMenuItems().thenApply((items) -> {
            Map<MenuCategory, List<MenuItem>> fullMenu = new HashMap<>();
            
            for (MenuItem item : items) {
                fullMenu.putIfAbsent(item.getCategory(), new ArrayList<>());
                fullMenu.get(item.getCategory()).add(item);
            }
            
            return fullMenu;
        });
    }

    public CompletableFuture<Optional<MenuItem>> getItem(MenuCategory category, int itemNumber) {
        return getFullMenu().thenApply((fullMenu) -> {
            try { return Optional.ofNullable(fullMenu.get(category).get(itemNumber)); }
            catch (IndexOutOfBoundsException e) { return Optional.empty(); }
        });
    }

    public CompletableFuture<List<MenuItem>> getMenu(MenuCategory category) {
        return getFullMenu().thenApply((fullMenu) -> fullMenu.get(category));
    }

    public MessageEmbed getAllMenuEmbed() {
        String menuString = """
                # Cafe Menu
                %s
                """;

        String categoryString = Arrays.stream(MenuCategory.values()).
                filter((category) -> !category.equals(MenuCategory.SECRET)).map((category) -> {
                    return String.format(
                            """
                            ### %d. %s
                            > %s
                            """, category.ordinal() + 1, category.getTitle(), category.getDescription()
                    );
                }).collect(Collectors.joining());

        String combined = String.format(menuString, categoryString);

        return new EmbedBuilder()
                .setColor(Helper.getRandomColor())
                .setDescription(combined)
                .setFooter("Want to request new menu items? Create a feature request with the \"/feature\" command!")
                .build();
    }

    public CompletableFuture<Optional<MessageEmbed>> getCategoryMenuEmbed(@Nullable MenuCategory category) {
        if (category == null) return CompletableFuture.completedFuture(Optional.empty());

        return this.getFullMenu().thenApply((menu) -> {
            String menuString = """
                # Cafe Menu
                ## %s
                *%s*~
                %s
                """;

            String itemString = IntStream.range(0, menu.get(category).size()).mapToObj((i) -> {
                MenuItem item = menu.get(category).get(i);
                return String.format(
                        """
                        ### %d. %s **(%.2f bC)**
                        > %s
                        """, i + 1, item.getName(), item.getPrice(), item.getDescription()
                );
            }).collect(Collectors.joining());

            String combined = String.format(menuString, category.getTitle(), category.getDescription(), itemString);

            return Optional.of(new EmbedBuilder()
                    .setColor(Helper.getRandomColor())
                    .setDescription(combined)
                    .setImage(category.getImageURL())
                    .setFooter("Want to request new menu items? Create a feature request with the \"/feature\" command!")
                    .build());
        });
    }

    public CompletableFuture<MessageEmbed> getItemEmbed(String itemName, DiscordUser user) {
        return this.getItemFromName(itemName).thenApply((menuItemOptional) -> {
            MenuItem menuItem = menuItemOptional.orElseThrow();
            String menuString = String.format("""
                # Cafe Menu
                ## %s **(%.2f bC)**
                ### %s
                *%s*~
                """, menuItem.getName(), menuItem.getPrice(), menuItem.getCategory(), menuItem.getDescription());

            return new EmbedBuilder()
                    .setColor(Helper.getRandomColor())
                    .setImage(menuItem.getImageUrl())
                    .setDescription(menuString)
                    .setFooter(String.format("You have %.2f CC - Item ID: %s", user.getBalance(), menuItem.getId()))
                    .build();
        });
    }

    private CompletableFuture<Optional<MenuItem>> getItemFromName(String itemName) {
        return cafeBot.getCafeAPI().getMenuApi().getMenuItems().thenApply((allItems) -> {
            return Arrays.stream(allItems).filter((item) -> item.getName().equalsIgnoreCase(itemName)).findAny();
        });
    }

    public StringSelectMenu getAllStringSelectMenu() {
        StringSelectMenu.Builder builder = StringSelectMenu.create("cafeBot:menu");
        builder.addOption("All", "ALL");
        Arrays.stream(MenuCategory.values()).forEach((category) -> builder.addOption(category.getTitle(), category.toString()));
        return builder.setPlaceholder("Choose Category").setMaxValues(1).setMinValues(1).build();
    }

    public CompletableFuture<StringSelectMenu> getItemStringSelectMenu(MenuCategory category) {
        return this.getFullMenu().thenApply((fullMenu) -> {
            StringSelectMenu.Builder builder = StringSelectMenu.create("cafeBot:menu:item");

            fullMenu.get(category).forEach((item) -> builder.addOption(item.getName(), item.getName()));

            return builder.setPlaceholder("Choose Item").setMaxValues(1).setMinValues(1).build();
        });

    }

    public CompletableFuture<EntitySelectMenu> getItemEntitySelectMenu(String itemString) {
        return this.getItemFromName(itemString).thenApply((itemOptional) -> {
            MenuItem item = itemOptional.orElseThrow();

            Builder builder = EntitySelectMenu.create("cafeBot:menu:user:" + item.getId(), SelectTarget.USER);
            return builder.setPlaceholder("Select User to Order For").setMaxValues(1).build();  // TODO: Is there a way to select multiple users without firing the event?
        });
    }

}
