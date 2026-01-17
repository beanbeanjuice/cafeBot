package com.beanbeanjuice.cafebot.endpoints.discord.user;

import com.beanbeanjuice.cafebot.ApiTest;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.GameStatusType;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.GameType;
import com.beanbeanjuice.cafebot.api.wrapper.type.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class GameApiTest extends ApiTest {

    private String USER1;
    private String USER2;

    private ArrayList<Game> games;

    @BeforeEach
    public void createExampleGames() throws ExecutionException, InterruptedException {
        USER1 = generateSnowflake().toString();
        USER2 = generateSnowflake().toString();
        games = new ArrayList<>();
        String[] users = {USER1, USER2};

        for (GameType type : GameType.values()) {
            Game game = cafeAPI.getGamesApi().createGame(type, 0, users).get();

            games.add(game);
        }
    }

    @Test
    @DisplayName("can get game by id")
    public void getGameById() throws ExecutionException, InterruptedException {
        Game game = cafeAPI.getGamesApi().getGame(games.getFirst().getId()).get();

        Assertions.assertNotNull(game);
        Assertions.assertEquals(game.getId(), games.getFirst().getId());
    }

    @Test
    @DisplayName("can get games by user id")
    public void getUserGames() throws ExecutionException, InterruptedException {
        ArrayList<Game> games = cafeAPI.getGamesApi().getGames(USER1).get();

        Assertions.assertEquals(GameType.values().length, games.size());
    }

    @Test
    @DisplayName("can get games by user id filtered by type")
    public void getFilteredUserGames() throws ExecutionException, InterruptedException {
        ArrayList<Game> games = cafeAPI.getGamesApi().getGames(USER1, GameType.CONNECT_FOUR).get();

        Assertions.assertEquals(1, games.size());
    }

    @Test
    @DisplayName("can create games")
    public void createGames() throws ExecutionException, InterruptedException {
        for (GameType type : GameType.values()) {
            String user1 = generateSnowflake().toString();
            String user2 = generateSnowflake().toString();

            String[] users = {user1, user2};

            Game game = cafeAPI.getGamesApi().createGame(type, 100, users).get();

            Assertions.assertNotNull(game);
            Assertions.assertEquals(type, game.getType());
            Assertions.assertEquals(200, game.getPool());
            Assertions.assertEquals(100, game.getWager());
            Assertions.assertTrue(Arrays.stream(game.getPlayers()).toList().contains(user1));
            Assertions.assertTrue(Arrays.stream(game.getPlayers()).toList().contains(user2));
            Assertions.assertEquals(GameStatusType.IN_PROGRESS, game.getStatus());
        }
    }

    @Test
    @DisplayName("can update game")
    public void updateGame() throws ExecutionException, InterruptedException {
        Game game = cafeAPI.getGamesApi().updateGame(games.getFirst().getId(), GameStatusType.DRAW, new String[]{USER1}).get();

        Assertions.assertNotNull(game);
        Assertions.assertEquals(games.getFirst().getId(), game.getId());
        Assertions.assertEquals(GameStatusType.DRAW, game.getStatus());
    }

}
