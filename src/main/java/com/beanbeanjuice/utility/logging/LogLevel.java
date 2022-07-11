package com.beanbeanjuice.utility.logging;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * A static class to be used with the {@link LogManager}.
 *
 * @author beanbeanjuice
 */
public enum LogLevel {

    DEBUG(Color.blue, "TEST", "TEST", "https://cdn.beanbeanjuice.com/images/cafeBot/log_level/debug.png"),
    LOADING(Color.orange, "LOADING", "LOAD", "https://cdn.beanbeanjuice.com/images/cafeBot/log_level/loading.gif"),
    OKAY(Color.green, "OKAY", "SUCC", "https://cdn.beanbeanjuice.com/images/cafeBot/log_level/okay.gif"),
    ERROR(Color.red, "ERROR", "ERR", "https://cdn.beanbeanjuice.com/images/cafeBot/log_level/error.webp"),
    INFO(Color.cyan, "INFO", "INFO", "https://cdn.beanbeanjuice.com/images/cafeBot/log_level/info.gif"),
    WARN(Color.orange, "WARN", "WARN", "https://cdn.beanbeanjuice.com/images/cafeBot/log_level/warn.gif");

    private final Color color;
    private final String message;
    private final String code;
    private final String imageURL;

    /**
     * Creates a new instance of the {@link LogLevel} enum.
     * @param color The {@link Color} to be used.
     * @param message The code to be shown.
     * @param imageURL The url for the image.
     */
    LogLevel(Color color, String message, String code, String imageURL) {
        this.color = color;
        this.message = message;
        this.code = code;
        this.imageURL = imageURL;
    }

    /**
     * @return The color to be displayed in the {@link net.dv8tion.jda.api.entities.MessageEmbed MessageEmbed}.
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return The code for the {@link LogLevel}.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return The image URL for the {@link LogLevel}.
     */
    public String getImageURL() {
        return imageURL;
    }

    /**
     * Gets the default code for the {@link LogLevel} chosen.
     * @return The code in its unedited, default state.
     */
    @NotNull
    public String getCode() {
        return code;
    }

    /**
     * Gets the formatted code for the {@link LogLevel} chosen.
     * @return The code in its formatted state.
     */
    @NotNull
    public String formatCode() {
        return code + (".").repeat(Math.max(0, 10 - code.length()));
    }

}
