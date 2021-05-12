import com.beanbeanjuice.utility.cafe.ServeHandler;
import com.beanbeanjuice.utility.cafe.ServeWord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TipTest {

    @Test
    @DisplayName("Tip Test")
    public void test1() {

        ServeHandler serveHandler = new ServeHandler();
        ServeWord word1 = new ServeWord("bruh", 1000);

        boolean answer = false;
        Double tip = serveHandler.calculateTip(word1);

        if (tip > 5 && tip < 30) {
            answer = true;
        }

        Assertions.assertTrue(answer);
    }

}
