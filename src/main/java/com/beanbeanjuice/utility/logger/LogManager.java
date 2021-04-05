package com.beanbeanjuice.utility.logger;

import com.beanbeanjuice.utility.exception.WebhookException;
import com.beanbeanjuice.utility.time.Time;
import com.beanbeanjuice.utility.webhook.Webhook;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * A class used for logging.
 *
 * @author beanbeanjuice
 */
public class LogManager {

    private final String name;
    private Guild guild;
    private TextChannel logChannel;
    private ArrayList<String> webhookURLs;

    /**
     * Create a {@link LogManager LogManager} instance.
     * @param name The name for the {@link LogManager LogManager}.
     * @param guild The {@link Guild Guild} to be used for logging.
     * @param logChannel The {@link TextChannel TextChannel} to be used for logging.
     */
    public LogManager(@NotNull String name, @NotNull Guild guild, @NotNull TextChannel logChannel) {
        this.name = name;
        this.guild = guild;
        this.logChannel = logChannel;

        webhookURLs = new ArrayList<>(); // Creates the ArrayList

    }

    /**
     * Sets the {@link Guild} for the {@link LogManager}.
     * @param guild The {@link Guild} which contains the {@link TextChannel logChannel}.
     */
    public void setGuild(@NotNull Guild guild) {
        this.guild = guild;
    }

    /**
     * Sets the log channel for the {@link LogManager}.
     * @param logChannel The {@link TextChannel logChannel} for the {@link Guild}.
     */
    public void setLogChannel(@NotNull TextChannel logChannel) {
        this.logChannel = logChannel;
    }

    /**
     * Log to discord, webhook, and file.
     * @param c The class that called the log.
     * @param logLevel The current {@link LogLevel} of the log to be created.
     * @param message The message contents for the log.
     */
    public void log(@NotNull Class<?> c, @NotNull LogLevel logLevel, @NotNull String message) {
        log(c, logLevel, message, true, true);
    }

    /**
     * This is for optionally choosing to log to the webhook and Discord channel.
     * @param c The class that called the log.
     * @param logLevel The current {@link LogLevel} of the log to be created.
     * @param message The message contents for the log.
     */
    public void log(@NotNull Class<?> c, @NotNull LogLevel logLevel, @NotNull String message,
                    @NotNull Boolean logToWebhook, @NotNull Boolean logToLogChannel) {

        Time time = new Time(Calendar.getInstance(TimeZone.getDefault()));

        LoggerFactory.getLogger(c).info(message);

        if (logToWebhook) {
            logToWebhook(c, logLevel, message, time);
        }

        if (logToLogChannel) {
            logToLogChannel(c, logLevel, message, time);
        }
    }

    /**
     * Logs to the webhook URLs.
     * @param c The class that called the log.
     * @param logLevel The current {@link LogLevel} of the log to be created.
     * @param message The message contents for the log.
     */
    private void logToWebhook(@NotNull Class<?> c, @NotNull LogLevel logLevel, @NotNull String message, @NotNull Time time) {
        String temp = "``[" + time.toString("{HH}:{mm}:{ss} {Z}") + "]" + " [" + c.getName() + "/" + logLevel.toString() + "]: " + message + "``";

        temp = shortenToLimit(temp, 2000); // Shortens it to 2000 characters.

        for (String url : webhookURLs) {
            Webhook hook = new Webhook(url);

            hook.setUsername(name);
            hook.setContent(temp);

            try {
                hook.execute();
            } catch (IOException e) {
                throw new WebhookException(url, e.getMessage());
            }
        }
    }

    /**
     * Logs to the {@link TextChannel Discord Log Channel}.
     * @param c The class that called the log.
     * @param logLevel The current {@link LogLevel} of the log to be created.
     * @param message The message contents for the log.
     */
    private void logToLogChannel(@NotNull Class<?> c, @NotNull LogLevel logLevel, @NotNull String message, @NotNull Time time) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setAuthor(logLevel.toString());
        embedBuilder.setThumbnail(logLevel.getImageURL());
        embedBuilder.setDescription("``" + c.getSimpleName() + "``");
        embedBuilder.setTitle(shortenToLimit(message, 200));
        embedBuilder.setColor(logLevel.getColor());

        try {
            logChannel.sendMessage(embedBuilder.build()).complete();
        } catch (NullPointerException e) {
            //
        }
    }

    /**
     * Shorten the message to a certain limit.
     * @param message The contents of the message.
     * @param limit The limit of the new message.
     * @return The new, limited message.
     */
    @NotNull
    private String shortenToLimit(@NotNull String message, @NotNull Integer limit) {
        message = message.replace("\"", "\\\"");
        if (message.length() >= limit) {
            return message.substring(0, limit - 3) + "...";
        }

        return message;
    }

    /**
     * Add a {@link Webhook} URL that receives logs.
     * @param url The link for the webhook.
     */
    public void addWebhookURL(@NotNull String url) {
        webhookURLs.add(url);
    }

    /**
     * Remove a {@link Webhook} URL that receives logs.
     * @param url The link for the webhook.
     */
    public void removeWebhookURL(@NotNull String url) {
        webhookURLs.remove(url);
    }

}