package com.beanbeanjuice.cafebot.commands.settings.goodbye.message;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.goodbyes.GoodbyesEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.goodbyes.GuildGoodbye;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.listeners.GoodbyeListener;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Map;

public class GoodbyeMessageModalListener extends ListenerAdapter {

    private final GoodbyesEndpoint goodbyesEndpoint;

    public GoodbyeMessageModalListener(final GoodbyesEndpoint goodbyesEndpoint) {
        this.goodbyesEndpoint = goodbyesEndpoint;
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (!event.getModalId().startsWith("cafeBot:modal:goodbye:message:")) return;

        Map<String, String> values = Helper.modalValuesToMap(event.getValues());

        GuildGoodbye guildGoodbye = new GuildGoodbye(
                event.getGuild().getId(),
                values.get("description"),
                values.get("thumbnail-url"),
                values.get("image-url"),
                values.get("message")
        );

        goodbyesEndpoint.updateGuildGoodbye(guildGoodbye)
                .exceptionallyComposeAsync((e) -> goodbyesEndpoint.createGuildGoodbye(guildGoodbye))
                .thenAcceptAsync((ignored) -> {
                    guildGoodbye.getMessage().ifPresentOrElse(
                            (message) -> event.reply(message.replace("{user}", event.getUser().getAsMention())).addEmbeds(GoodbyeListener.getGoodbyeEmbed(guildGoodbye, event.getUser())).setEphemeral(true).queue(),
                            () -> event.replyEmbeds(GoodbyeListener.getGoodbyeEmbed(guildGoodbye, event.getUser())).setEphemeral(true).queue()
                    );
                })
                .exceptionallyAsync((e) -> {
                    event.replyEmbeds(Helper.errorEmbed(
                            "Error Setting Goodbye Message",
                            String.format("There was an error setting the goodbye message: %s", e.getMessage())
                    )).queue();
                    return null;
                });

    }

}
