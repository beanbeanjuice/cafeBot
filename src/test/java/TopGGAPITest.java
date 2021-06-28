import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.security.auth.login.LoginException;

public class TopGGAPITest {

    @Test
    @DisplayName("Top.GG API Test")
    public void test() throws LoginException, InterruptedException {
        new CafeBot();
        CafeBot.getTopGGAPI().getBot(CafeBot.getJDA().getSelfUser().getId()).whenComplete((bot, e) -> {
            System.out.println(bot.getPrefix());
        });

        CafeBot.getTopGGAPI().getStats("").whenCompleteAsync((stats, e) -> {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.DEBUG, "Bot Servers: " + stats.getServerCount());
        });
    }

}
