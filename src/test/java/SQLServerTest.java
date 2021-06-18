import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.sql.SQLServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SQLServerTest {

    private static final String SQL_URL = System.getenv("CAFEBOT_MYSQL_URL");
    private static final String SQL_PORT = System.getenv("CAFEBOT_MYSQL_PORT");
    private static final String SQL_USERNAME = System.getenv("CAFEBOT_MYSQL_USERNAME");
    private static final String SQL_PASSWORD = System.getenv("CAFEBOT_MYSQL_PASSWORD");
    private static final boolean SQL_ENCRYPT = Boolean.parseBoolean(System.getenv("CAFEBOT_MYSQL_ENCRYPT"));

    @Test
    @DisplayName("SQL Server Test")
    public void test() {
        SQLServer sqlServer = new SQLServer(SQL_URL, SQL_PORT, SQL_ENCRYPT, SQL_USERNAME, SQL_PASSWORD);
        Assertions.assertNotNull(SQL_URL);
        Assertions.assertNotNull(SQL_PORT);
        Assertions.assertTrue(SQL_ENCRYPT);
        Assertions.assertNotNull(SQL_USERNAME);
        Assertions.assertNotNull(SQL_PASSWORD);
        Assertions.assertTrue(sqlServer.startConnection());
        Assertions.assertTrue(sqlServer.testConnection());
    }

}
