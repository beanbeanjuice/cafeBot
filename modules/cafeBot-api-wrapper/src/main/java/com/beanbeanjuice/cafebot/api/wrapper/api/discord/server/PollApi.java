package com.beanbeanjuice.cafebot.api.wrapper.api.discord.server;

import com.beanbeanjuice.cafebot.api.wrapper.RequestBuilder;
import com.beanbeanjuice.cafebot.api.wrapper.api.Api;
import com.beanbeanjuice.cafebot.api.wrapper.response.BasicResponse;
import com.beanbeanjuice.cafebot.api.wrapper.type.poll.PartialPoll;
import com.beanbeanjuice.cafebot.api.wrapper.type.poll.Poll;
import com.beanbeanjuice.cafebot.api.wrapper.type.poll.PollOption;
import org.apache.hc.core5.http.Method;
import tools.jackson.databind.JsonNode;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PollApi extends Api {

    public PollApi(String baseUrl, String token) {
        super(baseUrl, token);
    }

    public CompletableFuture<Map<String, List<Poll>>> getPolls() {
        return getPolls(true, true);
    }

    public CompletableFuture<Map<String, List<Poll>>> getPolls(boolean isActive, boolean isExpired) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/polls?active=%s&expired=%s", isActive, isExpired))
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("polls"))
                    .thenApply((pollsNode) -> {
                        HashMap<String, List<Poll>> polls = new HashMap<>();

                        for (JsonNode pollNode : pollsNode) {
                            Poll poll = parsePoll(pollNode);

                            polls.putIfAbsent(poll.getGuildId(), new ArrayList<>());
                            polls.get(poll.getGuildId()).add(poll);
                        }

                        return polls;
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<List<Poll>> getPolls(String guildId) {
        return getPolls(guildId, true, true);
    }

    public CompletableFuture<List<Poll>> getPolls(String guildId, boolean isActive, boolean isExpired) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/polls/%s?active=%s&expired=%s", guildId, isActive, isExpired))
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("polls"))
                    .thenApply((pollsNode) -> {
                        List<Poll> polls = new ArrayList<>();

                        for (JsonNode pollNode : pollsNode) {
                            polls.add(parsePoll(pollNode));
                        }

                        return polls;
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<List<PollOption>> getPollOptions(int pollId) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/polls/%s/options", pollId))
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("options"))
                    .thenApply((optionsNode) -> {
                        List<PollOption> options = new ArrayList<>();

                        for (JsonNode optionNode : optionsNode) {
                            options.add(parsePollOption(optionNode));
                        }

                        return options;
                    });
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Poll> setVote(int pollId, int optionId, String userId, boolean status) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        try {
            return RequestBuilder.builder()
                    .method(Method.POST)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/polls/%s/%s/vote?status=%s", pollId, optionId, status))
                    .body(map)
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("poll"))
                    .thenApply(this::parsePoll);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Poll> toggleVote(int pollId, int optionId, String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        try {
            return RequestBuilder.builder()
                    .method(Method.POST)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/polls/%s/%s/vote", pollId, optionId))
                    .body(map)
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("poll"))
                    .thenApply(this::parsePoll);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Poll> closePoll(int pollId) {
        try {
            return RequestBuilder.builder()
                    .method(Method.PATCH)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/polls/%s", pollId))
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("poll"))
                    .thenApply(this::parsePoll);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Poll> createPoll(String guildId, String messageId, PartialPoll poll) {
        List<Map<String, Object>> options = new ArrayList<>();

        Arrays.stream(poll.getOptions()).forEach(option -> {
            Map<String, Object> optionMap = new HashMap<>();

            optionMap.put("title", option.getTitle());
            option.getEmoji().ifPresent((emoji) -> optionMap.put("emoji", emoji));
            option.getDescription().ifPresent((description) -> optionMap.put("description", description));

            options.add(optionMap);
        });

        Map<String, Object> map = new HashMap<>();

        map.put("messageId", messageId);
        map.put("title", poll.getTitle());
        poll.getDescription().ifPresent((description) -> map.put("description", description));
        map.put("allowMultiple", poll.isAllowMultiple());
        map.put("options", options.toArray());
        map.put("endsAt", poll.getEndsAt().toEpochMilli());

        try {
            return RequestBuilder.builder()
                    .method(Method.POST)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/polls/%s", guildId))
                    .body(map)
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("poll"))
                    .thenApply(this::parsePoll);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Poll> getPoll(String guildId, String messageId) {
        try {
            return RequestBuilder.builder()
                    .method(Method.GET)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/polls/%s/poll/%s", guildId, messageId))
                    .queue()
                    .thenApply(BasicResponse::getBody)
                    .thenApply((node) -> node.get("poll"))
                    .thenApply(this::parsePoll);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    public CompletableFuture<Void> deletePoll(int pollId) {
        try {
            return RequestBuilder.builder()
                    .method(Method.DELETE)
                    .baseUrl(baseUrl)
                    .token(token)
                    .route(String.format("/api/v4/discord/servers/polls/%s", pollId))
                    .queue()
                    .thenApply(res -> null);
        } catch (Exception e) {
            throw new RuntimeException("Invalid route: " + e.getMessage());
        }
    }

    private Poll parsePoll(JsonNode node) {
        int id = node.get("id").asInt();
        String guildId = node.get("guildId").asString();
        String messageId = node.get("messageId").asString();

        String title = node.get("title").asString();
        String description = node.get("description").isNull() ? null : node.get("description").asString();
        boolean allowMultiple = node.get("allowMultiple").asBoolean();

        boolean isActive = node.get("active").asBoolean();
        String endsAt = node.get("endsAt").asString();

        List<PollOption> options = parsePollOptions(node.get("options"));
        List<PollOption> results = parsePollOptions(node.get("results"));

        return new Poll(
                id, guildId, messageId,
                title, description, allowMultiple,
                isActive, endsAt,
                options.toArray(new PollOption[0]), results.toArray(new PollOption[0])
        );
    }

    private List<PollOption> parsePollOptions(JsonNode node) {
        List<PollOption> options = new ArrayList<>();

        for (JsonNode optionNode : node) {
            options.add(parsePollOption(optionNode));
        }

        return options;
    }

    private PollOption parsePollOption(JsonNode node) {
        int id = node.get("id").asInt();
        int pollId = node.get("pollId").asInt();

        String emoji = node.get("emoji").isNull() ? null : node.get("emoji").asString(null);
        String title = node.get("title").asString();
        String description = node.get("description").isNull() ? null : node.get("description").asString(null);

        List<String> voters = new ArrayList<>();
        for (JsonNode voterNode : node.get("voters")) {
            voters.add(voterNode.get("id").asString());
        }

        return new PollOption(id, pollId, emoji, title, description, voters.toArray(new String[0]));
    }

}
