package com.beanbeanjuice.utility.sections.interaction;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import io.github.beanbeanjuice.cafeapi.cafebot.interactions.InteractionType;
import io.github.beanbeanjuice.cafeapi.exception.CafeException;
import io.github.beanbeanjuice.cafeapi.exception.ConflictException;
import io.github.beanbeanjuice.cafeapi.exception.NotFoundException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A custom class used for {@link Interaction}.
 *
 * @author beanbeanjuice
 */
public class InteractionHandler {

    /**
     * Retrieves a random image {@link String URL} from the {@link io.github.beanbeanjuice.cafeapi.CafeAPI CafeAPI}.
     * @param type The {@link Interaction type} of image.
     * @return The {@link String URL} of the image.
     */
    @Nullable
    public String getImage(@NotNull InteractionType type) {
        try {
            return CafeBot.getCafeAPI().interactionPictures().getRandomInteractionPicture(type);
        } catch (CafeException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Getting Random Interaction Image: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Retrieve the {@link Integer amount} of {@link Interaction} sent by the specified {@link String userID} for the specified {@link InteractionType type}.
     * @param userID The specified {@link String userID}.
     * @param type The {@link InteractionType type} of {@link Interaction}.
     * @return The {@link Integer amount} of {@link Interaction} sent for that {@link InteractionType type}. Null, if there was an error.
     */
    @Nullable
    public Integer getUserInteractionsSent(@NotNull String userID, @NotNull InteractionType type) {
        if (!createInteractionSender(userID)) {
            try {
                return CafeBot.getCafeAPI().interactions().getSpecificUserInteractionSentAmount(userID, type);
            } catch (CafeException e) {
                return null;
            }
        }

        return 0;
    }

    /**
     * Retrieve the {@link Integer amount} of {@link Interaction} received by the specified {@link String userID} for the specified {@link InteractionType type}.
     * @param userID The specified {@link String userID}.
     * @param type The {@link InteractionType type} of {@link Interaction}.
     * @return The {@link Integer amount} of {@link Interaction} received for that {@link InteractionType type}. Null, if there was an error.
     */
    @Nullable
    public Integer getUserInteractionsReceived(@NotNull String userID, @NotNull InteractionType type) {
        if (!createInteractionReceiver(userID)) {
            try {
                return CafeBot.getCafeAPI().interactions().getSpecificUserInteractionReceivedAmount(userID, type);
            } catch (CafeException e) {
                return null;
            }
        }

        return 0;
    }

    /**
     * Creates an {@link Interaction} receiver user.
     * @param userID The specified {@link String userID}.
     * @return True, if the {@link Interaction} receiver was created successfully.
     */
    @NotNull
    private Boolean createInteractionReceiver(@NotNull String userID) {
        try {
            CafeBot.getCafeAPI().interactions().createUserInteractionsReceived(userID);
            return true;
        } catch (ConflictException ignored) {
            return false;
        }
        catch (CafeException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Creating Interaction Receiver: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Creates an {@link Interaction} sender user.
     * @param userID The specified {@link String userID}.
     * @return True, if the {@link Interaction} sender was created successfully.
     */
    @NotNull
    private Boolean createInteractionSender(@NotNull String userID) {
        try {
            CafeBot.getCafeAPI().interactions().createUserInteractionsSent(userID);
            return true;
        } catch (ConflictException ignored) {
            return false;
        }
        catch (CafeException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Creating Interaction Sender: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Updates the {@link Interaction} sender's {@link Integer amount} of {@link Interaction}.
     * @param userID The {@link String userID} of the {@link net.dv8tion.jda.api.entities.User sender}.
     * @param type The {@link InteractionType type} of {@link Interaction}.
     * @param amount The {@link Integer amount} of {@link Interaction}.
     * @return True, if the {@link Interaction} sender was updated successfully.
     */
    @NotNull
    public Boolean updateSender(@NotNull String userID, @NotNull InteractionType type, @NotNull Integer amount) {
        try {
            CafeBot.getCafeAPI().interactions().updateSpecificUserInteractionSentAmount(userID, type, amount);
            return true;
        } catch (NotFoundException e) {
            if (createInteractionSender(userID)) {
                return updateSender(userID, type, amount);
            } else {
                return false;
            }
        } catch (CafeException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Updating Interaction Sender: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Updates the {@link Interaction} receiver's {@link Integer amount} of {@link Interaction}.
     * @param userID The {@link String userID} of the {@link net.dv8tion.jda.api.entities.User receiver}.
     * @param type The {@link InteractionType type} of {@link Interaction}.
     * @param amount The {@link Integer amount} of {@link Interaction}.
     * @return True, if the {@link Interaction} receiver was updated successfully.
     */
    @NotNull
    public Boolean updateReceiver(@NotNull String userID, @NotNull InteractionType type, @NotNull Integer amount) {

        try {
            CafeBot.getCafeAPI().interactions().updateSpecificUserInteractionReceivedAmount(userID, type, amount);
            return true;
        } catch (NotFoundException e) {

            if (createInteractionReceiver(userID)) {
                return updateReceiver(userID, type, amount);
            } else {
                return false;
            }

        } catch (CafeException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Updating Interaction Receiver: " + e.getMessage(), e);
            return false;
        }
    }

}
