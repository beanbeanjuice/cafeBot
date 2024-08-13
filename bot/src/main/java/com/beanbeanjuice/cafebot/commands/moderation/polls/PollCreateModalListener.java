package com.beanbeanjuice.cafebot.commands.moderation.polls;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.polls.PollsEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.welcomes.GuildWelcome;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.welcomes.WelcomesEndpoint;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.listeners.WelcomeListener;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Map;

public class PollCreateModalListener extends ListenerAdapter {

    private final PollsEndpoint pollsEndpoint;

    public PollCreateModalListener(final PollsEndpoint pollsEndpoint) {
        this.pollsEndpoint = pollsEndpoint;
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (!event.getModalId().startsWith("cafeBot:modal:polls:create")) return;


    }

}
