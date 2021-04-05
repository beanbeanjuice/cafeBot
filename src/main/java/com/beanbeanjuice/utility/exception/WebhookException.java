package com.beanbeanjuice.utility.exception;

import org.jetbrains.annotations.NotNull;

/**
 * Used for when something goes wrong with {@link com.beanbeanjuice.utility.webhook.Webhook Webhook}.
 */
public class WebhookException extends RuntimeException {

    private final String url;

    /**
     * Create a new {@link WebhookException} instance.
     * @param url The url that caused the exception.
     * @param message The message for the exception.
     */
    public WebhookException(@NotNull String url, @NotNull String message) {
        super("[provided: " + url + "] " + message);

        this.url = url;
    }

    public WebhookException(@NotNull String url, @NotNull String message, @NotNull Throwable cause) {
        super("[provided: " + url + "] " + message, cause);

        this.url = url;
    }

    /**
     * @return Gets the url that triggered the exception.
     */
    public String getURL() {
        return url;
    }

}
