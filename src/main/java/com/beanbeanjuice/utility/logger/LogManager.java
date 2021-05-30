package com.beanbeanjuice.utility.logger;

import com.beanbeanjuice.main.websocket.controller.ChatController;
import com.beanbeanjuice.main.websocket.model.ChatMessage;
import com.beanbeanjuice.main.websocket.model.MessageType;
import com.beanbeanjuice.utility.exception.WebhookException;
import com.beanbeanjuice.utility.time.Time;
import com.beanbeanjuice.utility.webhook.Webhook;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * A class used for logging.
 *
 * @author beanbeanjuice
 */
public class LogManager {

    private final String name;
    private TextChannel logChannel;
    private ArrayList<String> webhookURLs;

    /**
     * Create a {@link LogManager LogManager} instance.
     * @param name The name for the {@link LogManager LogManager}.
     * @param logChannel The {@link TextChannel TextChannel} to be used for logging.
     */
    public LogManager(@NotNull String name, @NotNull TextChannel logChannel) {
        this.name = name;
        this.logChannel = logChannel;

        webhookURLs = new ArrayList<>(); // Creates the ArrayList
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

        Logger logger = LoggerFactory.getLogger(c);

        switch (logLevel) {
            case INFO, LOADING, OKAY -> {
                logger.info(message);
            }

            case WARN -> {
                logger.warn(message);
            }

            case DEBUG -> {
                logger.debug(message);
            }

            case ERROR -> {
                logger.error(message);
            }

        }

        if (logToWebhook) {
            logToWebhook(c, logLevel, message, time);
        }

        if (logToLogChannel) {
            logToLogChannel(c, logLevel, message, time);
        }

        String formattedMessage = "``[" + time.toString("{HH}:{mm}:{ss} {Z}") + "]" + " [" + c.getName() + "/" + logLevel.toString() + "]: " + message + "``";
        ChatMessage chatMessage = ChatMessage.builder()
                .type(MessageType.CHAT)
                .sender("CONSOLE")
                .content(formattedMessage)
                .build();

        // TODO: Log this chatMessage.
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
        embedBuilder.setTitle("`" + c.getSimpleName() + "`");
        embedBuilder.setDescription(shortenToLimit(message, 4000));
        embedBuilder.setColor(logLevel.getColor());
        embedBuilder.setTimestamp(new Date().toInstant());

        try {
            logChannel.sendMessage(embedBuilder.build()).complete();
        } catch (NullPointerException ignored) {}
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