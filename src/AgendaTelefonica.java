import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AgendaTelefonica {

    // Não é mais necessário a lista em memória para persistência primária
    // private List<Contato> contatos;

    public AgendaTelefonica() {
        // O construtor pode ficar vazio ou realizar alguma configuração inicial se necessário
    }

    /**
     * Adiciona um novo contato ao banco de dados. [cite: 5, 19]
     * @param contato O objeto Contato a ser adicionado.
     */
    public void adicionarContato(Contato contato) {
        if (contato == null || contato.getNome() == null || contato.getNome().trim().isEmpty()) {
            System.out.println("Erro: O nome do contato não pode ser vazio.");
            return;
        }
        // Verifica se o contato já existe (opcional, mas bom para evitar duplicatas se 'nome' for uma chave lógica)
        if (buscarContatoPeloNome(contato.getNome()) != null) {
            System.out.println("Erro: Contato com o nome '" + contato.getNome() + "' já existe.");
            return;
        }

        String sql = "INSERT INTO contatos (nome, email, telefone) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, contato.getNome());
            pstmt.setString(2, contato.getEmail());
            pstmt.setString(3, contato.getTelefone());
            pstmt.executeUpdate();
            System.out.println("Contato '" + contato.getNome() + "' adicionado ao banco de dados.");

        } catch (SQLException e) {
            System.out.println("Erro ao adicionar contato no banco de dados: " + e.getMessage());
            // e.printStackTrace(); // Descomente para ver o rastreamento completo do erro durante o desenvolvimento
        }
    }

    /**
     * Remove um contato do banco de dados pelo nome. [cite: 20]
     * @param nome O nome do contato a ser removido.
     */
    public void removerContato(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            System.out.println("Nome para remoção inválido.");
            return;
        }

        String sql = "DELETE FROM contatos WHERE nome = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Contato '" + nome + "' removido do banco de dados.");
            } else {
                System.out.println("Contato '" + nome + "' não encontrado no banco de dados para remoção.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao remover contato do banco de dados: " + e.getMessage());
            // e.printStackTrace();
        }
    }

    /**
     * Busca e retorna um contato do banco de dados pelo nome. [cite: 20]
     * Este método é usado internamente e também pode ser exposto se necessário.
     * @param nomeBusca O nome do contato a ser buscado.
     * @return O objeto Contato se encontrado, caso contrário null.
     */
    private Contato buscarContatoPeloNome(String nomeBusca) { // Método auxiliar, pode ser público se desejado
        if (nomeBusca == null || nomeBusca.trim().isEmpty()) {
            return null; // Não imprime erro, pois pode ser usado internamente para verificações
        }

        String sql = "SELECT nome, email, telefone FROM contatos WHERE nome = ?";
        Contato contatoEncontrado = null;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nomeBusca);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    String email = rs.getString("email");
                    String telefone = rs.getString("telefone");
                    contatoEncontrado = new Contato(nome, email, telefone);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar contato '" + nomeBusca + "' no banco de dados: " + e.getMessage());
            // e.printStackTrace();
        }
        return contatoEncontrado;
    }

    /**
     * Busca e exibe um contato do banco de dados pelo nome. [cite: 20]
     * @param nomeBusca O nome do contato a ser buscado.
     * @return O objeto Contato se encontrado, caso contrário null.
     */
    public Contato buscarContato(String nomeBusca) {
        if (nomeBusca == null || nomeBusca.trim().isEmpty()) {
            System.out.println("Nome para busca inválido.");
            return null;
        }
        Contato contato = buscarContatoPeloNome(nomeBusca);
        if (contato != null) {
            System.out.println("Contato encontrado no banco:\n" + contato);
        } else {
            System.out.println("Contato '" + nomeBusca + "' não encontrado no banco de dados.");
        }
        return contato;
    }


    /**
     * Lista todos os contatos armazenados no banco de dados. [cite: 21]
     * @return Uma lista de objetos Contato.
     */
    public List<Contato> listarContatos() {
        List<Contato> contatosDoBanco = new ArrayList<>();
        String sql = "SELECT nome, email, telefone FROM contatos ORDER BY nome"; // Ordenar por nome é uma boa prática

        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (!rs.isBeforeFirst()) {
                System.out.println("A agenda no banco de dados está vazia.");
            } else {
                System.out.println("Lista de contatos do banco:");
                while (rs.next()) {
                    String nome = rs.getString("nome");
                    String email = rs.getString("email");
                    String telefone = rs.getString("telefone");
                    Contato contato = new Contato(nome, email, telefone);
                    contatosDoBanco.add(contato);
                    System.out.println(contato); // Imprime o contato formatado pelo toString() da classe Contato
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar contatos do banco de dados: " + e.getMessage());
            // e.printStackTrace();
        }
        return contatosDoBanco;
    }

    /**
     * Atualiza um contato existente no banco de dados.
     * O contato a ser atualizado é identificado pelo nomeAntigo.
     * @param nomeAntigo O nome atual do contato que será modificado.
     * @param contatoAtualizado O objeto Contato com as novas informações.
     */
    public void atualizarContato(String nomeAntigo, Contato contatoAtualizado) {
        if (nomeAntigo == null || nomeAntigo.trim().isEmpty() ||
            contatoAtualizado == null || contatoAtualizado.getNome() == null || contatoAtualizado.getNome().trim().isEmpty()) {
            System.out.println("Dados inválidos para atualização. Nome antigo e novo nome do contato não podem ser vazios.");
            return;
        }

        // Verifica se o contato antigo existe
        if (buscarContatoPeloNome(nomeAntigo) == null) {
            System.out.println("Contato '" + nomeAntigo + "' não encontrado para atualização.");
            return;
        }

        // Se o nome está sendo alterado, verifica se o novo nome já existe (e não é o próprio contato antigo)
        if (!nomeAntigo.equals(contatoAtualizado.getNome()) && buscarContatoPeloNome(contatoAtualizado.getNome()) != null) {
            System.out.println("Erro: Já existe um contato com o novo nome '" + contatoAtualizado.getNome() + "'.");
            return;
        }

        String sql = "UPDATE contatos SET nome = ?, email = ?, telefone = ? WHERE nome = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, contatoAtualizado.getNome());
            pstmt.setString(2, contatoAtualizado.getEmail());
            pstmt.setString(3, contatoAtualizado.getTelefone());
            pstmt.setString(4, nomeAntigo); // Cláusula WHERE para o nome antigo

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Contato '" + nomeAntigo + "' atualizado para '" + contatoAtualizado.getNome() + "' no banco de dados.");
            } else {
                // Esta mensagem pode não ser alcançada devido à verificação de existência acima,
                // mas é uma salvaguarda.
                System.out.println("Contato '" + nomeAntigo + "' não pôde ser atualizado (ou não foi encontrado).");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar contato no banco de dados: " + e.getMessage());
            // e.printStackTrace();
        }
    }
    public void limparTodosContatos() { // Nome do método atualizado para clareza
        String sql = "TRUNCATE TABLE contatos"; // Comando SQL alterado

        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement()) {

            // TRUNCATE TABLE não retorna o número de linhas afetadas da mesma forma que DELETE.
            // Em alguns drivers/bancos, executeUpdate() pode retornar 0 para TRUNCATE.
            // O importante é que a operação seja executada.
            stmt.executeUpdate(sql);
            System.out.println("Tabela 'contatos' truncada. Todos os contatos foram removidos e o contador de ID foi resetado.");

        } catch (SQLException e) {
            System.out.println("Erro ao tentar truncar a tabela 'contatos': " + e.getMessage());
            // e.printStackTrace(); // Descomente para debug, se necessário
        }
    }

}