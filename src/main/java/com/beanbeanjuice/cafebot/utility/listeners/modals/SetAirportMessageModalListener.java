package com.beanbeanjuice.cafebot.utility.listeners.modals;

import com.beanbeanjuice.cafebot.api.wrapper.CafeAPI;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.AirportMessageType;
import com.beanbeanjuice.cafebot.api.wrapper.type.airport.AirportMessage;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.listeners.AirportListener;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Map;

public class SetAirportMessageModalListener extends ListenerAdapter {

    public final CafeBot cafeBot;
    public final CafeAPI api;

    public SetAirportMessageModalListener(CafeBot cafeBot) {
        this.cafeBot = cafeBot;
        this.api = cafeBot.getCafeAPI();
    }

    private String emptyToNull(String value) {
        if (value == null) return null;
        value = value.trim();
        return value.isEmpty() ? null : value;
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        String modalId = event.getModalId();
        if (!modalId.startsWith("cafeBot:modal:airport:message:")) return;

        Map<String, String> values = Helper.modalValuesToMap(event.getValues());

        AirportMessageType type = AirportMessageType.valueOf(modalId.replace("cafeBot:modal:airport:message:", ""));

        AirportMessage airportMessageInProgress = new AirportMessage(
                event.getGuild().getId(),
                type,
                emptyToNull(values.get("title")),
                null, // used for later potentially
                null, // used for later potentially
                emptyToNull(values.get("image-url")),
                emptyToNull(values.get("thumbnail-url")),
                emptyToNull(values.get("description")),
                emptyToNull(values.get("message"))
        );

        api.getAirportApi().setAirportMessage(event.getGuild().getId(), airportMessageInProgress)
                        .thenAccept((airportMessage) -> {
                            airportMessage.getMessage().ifPresentOrElse(
                                    (message) -> event.reply(message.replace("{user}", event.getUser().getAsMention())).addEmbeds(AirportListener.getAirportEmbed(airportMessage, event.getUser())).setEphemeral(true).queue(),
                                    () -> event.replyEmbeds(AirportListener.getAirportEmbed(airportMessage, event.getUser())).setEphemeral(true).queue()
                            );
                        })
                        .exceptionally((ex) -> {
                            event.replyEmbeds(Helper.errorEmbed(
                                    "Error Setting Welcome Message",
                                    "There was an error setting the airport message... I'm sorry..."
                            )).queue();

                            this.cafeBot.getLogger().log(this.getClass(), LogLevel.WARN, String.format("Error Setting Airport Message: %s", ex.getMessage()), true, true);
                            return null;
                        });
    }

}
