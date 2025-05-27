import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final String URL = "jdbc:mysql://localhost:3306/agenda_db";
    private static final String USER = "root"; // altere se necessário
    private static final String PASSWORD = "root"; // coloque sua senha real aqui

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
