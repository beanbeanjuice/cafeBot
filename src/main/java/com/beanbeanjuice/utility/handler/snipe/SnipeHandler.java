package com.beanbeanjuice.utility.handler.snipe;

import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * A handler used for sniping {@link net.dv8tion.jda.api.entities.Message} objects.
 *
 * @author beanbeanjuice
 * @since v3.1.0
 */
public class SnipeHandler {

    private static class PreSnipeTimer extends TimerTask {

        private final PreSnipeMessage preSnipeMessage;
        private PreSnipeTimer(@NotNull PreSnipeMessage preSnipeMessage) { this.preSnipeMessage = preSnipeMessage; }

        @Override
        public void run() { removePreSnipe(preSnipeMessage); }  // Remove when done.

    }

    private static class SnipeTimer extends TimerTask {

        private final SnipeMessage snipeMessage;
        private SnipeTimer(@NotNull SnipeMessage snipeMessage) { this.snipeMessage = snipeMessage; }

        @Override
        public void run() { removeSnipe(snipeMessage); }  // Remove when done.

    }

    protected static class PreSnipeMessage {

        protected final String channelID;
        protected final User user;
        protected final String messageID;
        protected final String message;
        protected Timer timer;

        protected PreSnipeMessage(@NotNull String channelID,
                             @NotNull User user, @NotNull String messageID, @NotNull String message) {
            this.channelID = channelID;
            this.user = user;
            this.messageID = messageID;
            this.message = message;
            timer = new Timer();
            start();
        }

        protected void start() {
            timer.schedule(new PreSnipeTimer(this), TimeUnit.MINUTES.toMillis(2));
        }

    }

    private static class SnipeMessage extends PreSnipeMessage {

        private SnipeMessage(@NotNull PreSnipeMessage snipeMessage) {
            super(snipeMessage.channelID, snipeMessage.user, snipeMessage.messageID, snipeMessage.message);
        }

        @Override
        protected void start() {
            timer.schedule(new SnipeTimer(this), TimeUnit.SECONDS.toMillis(30));
        }

        @NotNull
        private MessageEmbed getMessageEmbed() {
            return new EmbedBuilder()
                    .setAuthor(user.getName(), null, user.getAvatarUrl())
                    .setColor(Helper.getRandomColor())
                    .setDescription(message)
                    .setFooter("Message ID: " + messageID)
                    .build();
        }
    }

    private static final HashMap<String, HashMap<String, PreSnipeMessage>> preSnipes = new HashMap<>();
    private static final HashMap<String, Stack<SnipeMessage>> snipes = new HashMap<>();

    public static void addPreSnipe(@NotNull MessageReceivedEvent event) {
        // Channel IDs are unique. No need to store the guild ID.
        String channelID = event.getChannel().getId();
        User user = event.getAuthor();
        String messageID = event.getMessageId();
        String message = event.getMessage().getContentRaw();

        // Adds the guild to the snipe handler, if it is not in it yet.
        if (!preSnipes.containsKey(channelID))
            preSnipes.put(channelID, new HashMap<>());

        preSnipes.get(channelID).put(messageID, new PreSnipeMessage(channelID, user, messageID, message));
    }

    public static void moveSnipe(@NotNull String channelID, @NotNull String messageID) {
        // Checking if the message is in presnipes.
        if (preSnipes.containsKey(channelID) && preSnipes.get(channelID).containsKey(messageID)) {
            PreSnipeMessage preSnipeMessage = preSnipes.get(channelID).get(messageID);
            SnipeMessage snipeMessage = new SnipeMessage(preSnipeMessage);

            if (!snipes.containsKey(channelID))
                snipes.put(channelID, new Stack<>());

            snipes.get(channelID).add(snipeMessage);
            removePreSnipe(preSnipeMessage);
        }

    }

    private static void removePreSnipe(@NotNull PreSnipeMessage preSnipeMessage) {
        preSnipes.get(preSnipeMessage.channelID).remove(preSnipeMessage.messageID);
    }

    private static void removeSnipe(@NotNull SnipeMessage snipeMessage) {
        snipes.get(snipeMessage.channelID).remove(snipeMessage);
    }

    @Nullable
    public static MessageEmbed getLatestSnipe(@NotNull String channelID) {
        if (!snipes.containsKey(channelID) || snipes.get(channelID).isEmpty())
            return null;

        return snipes.get(channelID).pop().getMessageEmbed();
    }

}
