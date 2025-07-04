package com.beanbeanjuice.cafebot.commands.settings.welcome.message;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.welcomes.GuildWelcome;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.welcomes.WelcomesEndpoint;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.listeners.WelcomeListener;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Map;

public class WelcomeMessageModalListener extends ListenerAdapter {

    private final WelcomesEndpoint welcomesEndpoint;

    public WelcomeMessageModalListener(final WelcomesEndpoint welcomesEndpoint) {
        this.welcomesEndpoint = welcomesEndpoint;
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (!event.getModalId().startsWith("cafeBot:modal:welcome:message:")) return;

        Map<String, String> values = Helper.modalValuesToMap(event.getValues());

        GuildWelcome guildWelcome = new GuildWelcome(
                event.getGuild().getId(),
                values.get("description"),
                values.get("thumbnail-url"),
                values.get("image-url"),
                values.get("message")
        );

        welcomesEndpoint.updateGuildWelcome(guildWelcome)
                .exceptionallyComposeAsync((e) -> welcomesEndpoint.createGuildWelcome(guildWelcome))
                .thenAcceptAsync((ignored) -> {
                    guildWelcome.getMessage().ifPresentOrElse(
                            (message) -> event.reply(message.replace("{user}", event.getUser().getAsMention())).addEmbeds(WelcomeListener.getWelcomeEmbed(guildWelcome, event.getUser())).setEphemeral(true).queue(),
                            () -> event.replyEmbeds(WelcomeListener.getWelcomeEmbed(guildWelcome, event.getUser())).setEphemeral(true).queue()
                    );
                })
                .exceptionallyAsync((e) -> {
                    event.replyEmbeds(Helper.errorEmbed(
                            "Error Setting Welcome Message",
                            String.format("There was an error setting the welcome message: %s", e.getMessage())
                    )).queue();
                    return null;
                });

    }

}
