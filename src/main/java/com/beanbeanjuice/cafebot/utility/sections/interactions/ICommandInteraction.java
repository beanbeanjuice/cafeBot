package com.beanbeanjuice.cafebot.utility.sections.interactions;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.interactions.InteractionType;
import com.beanbeanjuice.cafebot.CafeBot;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface ICommandInteraction {

    default void handleInteraction(final InteractionType type, final SlashCommandInteractionEvent event, final CafeBot cafeBot) {
        CafeInteraction interaction = new CafeInteraction(
                type,
                this.getSelfString(),
                this.getOtherString(),
                this.getBotString(),
                this.getFooterString()
        );
        interaction.sendInteraction(cafeBot, event);
    }
    String getSelfString();
    String getOtherString();
    String getBotString();
    String getFooterString();

}
