import com.beanbeanjuice.main.BeanBot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BotVersionTest {

    @Test
    @DisplayName("Bot Version Test")
    public void test() {
        Assertions.assertEquals(BeanBot.getBotVersion(), "v1.2.0", "Bot Version Incorrect!");
    }

}
