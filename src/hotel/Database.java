package hotel;

import java.sql.*;

public class Database {

    private final String url;
    private Connection sqlConnection;

    Database(String databaseName, String databaseUser, String databasePassword) throws SQLException {
        this.url = "jdbc:mysql://localhost:3306/" + databaseName + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        this.sqlConnection = DriverManager.getConnection(url, databaseUser, databasePassword);
    }

    public void close() throws SQLException {
        sqlConnection.close();
    }

}