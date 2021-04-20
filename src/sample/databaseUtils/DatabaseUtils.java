package sample.databaseUtils;

import java.sql.*;

public class DatabaseUtils {
    public static Connection connectToDb() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String DB_URL = "jdbc:mysql://localhost/AccountingSystem";
        String USER = "root";
        String PASS = "";
        Connection connection = DriverManager.getConnection(DB_URL, USER,PASS);
        return connection;
    }

    public static void disconnectFromDb(Connection connection, Statement statement) throws SQLException {
        if(connection != null && statement != null)
        {
            statement.close();
            connection.close();
        }
    }
}
