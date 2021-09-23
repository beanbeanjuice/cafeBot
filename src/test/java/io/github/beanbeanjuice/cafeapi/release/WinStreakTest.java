package io.github.beanbeanjuice.cafeapi.release;

import io.github.beanbeanjuice.cafeapi.CafeAPI;
import io.github.beanbeanjuice.cafeapi.cafebot.minigames.winstreaks.MinigameType;
import io.github.beanbeanjuice.cafeapi.exception.ConflictException;
import io.github.beanbeanjuice.cafeapi.exception.NotFoundException;
import io.github.beanbeanjuice.cafeapi.requests.RequestLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WinStreakTest {

    @Test
    @DisplayName("Test WinStreaks API")
    public void winStreaksAPITest() {
        CafeAPI cafeAPI = new CafeAPI("beanbeanjuice", System.getenv("RELEASE_API_PASSWORD"), RequestLocation.RELEASE);

        Assertions.assertTrue(cafeAPI.winStreaks().deleteUserWinStreak("817975989547040795"));

        Assertions.assertThrows(NotFoundException.class, () -> {
            cafeAPI.winStreaks().updateUserWinStreak("817975989547040795", MinigameType.TIC_TAC_TOE, 100);
        });

        Assertions.assertThrows(NotFoundException.class, () -> {
            cafeAPI.winStreaks().getUserWinStreak("817975989547040795");
        });

        Assertions.assertTrue(cafeAPI.winStreaks().createUserWinStreak("817975989547040795"));

        Assertions.assertThrows(ConflictException.class, () -> {
            cafeAPI.winStreaks().createUserWinStreak("817975989547040795");
        });

        Assertions.assertEquals(0, cafeAPI.winStreaks().getUserWinStreak("817975989547040795").getTicTacToeWins());
        Assertions.assertEquals(0, cafeAPI.winStreaks().getUserWinStreak("817975989547040795").getConnectFourWins());

        Assertions.assertTrue(cafeAPI.winStreaks().updateUserWinStreak("817975989547040795", MinigameType.CONNECT_FOUR, 20));
        Assertions.assertEquals(20, cafeAPI.winStreaks().getAllWinStreaks().get("817975989547040795").getConnectFourWins());

        Assertions.assertTrue(cafeAPI.winStreaks().updateUserWinStreak("817975989547040795", MinigameType.TIC_TAC_TOE, 25));
        Assertions.assertEquals(25, cafeAPI.winStreaks().getUserWinStreak("817975989547040795").getTicTacToeWins());

        Assertions.assertTrue(cafeAPI.winStreaks().deleteUserWinStreak("817975989547040795"));

        Assertions.assertThrows(NotFoundException.class, () -> {
            cafeAPI.winStreaks().getUserWinStreak("817975989547040795");
        });
    }

}
