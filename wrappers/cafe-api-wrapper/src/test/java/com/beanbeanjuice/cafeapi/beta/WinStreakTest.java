package com.beanbeanjuice.cafeapi.beta;

import com.beanbeanjuice.cafeapi.CafeAPI;
import com.beanbeanjuice.cafeapi.cafebot.minigames.winstreaks.MinigameType;
import com.beanbeanjuice.cafeapi.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.exception.api.NotFoundException;
import com.beanbeanjuice.cafeapi.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WinStreakTest {

    @Test
    @DisplayName("Test WinStreaks API")
    public void winStreaksAPITest() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"), RequestLocation.BETA);

        Assertions.assertTrue(cafeAPI.WIN_STREAK.deleteUserWinStreak("879310496002637824"));

        Assertions.assertThrows(NotFoundException.class, () -> {
            cafeAPI.WIN_STREAK.updateUserWinStreak("879310496002637824", MinigameType.TIC_TAC_TOE, 100);
        });

        Assertions.assertThrows(NotFoundException.class, () -> {
            cafeAPI.WIN_STREAK.getUserWinStreak("879310496002637824");
        });

        Assertions.assertTrue(cafeAPI.WIN_STREAK.createUserWinStreak("879310496002637824"));

        Assertions.assertThrows(ConflictException.class, () -> {
            cafeAPI.WIN_STREAK.createUserWinStreak("879310496002637824");
        });

        Assertions.assertEquals(0, cafeAPI.WIN_STREAK.getUserWinStreak("879310496002637824").getTicTacToeWins());
        Assertions.assertEquals(0, cafeAPI.WIN_STREAK.getUserWinStreak("879310496002637824").getConnectFourWins());

        Assertions.assertTrue(cafeAPI.WIN_STREAK.updateUserWinStreak("879310496002637824", MinigameType.CONNECT_FOUR, 20));
        Assertions.assertEquals(20, cafeAPI.WIN_STREAK.getAllWinStreaks().get("879310496002637824").getConnectFourWins());

        Assertions.assertTrue(cafeAPI.WIN_STREAK.updateUserWinStreak("879310496002637824", MinigameType.TIC_TAC_TOE, 25));
        Assertions.assertEquals(25, cafeAPI.WIN_STREAK.getUserWinStreak("879310496002637824").getTicTacToeWins());

        Assertions.assertTrue(cafeAPI.WIN_STREAK.deleteUserWinStreak("879310496002637824"));

        Assertions.assertThrows(NotFoundException.class, () -> {
            cafeAPI.WIN_STREAK.getUserWinStreak("879310496002637824");
        });
    }

}
