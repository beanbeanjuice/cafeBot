package com.beanbeanjuice.cafebot.utility.sections.cafe;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.cafe.CafeUser;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MenuHandler {

    private final CafeBot cafeBot;
    @Getter private final HashMap<CafeCategory, ArrayList<MenuItem>> menu;
    @Getter private final ArrayList<MenuItem> allItems;

    public MenuHandler(final CafeBot cafeBot) {
        this.cafeBot = cafeBot;
        this.menu = new HashMap<>();
        this.allItems = new ArrayList<>();
        refreshMenu();
        cafeBot.getJDA().addEventListener(new MenuListener(cafeBot));
    }

    public void refreshMenu() {
        this.menu.clear();
        this.allItems.clear();

        try {
            this.createMenuItems();
            this.cafeBot.getLogger().log(MenuHandler.class, LogLevel.INFO, "Successfully refreshed the menu items.");
        } catch (IOException e) {
            this.cafeBot.getLogger().log(MenuHandler.class, LogLevel.ERROR, "Unable to refresh the menu.", e);
        }
    }

    private void createMenuItems() throws IOException {
        JsonNode menuJSON = Helper.parseJson("menu.json");

        Arrays.stream(CafeCategory.values()).forEach((category) -> {
            ArrayList<MenuItem> section = new ArrayList<>();

            for (JsonNode itemJSON : menuJSON.get(category.toString().toLowerCase()))
                section.add(convertJSONNodeToMenuItem(category, itemJSON));

            menu.put(category, section);
            allItems.addAll(section);
        });
    }

    private MenuItem convertJSONNodeToMenuItem(final CafeCategory category, final JsonNode itemJSON) {
        return new MenuItem(
                category,
                itemJSON.get("name").asText(),
                itemJSON.get("price").asDouble(),
                itemJSON.get("description").asText(),
                itemJSON.get("image_url").asText()
        );
    }

    public Optional<MenuItem> getItem(final CafeCategory category, final int itemNumber) {
        try { return Optional.ofNullable(menu.get(category).get(itemNumber)); }
        catch (IndexOutOfBoundsException e) { return Optional.empty(); }
    }

    public ArrayList<MenuItem> getMenu(final CafeCategory category) {
        return menu.get(category);
    }

    public MessageEmbed getAllMenuEmbed() {
        String menuString = """
                # Cafe Menu
                %s
                """;

        String categoryString = Arrays.stream(CafeCategory.values()).
                filter((category) -> !category.equals(CafeCategory.SECRET)).map((category) -> {
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
                .build();
    }

    public MessageEmbed getCategoryMenuEmbed(CafeCategory category) {
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

        return new EmbedBuilder()
                .setColor(Helper.getRandomColor())
                .setDescription(combined)
                .setImage(category.getImageURL())
                .build();
    }

    public MessageEmbed getItemEmbed(final String itemName, final CafeUser user) {
        MenuItem menuItem = this.getItemFromName(itemName).orElseThrow();

        String menuString = String.format("""
                # Cafe Menu
                ## %s **(%.2f bC)**
                ### %s
                *%s*~
                """, menuItem.getName(), menuItem.getPrice(), menuItem.getCategory(), menuItem.getDescription());

        return new EmbedBuilder()
                .setColor(Helper.getRandomColor())
                .setImage(menuItem.getImageURL())
                .setDescription(menuString)
                .setFooter(String.format("You have %.2f bC", user.getBeanCoins()))
                .build();
    }

    private Optional<MenuItem> getItemFromName(final String itemName) {
        return allItems.stream().filter((item) -> item.getName().equalsIgnoreCase(itemName)).findAny();
    }

    private int convertItemToIndex(final String itemName) {
        for (int i = 0; i < allItems.size(); i++) {
            if (allItems.get(i).getName().equalsIgnoreCase(itemName)) return i;
        }
        return -1;
    }

    public StringSelectMenu getAllStringSelectMenu() {
        StringSelectMenu.Builder builder = StringSelectMenu.create("menu:id");
        builder.addOption("All", "ALL");
        Arrays.stream(CafeCategory.values()).forEach((category) -> builder.addOption(category.getTitle(), category.toString()));
        return builder.setPlaceholder("Choose Category").setMaxValues(1).setMinValues(1).build();
    }

    public StringSelectMenu getItemStringSelectMenu(final CafeCategory category) {
        StringSelectMenu.Builder builder = StringSelectMenu.create("menu-item:id");
        menu.get(category).forEach((item) -> builder.addOption(item.getName(), item.getName()));
        return builder.setPlaceholder("Choose Item").setMaxValues(1).setMinValues(1).build();
    }

    public EntitySelectMenu getItemEntitySelectMenu(final String itemString) {
        int itemID = convertItemToIndex(itemString);
        EntitySelectMenu.Builder builder = EntitySelectMenu.create("menu-entity:" + itemID, EntitySelectMenu.SelectTarget.USER);
        return builder.setPlaceholder("Select User to Order For").setMaxValues(1).build();  // TODO: Is there a way to select multiple users without firing the event?
    }

}
