package com.beanbeanjuice.cafebot;

import com.beanbeanjuice.cafeapi.cafebot.words.Word;
import com.beanbeanjuice.utility.sections.cafe.ServeHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TipTest {

    @Test
    @DisplayName("Tip Test")
    public void tipTest() {
        ServeHandler serveHandler = new ServeHandler();

        boolean answer = false;
        Double tip = serveHandler.calculateTip(new Word("bruh", 1000));

        if (tip > 5 && tip < 30) {
            answer = true;
        }

        Assertions.assertTrue(answer);
    }

}
