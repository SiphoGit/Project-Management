import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC {
    /**
     * This method Connects to the poisedpms_db database with username and password and returns Statement object.
     * @return
     * @throws SQLException
     */
    public Statement connection() throws SQLException {
        // Connect to the poisedpms_db database with username and password and returns Statement object.
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/poisedpms_db?allowPublicKeyRetrieval=true&useSSL=false",
                "otheruser",
                "swordfish"
        );

        // Create a direct line to the database for running the queries
        Statement statement = connection.createStatement();
        return statement;
    }
}
