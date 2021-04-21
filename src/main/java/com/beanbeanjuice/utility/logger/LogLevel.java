package com.beanbeanjuice.utility.logger;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * A static class to be used with the {@link LogManager}.
 *
 * @author beanbeanjuice
 */
public enum LogLevel {

    DEBUG(Color.blue, "TEST", "TEST", "https://homepages.cae.wisc.edu/~ece533/images/lena.png"),
    LOADING(Color.orange, "LOADING", "LOAD", "https://i.pinimg.com/originals/5c/d3/57/5cd3570c93ae3dc5bd2cdb299d02115b.gif"),
    OKAY(Color.green, "OKAY", "SUCC", "https://cdn.discordapp.com/avatars/798978417994498061/4fb0f7469dada42c6b434c3cf7156e25.png"),
    ERROR(Color.red, "ERROR", "ERR", "https://cdn4.iconfinder.com/data/icons/social-messaging-ui-color-squares-01/3/28-512.png"),
    INFO(Color.cyan, "INFO", "INFO", "https://banner2.cleanpng.com/20171217/c7b/exclamation-mark-png-5a36968f3062a2.2892884615135269271982.jpg"),
    WARN(Color.orange, "WARN", "WARN", "https://cdn4.iconfinder.com/data/icons/social-messaging-ui-color-squares-01/3/28-512.png");

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
