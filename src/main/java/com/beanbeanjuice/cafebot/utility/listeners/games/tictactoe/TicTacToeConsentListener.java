package com.beanbeanjuice.cafebot.utility.listeners.games.tictactoe;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.handlers.games.TicTacToeHandler;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@RequiredArgsConstructor
public class TicTacToeConsentListener extends ListenerAdapter {

    private final CafeBot bot;

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getUser().isBot()) return;
        if (!event.isFromGuild()) return;

        String id = event.getComponentId();
        if (!id.startsWith(TicTacToeHandler.CONSENT_BUTTON_ID)) return;

        event.deferEdit().queue();

        parseEvent(event);
    }

    private void parseEvent(ButtonInteractionEvent event) {
        String[] splitId = event.getComponentId().replace(TicTacToeHandler.CONSENT_BUTTON_ID, "").split(":");
        String player1Id = splitId[0];
        String player2Id = splitId[1];
        int wager = Integer.parseInt(splitId[2]);

        Guild guild = event.getGuild();
        Member player2 = event.getMember(); // Person who clicked event must be player2.
        Member player1 = guild.getMemberById(player1Id);

        if (player1 == null || player2 == null) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Missing User",
                    "For some reason... one of you doesn't exist in my eyes... wh- what's going on?!?!"
            )).queue();
            return;
        }

        // player2 SHOULD be the one who pressed the event.
        if (!player2.getId().equals(player2Id)) return;

        bot.getTicTacToeHandler().startGame(event, player1, player2, wager);
    }

}
