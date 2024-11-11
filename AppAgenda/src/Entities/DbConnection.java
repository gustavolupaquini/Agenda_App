package Entities;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/agenda_contatos";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    // Lançando SQLException em vez de tratá-la
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
