package io.github.beanbeanjuice.cafeapi;

import io.github.beanbeanjuice.cafeapi.cafebot.minigames.winstreaks.MinigameType;
import io.github.beanbeanjuice.cafeapi.exception.ConflictException;
import io.github.beanbeanjuice.cafeapi.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WinStreakTest {

    @Test
    @DisplayName("Test WinStreaks API")
    public void winStreaksAPITest() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("API_PASSWORD"));

        Assertions.assertTrue(cafeAPI.winStreaks().deleteUserWinStreak("879310496002637824"));

        Assertions.assertThrows(NotFoundException.class, () -> {
            cafeAPI.winStreaks().updateUserWinStreak("879310496002637824", MinigameType.TIC_TAC_TOE, 100);
        });

        Assertions.assertThrows(NotFoundException.class, () -> {
            cafeAPI.winStreaks().getUserWinStreak("879310496002637824");
        });

        Assertions.assertTrue(cafeAPI.winStreaks().createUserWinStreak("879310496002637824"));

        Assertions.assertThrows(ConflictException.class, () -> {
            cafeAPI.winStreaks().createUserWinStreak("879310496002637824");
        });

        Assertions.assertEquals(0, cafeAPI.winStreaks().getUserWinStreak("879310496002637824").getTicTacToeWins());
        Assertions.assertEquals(0, cafeAPI.winStreaks().getUserWinStreak("879310496002637824").getConnectFourWins());

        Assertions.assertTrue(cafeAPI.winStreaks().updateUserWinStreak("879310496002637824", MinigameType.CONNECT_FOUR, 20));
        Assertions.assertEquals(20, cafeAPI.winStreaks().getAllWinStreaks().get("879310496002637824").getConnectFourWins());

        Assertions.assertTrue(cafeAPI.winStreaks().updateUserWinStreak("879310496002637824", MinigameType.TIC_TAC_TOE, 25));
        Assertions.assertEquals(25, cafeAPI.winStreaks().getUserWinStreak("879310496002637824").getTicTacToeWins());

        Assertions.assertTrue(cafeAPI.winStreaks().deleteUserWinStreak("879310496002637824"));

        Assertions.assertThrows(NotFoundException.class, () -> {
            cafeAPI.winStreaks().getUserWinStreak("879310496002637824");
        });
    }

}
