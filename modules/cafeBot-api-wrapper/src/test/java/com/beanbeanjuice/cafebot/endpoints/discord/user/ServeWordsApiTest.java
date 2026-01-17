package com.beanbeanjuice.cafebot.endpoints.discord.user;

import com.beanbeanjuice.cafebot.ApiTest;
import com.beanbeanjuice.cafebot.api.wrapper.type.ServeWord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

public class ServeWordsApiTest extends ApiTest {

    @Test
    @DisplayName("can serve word")
    public void canServeWord() throws ExecutionException, InterruptedException {
        ServeWord word = cafeAPI.getServeWordsApi().serveWord(generateSnowflake().toString(), "hello").get();

        Assertions.assertTrue(word.getUses() > 0);
        Assertions.assertTrue(word.getReward() > 0);
        Assertions.assertTrue(word.getNewBalance() > 0);
    }

}
