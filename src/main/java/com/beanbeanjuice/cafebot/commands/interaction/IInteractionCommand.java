package com.beanbeanjuice.cafebot.commands.interaction;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.InteractionType;
import com.beanbeanjuice.cafebot.api.wrapper.api.exception.ApiRequestException;
import com.beanbeanjuice.cafebot.api.wrapper.type.Interaction;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import tools.jackson.databind.JsonNode;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public interface IInteractionCommand {

    default void handleInteraction(final InteractionType type, final SlashCommandInteractionEvent event, final CafeBot cafeBot) {
        Optional<OptionMapping> userMapping = Optional.ofNullable(event.getOption("user"));
        User sender = event.getUser();
        User receiver = userMapping.map(OptionMapping::getAsUser).orElse(event.getUser());

        Optional<OptionMapping> messageMapping = Optional.ofNullable(event.getOption("message"));
        Optional<String> additionalMessageOptional = messageMapping.map(OptionMapping::getAsString);

        CompletableFuture<String> interactionImageFuture = cafeBot.getCafeAPI().getInteractionsApi().getImage(type);
        CompletableFuture<Interaction> interactionFuture = cafeBot.getCafeAPI()
                .getInteractionsApi().createInteraction(sender.getId(), receiver.getId(), type)
                .exceptionally((ex) -> {
                    if (sender.getId().equals(receiver.getId())) return null; // ignore failure if its the same user

                    if (ex.getCause() instanceof ApiRequestException) throw new CompletionException(ex.getCause()); // Escalate exception.

                    cafeBot.getLogger().log(this.getClass(), LogLevel.WARN, "Error creating interaction: " + ex.getMessage(), true, true);
                    return null; // failed, but tolerated. We want to continue even if we can't create an interaction.
                });

        interactionImageFuture.thenCombine(interactionFuture, (imageUrl, interaction) -> {
            // Interaction may be null, but imageUrl should exist here.
            boolean isSelf = sender.getId().equals(receiver.getId());

            String rawMessage = (isSelf) ? this.getSelfString() : this.getOtherString();
            String message;

            if (isSelf) message = String.format(rawMessage, sender.getAsMention());
            else message = String.format(rawMessage, sender.getAsMention(), receiver.getAsMention());

            EmbedBuilder embedBuilder = new EmbedBuilder();

            additionalMessageOptional.ifPresent((additionalMessage) -> embedBuilder.setDescription(String.format("\"%s\"", additionalMessage)));
            embedBuilder.setImage(imageUrl);

            if (interaction != null) {
                String customFooter = String.format(this.getFooterString(), sender.getName(), interaction.getNumSentFrom(), receiver.getName(), interaction.getNumSentTo());
                String footer = String.format("%s - Use \"/interaction block\" or \"/interaction status\" to disable interactions!", customFooter);
                embedBuilder.setFooter(footer);
            }

            embedBuilder.setColor(Helper.getRandomColor());

            event.getHook().sendMessage(message).addEmbeds(embedBuilder.build()).queue((interactionMessage) -> {
                if (!receiver.getId().equals(cafeBot.getSelfUser().getId())) return;
                interactionMessage.reply(this.getBotString()).queue();
            });

            return null;
        }).exceptionally((ex) -> {
            if (ex.getCause() instanceof ApiRequestException apiRequestException) {
                JsonNode errorBody = apiRequestException.getBody().get("error");

                if (errorBody.has("to")) {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            "Can't Interact with User",
                            errorBody.get("to").get(0).asString()
                    )).queue();

                    throw new CompletionException(ex);
                }
            }

            event.getHook().sendMessageEmbeds(Helper.errorEmbed("Error Interacting", "I'm sorry 🥺 there was a problem interacting...")).queue();
            throw new CompletionException(ex);
        });
    }

    String getSelfString();
    String getOtherString();
    String getBotString();
    String getFooterString();

}
