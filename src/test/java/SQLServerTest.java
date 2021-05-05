import com.beanbeanjuice.utility.sql.SQLServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class SQLServerTest {

    private static SQLServer sqlServer;
    private static final String SQL_URL = "beanbeanjuice.com";
    private static final String SQL_PORT = "4000";
    private static final String SQL_USERNAME = "root";
    private static final String SQL_PASSWORD = "gHDf]Tf~8T^VuZisn%6ktgukr*ci~!";
    private static final boolean SQL_ENCRYPT = true;

    @Test
    @DisplayName("Testing the SQL Server Class")
    public void test1() throws SQLException {

        sqlServer = new SQLServer(SQL_URL, SQL_PORT, SQL_ENCRYPT, SQL_USERNAME, SQL_PASSWORD);
        Assertions.assertTrue(sqlServer.testConnection());
        Assertions.assertTrue(sqlServer.startConnection());

        sqlServer.getConnection().close();

        sqlServer = new SQLServer(SQL_URL, SQL_PORT, SQL_ENCRYPT, SQL_USERNAME, SQL_PASSWORD);
        Assertions.assertTrue(sqlServer.testConnection());
        Assertions.assertTrue(sqlServer.startConnection());

        Assertions.assertTrue(sqlServer.checkConnection());
        sqlServer.getConnection().close();
        Assertions.assertFalse(sqlServer.checkConnection());

        sqlServer.startConnection();
        Assertions.assertTrue(sqlServer.checkConnection());

    }

}
