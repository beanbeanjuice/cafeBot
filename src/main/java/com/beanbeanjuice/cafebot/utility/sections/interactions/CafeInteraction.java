package com.beanbeanjuice.cafebot.utility.sections.interactions;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.InteractionType;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.InteractionsEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.pictures.InteractionPicturesEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.users.Interaction;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CafeInteraction {

    private final InteractionType type;
    private String selfString;
    private String otherString;
    private final String botString;
    private final String footerString;

    public CafeInteraction(final InteractionType type, final String selfString, final String otherString,
                           final String botString, final String footerString) {
        this.type = type;
        this.selfString = selfString;
        this.otherString = otherString;
        this.botString = botString;
        this.footerString = footerString;
    }

    public void sendInteraction(final CafeBot cafeBot, final SlashCommandInteractionEvent event) {
        InteractionsEndpoint interactionsEndpoint = cafeBot.getCafeAPI().getInteractionsEndpoint();
        InteractionPicturesEndpoint picturesEndpoint = cafeBot.getCafeAPI().getInteractionPicturesEndpoint();

        Optional<OptionMapping> userMapping = Optional.ofNullable(event.getOption("user"));
        User sender = event.getUser();
        User receiver = userMapping.map(OptionMapping::getAsUser).orElse(event.getUser());

        Optional<OptionMapping> messageMapping = Optional.ofNullable(event.getOption("message"));
        Optional<String> additionalMessageOptional = messageMapping.map(OptionMapping::getAsString);

        CompletableFuture<Interaction> senderInteractionFuture = interactionsEndpoint.getAndCreateUserInteractionsSent(sender.getId());
        CompletableFuture<Interaction> receiverInteractionFuture = interactionsEndpoint.getAndCreateUserInteractionsSent(receiver.getId());
        CompletableFuture<Optional<String>> interactionPictureFuture = picturesEndpoint.getRandomInteractionPicture(this.type);

        senderInteractionFuture
                .thenCombineAsync(receiverInteractionFuture, Pair::of)
                .thenCombineAsync(interactionPictureFuture, (pair, optionalURL) -> {
                    int numSent = pair.getLeft().getInteractionAmount(type) + 1;
                    int numReceived = pair.getRight().getInteractionAmount(type) + 1;

                    this.selfString = String.format(selfString, sender.getAsMention());
                    this.otherString = String.format(otherString, sender.getAsMention(), receiver.getAsMention());

                    String message = (sender.getId().equals(receiver.getId())) ? selfString : otherString;
                    String footer = String.format(footerString, sender.getName(), numSent, receiver.getName(), numReceived);

                    EmbedBuilder embed = new EmbedBuilder();
                    optionalURL.ifPresent(embed::setImage);
                    additionalMessageOptional.ifPresent((description) -> embed.setDescription(String.format("\"%s\"", description)));
                    embed.setColor(Helper.getRandomColor());

                    if (!sender.getId().equalsIgnoreCase(receiver.getId())) {
                        embed.setFooter(footer);
                        interactionsEndpoint.updateSpecificUserInteractionSentAmount(sender.getId(), type, numSent);
                        interactionsEndpoint.updateSpecificUserInteractionReceivedAmount(receiver.getId(), type, numReceived);
                    }

                    event.getHook().sendMessage(message).mention(receiver).addEmbeds(embed.build()).queue((hook) -> {
                        if (!receiver.getId().equalsIgnoreCase(cafeBot.getJDA().getSelfUser().getId())) return;
                        hook.reply(botString).delay(1, TimeUnit.SECONDS).queue();
                    });

                    return true;
                });
    }

}
