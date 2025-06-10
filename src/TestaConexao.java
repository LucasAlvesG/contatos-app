import java.sql.Connection;
import java.sql.SQLException;

public class TestaConexao {
    public static void main(String[] args) {
        try {
            Connection conn = Conexao.getConnection();
            System.out.println("deu bom");
            conn.close();
        } catch (SQLException e) {
            System.out.println("não foi " + e.getMessage());
        }
    }
}