package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.minigames.winstreaks.MinigameType;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.NotFoundException;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WinStreakTest {

    @Test
    @DisplayName("Win Streaks Endpoint Test")
    public void testWinStreakEndpoint() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        Assertions.assertTrue(cafeAPI.getWinStreaksEndpoint().deleteUserWinStreak("879310496002637824"));

        Assertions.assertThrows(NotFoundException.class, () -> {
            cafeAPI.getWinStreaksEndpoint().updateUserWinStreak("879310496002637824", MinigameType.TIC_TAC_TOE, 100);
        });

        Assertions.assertThrows(NotFoundException.class, () -> {
            cafeAPI.getWinStreaksEndpoint().getUserWinStreak("879310496002637824");
        });

        Assertions.assertTrue(cafeAPI.getWinStreaksEndpoint().createUserWinStreak("879310496002637824"));

        Assertions.assertThrows(ConflictException.class, () -> {
            cafeAPI.getWinStreaksEndpoint().createUserWinStreak("879310496002637824");
        });

        Assertions.assertEquals(0, cafeAPI.getWinStreaksEndpoint().getUserWinStreak("879310496002637824").getTicTacToeWins());
        Assertions.assertEquals(0, cafeAPI.getWinStreaksEndpoint().getUserWinStreak("879310496002637824").getConnectFourWins());

        Assertions.assertTrue(cafeAPI.getWinStreaksEndpoint().updateUserWinStreak("879310496002637824", MinigameType.CONNECT_FOUR, 20));
        Assertions.assertEquals(20, cafeAPI.getWinStreaksEndpoint().getAllWinStreaks().get("879310496002637824").getConnectFourWins());

        Assertions.assertTrue(cafeAPI.getWinStreaksEndpoint().updateUserWinStreak("879310496002637824", MinigameType.TIC_TAC_TOE, 25));
        Assertions.assertEquals(25, cafeAPI.getWinStreaksEndpoint().getUserWinStreak("879310496002637824").getTicTacToeWins());

        Assertions.assertTrue(cafeAPI.getWinStreaksEndpoint().deleteUserWinStreak("879310496002637824"));

        Assertions.assertThrows(NotFoundException.class, () -> {
            cafeAPI.getWinStreaksEndpoint().getUserWinStreak("879310496002637824");
        });
    }

}
