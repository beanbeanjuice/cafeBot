package com.beanbeanjuice.cafebot.utility.section.interaction;

import com.beanbeanjuice.cafebot.Bot;
import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.InteractionType;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.CafeException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.NotFoundException;

import java.util.Optional;

/**
 * A class used for handling {@link Interaction} between users.
 *
 * @author beanbeanjuice
 */
public class InteractionHandler {

    /**
     * Retrieves a random image {@link String URL} from the {@link CafeAPI CafeAPI}.
     *
     * @param type The {@link Interaction type} of image.
     * @return The {@link String URL} of the image.
     */
    public static Optional<String> getImage(final InteractionType type) {
        try {
            return Optional.of(Bot.getCafeAPI().getInteractionPicturesEndpoint().getRandomInteractionPicture(type));
        } catch (CafeException e) {
            Bot.getLogger().log(InteractionHandler.class, LogLevel.WARN, "Error Getting Random Interaction Image: " + e.getMessage(), e);
            return Optional.empty();
        }
    }

    /**
     * Retrieve the {@link Integer amount} of {@link Interaction} sent by the specified {@link String userID} for the specified {@link InteractionType type}.
     *
     * @param userID The specified {@link String userID}.
     * @param type   The {@link InteractionType type} of {@link Interaction}.
     * @return The {@link Optional<Integer> amount} of {@link Interaction} sent for that {@link InteractionType type}.
     */
    public static Optional<Integer> getUserInteractionsSent(final String userID, final InteractionType type) {
        if (createInteractionSender(userID)) return Optional.of(0);

        try {
            return Optional.of(Bot.getCafeAPI().getInteractionsEndpoint().getSpecificUserInteractionSentAmount(userID, type));
        } catch (CafeException e) {
            return Optional.empty();
        }
    }

    /**
     * Retrieve the {@link Integer amount} of {@link Interaction} received by the specified {@link String userID} for the specified {@link InteractionType type}.
     *
     * @param userID The specified {@link String userID}.
     * @param type   The {@link InteractionType type} of {@link Interaction}.
     * @return The {@link Integer amount} of {@link Interaction} received for that {@link InteractionType type}. Null, if there was an error.
     */
    public static Optional<Integer> getUserInteractionsReceived(final String userID, final InteractionType type) {
        if (createInteractionReceiver(userID)) return Optional.of(0);

        try {
            return Optional.of(Bot.getCafeAPI().getInteractionsEndpoint().getSpecificUserInteractionReceivedAmount(userID, type));
        } catch (CafeException e) {
            return Optional.empty();
        }
    }

    /**
     * Creates an {@link Interaction} receiver user.
     *
     * @param userID The specified {@link String userID}.
     * @return True, if the {@link Interaction} receiver was created successfully.
     */
    private static boolean createInteractionReceiver(final String userID) {
        try {
            Bot.getCafeAPI().getInteractionsEndpoint().createUserInteractionsReceived(userID);
            return true;
        } catch (ConflictException e) {
            return false;
        } catch (CafeException e) {
            Bot.getLogger().log(InteractionHandler.class, LogLevel.WARN, "Error Creating Interaction Receiver: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Creates an {@link Interaction} sender user.
     *
     * @param userID The specified {@link String userID}.
     * @return True, if the {@link Interaction} sender was created successfully.
     */
    private static boolean createInteractionSender(final String userID) {
        try {
            Bot.getCafeAPI().getInteractionsEndpoint().createUserInteractionsSent(userID);
            return true;
        } catch (ConflictException ignored) {
            return false;
        } catch (CafeException e) {
            Bot.getLogger().log(InteractionHandler.class, LogLevel.WARN, "Error Creating Interaction Sender: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Updates the {@link Interaction} sender's {@link Integer amount} of {@link Interaction}.
     *
     * @param userID The {@link String userID} of the {@link net.dv8tion.jda.api.entities.User sender}.
     * @param type   The {@link InteractionType type} of {@link Interaction}.
     * @param amount The {@link Integer amount} of {@link Interaction}.
     * @return True, if the {@link Interaction} sender was updated successfully.
     */
    public static boolean updateSender(final String userID, final InteractionType type, final int amount) {
        try {
            Bot.getCafeAPI().getInteractionsEndpoint().updateSpecificUserInteractionSentAmount(userID, type, amount);
            return true;
        } catch (NotFoundException e) {
            if (createInteractionSender(userID)) return updateSender(userID, type, amount);
            else return false;
        } catch (CafeException e) {
            Bot.getLogger().log(InteractionHandler.class, LogLevel.WARN, "Error Updating Interaction Sender: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Updates the {@link Interaction} receiver's {@link Integer amount} of {@link Interaction}.
     *
     * @param userID The {@link String userID} of the {@link net.dv8tion.jda.api.entities.User receiver}.
     * @param type   The {@link InteractionType type} of {@link Interaction}.
     * @param amount The {@link Integer amount} of {@link Interaction}.
     * @return True, if the {@link Interaction} receiver was updated successfully.
     */
    public static boolean updateReceiver(final String userID, final InteractionType type, final int amount) {
        try {
            Bot.getCafeAPI().getInteractionsEndpoint().updateSpecificUserInteractionReceivedAmount(userID, type, amount);
            return true;
        } catch (NotFoundException e) {
            if (createInteractionReceiver(userID)) return updateReceiver(userID, type, amount);
            else return false;
        } catch (CafeException e) {
            Bot.getLogger().log(InteractionHandler.class, LogLevel.WARN, "Error Updating Interaction Receiver: " + e.getMessage(), e);
            return false;
        }
    }
}
