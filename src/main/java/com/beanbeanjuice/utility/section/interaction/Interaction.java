package com.beanbeanjuice.utility.section.interaction;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.logging.LogLevel;
import com.beanbeanjuice.cafeapi.cafebot.interactions.InteractionType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A class used for creating an {@link Interaction} between users.
 *
 * @author beanbeanjuice
 */
public class Interaction {
    private final InteractionType type;

    private final String noUserAction;;
    private final String userAction;
    private String footer;
    private final String selfMessage;

    private final User sender;
    private User receiver;

    private String message;
    private String description;

    /**
     * Creates a new {@link Interaction} object.
     * @param type The {@link InteractionType type} of {@link Interaction}.
     * @param noUserAction The {@link String message} to send when no {@link User} is specified.
     * @param userAction The {@link String message} to send when a {@link User} is specified.
     * @param footer The {@link String footer} to attach to the {@link MessageEmbed}.
     * @param selfMessage The {@link String message} to send to the user, if the receiver is the {@link Bot}.
     * @param event The {@link SlashCommandInteractionEvent event} that triggered the {@link Interaction}.
     */
    public Interaction(@NotNull InteractionType type, @NotNull String noUserAction, @NotNull String userAction,
                       @NotNull String footer, @NotNull String selfMessage, @NotNull SlashCommandInteractionEvent event) {
        this.type = type;
        this.noUserAction = noUserAction;
        this.userAction = userAction;
        this.footer = footer;
        this.selfMessage = selfMessage;

        this.sender = event.getUser();

        /*
            We need to check if the receiver/description are null here.
            I'm not sure why it's not just Nullable....
         */
        this.receiver = null;
        try {
            this.receiver = event.getOption("receiver").getAsUser();
        } catch (NullPointerException ignored) {}


        this.description = null;
        try {
            this.description = event.getOption("message").getAsString();
        } catch (NullPointerException ignored) {}

        String url = InteractionHandler.getImage(this.type);
        getMessageAndFooter();

        event.getHook().sendMessage(this.message).addEmbeds(actionEmbed(url, this.footer)).queue();

        // Sends a message if CafeBot is the one who receives the interaction.
        if (this.containsCafeBot())
            event.getChannel().sendMessage(selfMessage).queue();
    }

    /**
     * Gets the message and footer according to the amount of {@link User receiver}.
     */
    private void getMessageAndFooter() {
        if (receiver == null) {
            message = noUserAction.replace("{sender}", sender.getName());

            footer = null;
        } else {
            message = userAction.replace("{sender}", sender.getName()).replace("{receiver}", receiver.getAsMention());

            Integer sendAmount = null;
            Integer receiveAmount = null;
            try {
                sendAmount = InteractionHandler.getUserInteractionsSent(sender.getId(), type) + 1;
                receiveAmount = InteractionHandler.getUserInteractionsReceived(receiver.getId(), type) + 1;
                InteractionHandler.updateSender(sender.getId(), type, sendAmount);
                InteractionHandler.updateReceiver(receiver.getId(), type, receiveAmount);
            } catch (NullPointerException e) {
                Bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Getting Send/Receive Amounts: " + e.getMessage(), e);
            }

            footer = footer.replace("{sender}", sender.getName())
                    .replace("{amount_sent}", String.valueOf(sendAmount))
                    .replace("{receiver}", receiver.getName())
                    .replace("{amount_received}", String.valueOf(receiveAmount));
        }
    }

    /**
     * @param link The image URL for the {@link MessageEmbed}.
     * @return The created {@link MessageEmbed}.
     */
    @NotNull
    private MessageEmbed actionEmbed(@Nullable String link, @Nullable String footer) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (link != null)
            embedBuilder.setImage(link);

        if (description != null)
            embedBuilder.setDescription("\"" + description + "\"");

        embedBuilder.setColor(Helper.getRandomColor());
        embedBuilder.setFooter(footer);
        return embedBuilder.build();
    }

    /**
     * Checks if the {@link Interaction} contains the bot.
     * @return True, if the {@link Interaction} contains the bot.
     */
    @NotNull
    public Boolean containsCafeBot() {
        if (receiver == null)
            return false;

        return receiver.getId().equals(Bot.getBot().getSelfUser().getId());
    }

}
