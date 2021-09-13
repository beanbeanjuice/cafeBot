package com.beanbeanjuice.utility.sections.interaction;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import io.github.beanbeanjuice.cafeapi.cafebot.interactions.InteractionType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Interaction {

    private final InteractionType type;

    private final String noUserAction;;
    private final String multipleUsersAction;
    private String footer;

    private final User sender;
    private final ArrayList<User> receivers;

    private String message;

    /**
     * Creates a new {@link Interaction}.
     * @param type The {@link InteractionType type} of {@link Interaction}.
     * @param noUserAction The {@link String message} to send when there is no user.
     * @param multipleUsersAction The {@link String message} to send when there is a user.
     * @param footer The {@link String footer} to send.
     * @param sender The {@link User sender} of the {@link Interaction}.
     * @param arguments The {@link ArrayList} of {@link String argument}.
     * @param channel The {@link TextChannel} where the {@link Interaction} was sent.
     */
    public Interaction(@NotNull InteractionType type, @NotNull String noUserAction, @NotNull String multipleUsersAction,
                       @NotNull String footer, @NotNull User sender, @NotNull ArrayList<String> arguments,
                       @NotNull TextChannel channel) {
        this.type = type;
        this.noUserAction = noUserAction;
        this.multipleUsersAction = multipleUsersAction;
        this.footer = footer;

        this.sender = sender;

        receivers = new ArrayList<>();
        int count = 0;

        // Checking if there are users.
        if (!arguments.isEmpty()) {
            while (CafeBot.getGeneralHelper().getUser(arguments.get(count)) != null) {
                receivers.add(CafeBot.getGeneralHelper().getUser(arguments.get(count++)));
                if (count == arguments.size()) {
                    break;
                }
            }
        }

        // Builds the users.
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = count; i < arguments.size(); i++) {
            stringBuilder.append(arguments.get(i));
            if (i != arguments.size() - 1) {
                stringBuilder.append(" ");
            }
        }

        String url = CafeBot.getInteractionHandler().getImage(this.type);
        getMessageAndFooter();

        if (stringBuilder.isEmpty()) {
            channel.sendMessage(message).setEmbeds(actionEmbed(url, this.footer)).queue();
        } else {
            channel.sendMessage(message).setEmbeds(actionWithDescriptionEmbed(url, stringBuilder.toString(), this.footer)).queue();
        }
    }

    /**
     * Gets the message and footer according to the amount of {@link User receiver}.
     */
    private void getMessageAndFooter() {
        if (receivers.size() == 0) {
            message = noUserAction.replace("{sender}", sender.getName());

            footer = null;
        } else {
            message = multipleUsersAction.replace("{sender}", sender.getName()).replace("{receiver}", getReceiverString(receivers));

            if (receivers.size() == 1) {
                Integer sendAmount = null;
                Integer receiveAmount = null;
                try {
                    sendAmount = CafeBot.getInteractionHandler().getUserInteractionsSent(sender.getId(), type) + 1;
                    receiveAmount = CafeBot.getInteractionHandler().getUserInteractionsReceived(receivers.get(0).getId(), type) + 1;
                    CafeBot.getInteractionHandler().updateSender(sender.getId(), type, sendAmount);
                    CafeBot.getInteractionHandler().updateReceiver(receivers.get(0).getId(), type, receiveAmount);
                } catch (NullPointerException e) {
                    CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Getting Send/Receive Amounts: " + e.getMessage(), e);
                }


                footer = footer.replace("{sender}", sender.getName())
                        .replace("{amount_sent}", String.valueOf(sendAmount))
                        .replace("{receiver}", receivers.get(0).getName())
                        .replace("{amount_received}", String.valueOf(receiveAmount));
            } else {
                footer = null;
            }
        }
    }

    /**
     * Gets the receivers {@link String} for the Interaction Commands.
     * @param receivers The {@link ArrayList<User>} to be used as the receivers.
     * @return The new receiver {@link String}.
     */
    @NotNull
    private String getReceiverString(ArrayList<User> receivers) {
        StringBuilder receiverBuilder = new StringBuilder();

        for (int i = 0; i < receivers.size(); i++) {
            receiverBuilder.append(receivers.get(i).getName());

            if (i != receivers.size() - 1) {
                receiverBuilder.append(", ");
            }

            if (i == receivers.size() - 2) {
                receiverBuilder.append(" and ");
            }
        }
        return receiverBuilder.toString();
    }

    /**
     * @param link The image URL for the {@link MessageEmbed}.
     * @return The created {@link MessageEmbed}.
     */
    @NotNull
    private MessageEmbed actionEmbed(@Nullable String link, @Nullable String footer) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (link != null) {
            embedBuilder.setImage(link);
        }
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setFooter(footer);
        return embedBuilder.build();
    }

    /**
     * @param link The image URL for the {@link MessageEmbed}.
     * @param description The {@link String} description for the message embed.
     * @return The created {@link MessageEmbed}.
     */
    @NotNull
    private MessageEmbed actionWithDescriptionEmbed(@Nullable String link, @NotNull String description, @Nullable String footer) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (link != null) {
            embedBuilder.setImage(link);
        }
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());

        StringBuilder descriptionBuilder = new StringBuilder();
        descriptionBuilder.append("\"").append(description).append("\"");
        embedBuilder.setDescription(descriptionBuilder.toString());
        embedBuilder.setFooter(footer);
        return embedBuilder.build();
    }

}
