package com.beanbeanjuice.cafebot.utility.listeners;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildInformationType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import com.fasterxml.jackson.databind.JsonNode;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AIResponseListener extends ListenerAdapter {

    private final CafeBot cafeBot;
    private final HashMap<List<String>, List<String>> messageMap;

    public AIResponseListener(final CafeBot cafeBot) {
        this.cafeBot = cafeBot;
        this.messageMap = new HashMap<>();

        refreshMaps();
    }

    public void refreshMaps() {
        this.messageMap.clear();

        try {
            createMaps();
        } catch (IOException e) {
            cafeBot.getLogger().log(AIResponseListener.class, LogLevel.ERROR, "Unable to refresh AI maps...", true, true, e);
        }
    }

    private void createMaps() throws IOException {
        for (JsonNode type : Helper.parseJson("ai.json")) {
            List<String> triggers = new ArrayList<>();
            List<String> responses = new ArrayList<>();

            for (JsonNode trigger : type.get("triggers")) triggers.add(trigger.asText());
            for (JsonNode response : type.get("responses")) responses.add(response.asText());

            messageMap.put(triggers, responses);
        }
    }

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getAuthor().isBot()) return;

        String guildID = event.getGuild().getId();

        cafeBot.getCafeAPI().getGuildsEndpoint().getGuildInformation(guildID).thenAcceptAsync((information) -> {
            boolean useAI = Boolean.parseBoolean(information.getSetting(GuildInformationType.AI_RESPONSE));
            if (!useAI) return;

            String message = event.getMessage().getContentRaw().replaceAll("[^\\sa-zA-Z0-9]", "");

            messageMap.forEach((commandTerms, commandResponses) -> {
                if (!commandTerms.contains(message)) return;

                event.getMessage().reply(parseMessage(
                        commandResponses.get(Helper.getRandomInteger(0, commandResponses.size())),
                        event.getAuthor()
                )).queue();
                cafeBot.increaseCommandsRun();
            });
        });
    }

    private String parseMessage(final String message, final User user) {
        return message.replace("{user}", user.getAsMention());
    }


}
