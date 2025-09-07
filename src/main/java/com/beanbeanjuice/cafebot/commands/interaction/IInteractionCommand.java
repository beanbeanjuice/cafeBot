package com.beanbeanjuice.cafebot.commands.interaction;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.InteractionType;
import com.beanbeanjuice.cafebot.api.wrapper.type.Interaction;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
                String footer = String.format(this.getFooterString(), sender.getName(), interaction.getNumSentFrom(), receiver.getName(), interaction.getNumSentTo());
                embedBuilder.setFooter(footer);
            }

            embedBuilder.setColor(Helper.getRandomColor());

            event.getHook().sendMessage(message).addEmbeds(embedBuilder.build()).queue((interactionMessage) -> {
                if (!receiver.getId().equals(cafeBot.getSelfUser().getId())) return;
                interactionMessage.reply(this.getBotString()).queue();
            });

            return null;
        }).exceptionally((ex) -> {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed("Error Interacting", "I'm sorry 🥺 there was a problem interacting...")).queue();
            return null;
        });
    }

    String getSelfString();
    String getOtherString();
    String getBotString();
    String getFooterString();

}
