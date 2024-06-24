package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.minigames.winstreaks.MinigameType;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.NotFoundException;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

public class WinStreaksEndpointTests {

    @Test
    @DisplayName("Win Streaks Endpoint Test")
    public void testWinStreakEndpoint() throws ExecutionException, InterruptedException {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        Assertions.assertTrue(cafeAPI.getWinStreaksEndpoint().deleteUserWinStreak("879310496002637824").get());

        cafeAPI.getWinStreaksEndpoint().updateUserWinStreak("879310496002637824", MinigameType.TIC_TAC_TOE, 100)
                .thenAcceptAsync((isSuccessful) -> Assertions.fail())
                .exceptionallyAsync((exception) -> {
                    Assertions.assertInstanceOf(NotFoundException.class, exception.getCause());
                    return null;
                }).join();

        cafeAPI.getWinStreaksEndpoint().getUserWinStreak("879310496002637824")
                .thenAcceptAsync((winStreak) -> Assertions.fail())
                .exceptionallyAsync((exception) -> {
                    Assertions.assertInstanceOf(NotFoundException.class, exception.getCause());
                    return null;
                }).join();

        Assertions.assertTrue(cafeAPI.getWinStreaksEndpoint().createUserWinStreak("879310496002637824").get());

        cafeAPI.getWinStreaksEndpoint().createUserWinStreak("879310496002637824")
                .thenAcceptAsync((isSuccessful) -> Assertions.fail())
                .exceptionallyAsync((exception) -> {
                    Assertions.assertInstanceOf(ConflictException.class, exception.getCause());
                    return null;
                }).join();

        Assertions.assertEquals(0, cafeAPI.getWinStreaksEndpoint().getUserWinStreak("879310496002637824").get().getTicTacToeWins());
        Assertions.assertEquals(0, cafeAPI.getWinStreaksEndpoint().getUserWinStreak("879310496002637824").get().getConnectFourWins());

        Assertions.assertTrue(cafeAPI.getWinStreaksEndpoint().updateUserWinStreak("879310496002637824", MinigameType.CONNECT_FOUR, 20).get());
        Assertions.assertEquals(20, cafeAPI.getWinStreaksEndpoint().getAllWinStreaks().get().get("879310496002637824").getConnectFourWins());

        Assertions.assertTrue(cafeAPI.getWinStreaksEndpoint().updateUserWinStreak("879310496002637824", MinigameType.TIC_TAC_TOE, 25).get());
        Assertions.assertEquals(25, cafeAPI.getWinStreaksEndpoint().getUserWinStreak("879310496002637824").get().getTicTacToeWins());

        Assertions.assertTrue(cafeAPI.getWinStreaksEndpoint().deleteUserWinStreak("879310496002637824").get());

        cafeAPI.getWinStreaksEndpoint().getUserWinStreak("879310496002637824")
                .thenAcceptAsync((winStreak) -> Assertions.fail())
                .exceptionallyAsync((exception) -> {
                    Assertions.assertInstanceOf(NotFoundException.class, exception.getCause());
                    return null;
                }).join();
    }

}
