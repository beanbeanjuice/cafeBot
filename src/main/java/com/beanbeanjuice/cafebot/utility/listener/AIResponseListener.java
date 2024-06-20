package com.beanbeanjuice.cafebot.utility.listener;

import com.beanbeanjuice.cafebot.Bot;
import com.beanbeanjuice.cafebot.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import com.fasterxml.jackson.databind.JsonNode;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A {@link ListenerAdapter} that listens to {@link MessageReceivedEvent} and gives an
 * appropriate response depending on if it is enabled in the {@link Guild}.
 *
 * @author beanbeanjuice
 * @since v3.0.0
 */
public class AIResponseListener extends ListenerAdapter {

    private final HashMap<ArrayList<String>, ArrayList<String>> messageMap;

    /**
     * Create a new {@link AIResponseListener} object.
     */
    public AIResponseListener() {
        messageMap = new HashMap<>();
        refreshMaps();
    }

    /**
     * Refreshes the AI maps if the file has changed.
     */
    public void refreshMaps() {
        messageMap.clear();

        try {
            createMaps();
        } catch (IOException e) {
            Bot.getLogger().log(AIResponseListener.class, LogLevel.ERROR, "Unable to refresh AI maps...", true, true, e);
        }
    }

    private void createMaps() throws IOException {
        for (JsonNode type : Helper.parseJson("ai.json")) {
            ArrayList<String> triggers = new ArrayList<>();
            ArrayList<String> responses = new ArrayList<>();

            /*
                Adds the trigger.
                Deletes all whitespaces.
                Deletes all characters that are not letters or numbers.
             */
            for (JsonNode trigger : type.get("triggers"))
                triggers.add(trigger.asText().replaceAll("[^a-zA-Z0-9]", ""));

            for (JsonNode response : type.get("responses"))
                responses.add(response.asText());

            messageMap.put(triggers, responses);
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (!event.isFromGuild())
            return;

        try {
            Guild guild = event.getGuild();

            if (event.getAuthor().isBot())
                return;

            if (!GuildHandler.getCustomGuild(guild).getAIState())
                return;

            String message = event.getMessage().getContentRaw().toLowerCase();

            messageMap.forEach((commandTerms, commandResponses) -> {
                /*
                    Replaces all the messages such that it only contains letters and numbers.
                    This includes removing whitespaces.
                 */
                if (commandTerms.contains(message.replaceAll("[^a-zA-Z0-9]", ""))) {
                    event.getMessage().reply(parseMessage(
                            commandResponses.get(Helper.getRandomNumber(0, commandResponses.size())),
                            event.getAuthor()
                    )).queue();
                    Bot.commandsRun++;
                }
            });
        } catch (NullPointerException ignored) {}
    }

    @NotNull
    private String parseMessage(@NotNull String message, @NotNull User user) {
        return message.replace("{user}", user.getAsMention());
    }

}
