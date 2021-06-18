import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.security.auth.login.LoginException;

public class SQLServerTest {

    @Test
    @DisplayName("SQL Server Test")
    public void test() throws LoginException, InterruptedException {
        new CafeBot();
        CafeBot.getLogManager().log(this.getClass(), LogLevel.INFO, "Testing the Beta SQL Database Connection...");
        Assertions.assertTrue(CafeBot.getSQLServer().testConnection());
        CafeBot.getLogManager().log(this.getClass(), LogLevel.OKAY, "Database is online!");
    }

}
