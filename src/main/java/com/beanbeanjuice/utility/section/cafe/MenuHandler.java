package com.beanbeanjuice.utility.section.cafe;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.logging.LogLevel;
import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class used for handling the menu.
 *
 * @author beanbeanjuice
 */
public class MenuHandler {

    private static HashMap<CafeCategory, ArrayList<MenuItem>> menu = new HashMap<>();

    /**
     * Starts the {@link MenuHandler}.
     */
    public static void start() {
        menu = new HashMap<>();
        refreshMenu();
    }

    /**
     * Refreshes the menu if the file has changed.
     */
    public static void refreshMenu() {
        menu.clear();

        try {
            createMenuItems();
        } catch (IOException e) {
            Bot.getLogger().log(MenuHandler.class, LogLevel.ERROR, "Unable to refresh the menu.", true, true, e);
        }
    }

    /**
     * Creates the {@link MenuItem}.
     */
    private static void createMenuItems() throws IOException {
        JsonNode menuJSON = Helper.parseJson("menu.json");

        // Cycle through all possible categories.
        for (CafeCategory category : CafeCategory.values()) {
            ArrayList<MenuItem> section = new ArrayList<>();

            // Cycle through all JSON nodes corresponding to the CafeCategory
            for (JsonNode itemJSON : menuJSON.get(category.toString().toLowerCase()))
                section.add(new MenuItem(
                        category,
                        itemJSON.get("name").asText(),
                        itemJSON.get("price").asDouble(),
                        itemJSON.get("description").asText(),
                        itemJSON.get("image_url").asText()
                ));

            menu.put(category, section);
        }
    }

    /**
     * Gets the {@link MenuItem} from the {@link CafeCategory} and item number.
     * @param category The {@link CafeCategory} of the item.
     * @param itemNumber The item number.
     * @return The {@link MenuItem} searched for. Null if not found.
     */
    @Nullable
    public static MenuItem getItem(@NotNull CafeCategory category, @NotNull Integer itemNumber) {
        try {
            return menu.get(category).get(itemNumber);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Gets the {@link ArrayList<MenuItem>} for the {@link CafeCategory}.
     * @param category The {@link CafeCategory} to look in.
     * @return The {@link ArrayList<MenuItem>} for the {@link CafeCategory}.
     */
    @NotNull
    public static ArrayList<MenuItem> getMenu(@NotNull CafeCategory category) {
        return menu.get(category);
    }

}
