package DataConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Entities.DbConnection;
import Entities.Contato;

public class ContatoDAO {
    private static final String INSERT_CONTATO = "INSERT INTO contato (nome, email, telefone) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_CONTATOS = "SELECT * FROM contato";
    private static final String UPDATE_CONTATO = "UPDATE contato SET nome = ?, email = ?, telefone = ? WHERE id = ?";
    private static final String DELETE_CONTATO = "DELETE FROM contato WHERE id = ?";

    // Método para inserir um novo contato
    public void inserirContato(Contato contato) {
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_CONTATO, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, contato.getNome());
            stmt.setString(2, contato.getEmail());
            stmt.setString(3, contato.getTelefone());
            stmt.executeUpdate();

            // Recuperando o ID gerado automaticamente
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);  // Recupera o ID
                    contato.setId(generatedId);  // Define o ID no objeto Contato
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para listar todos os contatos
    public List<Contato> listarContatos() {
        List<Contato> contatos = new ArrayList<>();
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_CONTATOS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");  // Recuperando o id
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String telefone = rs.getString("telefone");
                Contato contato = new Contato(nome, email, telefone);
                contato.setId(id);  // Definindo o id do contato
                contatos.add(contato);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contatos;
    }

    // Método para atualizar um contato
    public void atualizarContato(int id, Contato contato) {
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_CONTATO)) {
            stmt.setString(1, contato.getNome());
            stmt.setString(2, contato.getEmail());
            stmt.setString(3, contato.getTelefone());
            stmt.setInt(4, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para excluir um contato
    public void excluirContato(int id) {
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_CONTATO)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
