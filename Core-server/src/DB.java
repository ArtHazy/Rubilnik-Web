import java.sql.*;

class DB {
    public static boolean isConnected = false;
    static Connection connection;
    static Statement statement;

    public static void connect() throws SQLException{
        connection = DriverManager.getConnection(_Env.DB_URL);
        statement = connection.createStatement();
    }
    public static void disconnect() throws SQLException{
        connection.close();
    }
}
