package com.beanbeanjuice.utility.listener;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.logging.LogLevel;
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

        try {
            createMaps();
        } catch (IOException e) {
            Bot.getLogger().log(AIResponseListener.class, LogLevel.ERROR, "There was an error establishing AI responses.", false, true, e);
        }
    }

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

            for (JsonNode trigger : type.get("triggers"))
                triggers.add(trigger.asText());

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
                if (commandTerms.contains(message.replaceAll("[^\\sa-zA-Z0-9]", ""))) {
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
