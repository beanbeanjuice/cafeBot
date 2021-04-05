import com.beanbeanjuice.utility.sql.SQLServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SQLServerTest {

    private static SQLServer sqlServer;
    private static final String SQL_URL = "beanbeanjuice.com";
    private static final String SQL_PORT = "4001";
    private static final String SQL_USERNAME = "root";
    private static final String SQL_PASSWORD = "Hawaii&Florida12345";
    private static final boolean SQL_ENCRYPT = true;

    @Test
    @DisplayName("Testing the SQL Server Class")
    public void test1() {

        sqlServer = new SQLServer(SQL_URL, SQL_PORT, SQL_ENCRYPT, SQL_USERNAME, SQL_PASSWORD);

        Assertions.assertTrue(sqlServer.startConnection());
        Assertions.assertTrue(sqlServer.testConnection());

    }

}
