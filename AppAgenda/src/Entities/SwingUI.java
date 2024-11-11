package Entities;
import DataConnection.ContatoDAO;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.JOptionPane;


public class SwingUI extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private ContatoDAO contatoDAO;

    public SwingUI() {
        contatoDAO = new ContatoDAO();
        setTitle("Agenda de Contatos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Configuração do JTable e do modelo de tabela
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Email", "Telefone"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Painel para os botões CRUD
        JPanel panel = new JPanel();
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnListar = new JButton("Listar Contatos");

        panel.add(btnAdicionar);
        panel.add(btnAtualizar);
        panel.add(btnExcluir);
        panel.add(btnListar);

        add(panel, BorderLayout.SOUTH);

        // Ações dos botões
        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarContato();
            }
        });

        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarContato();
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluirContato();
            }
        });

        btnListar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarContatos();
            }
        });

        // Carregar contatos no início
        listarContatos();
    }

    private void adicionarContato() {
        String nome = JOptionPane.showInputDialog("Digite o nome:");
        String email = JOptionPane.showInputDialog("Digite o email:");
        String telefone = JOptionPane.showInputDialog("Digite o telefone:");

        // Validar se os campos não estão vazios
        if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios.");
            return;
        }

        // Criar um novo objeto Contato sem ID (o ID será gerado pelo banco)
        Contato contato = new Contato(nome, email, telefone);

        // Inserir o contato no banco e obter o ID gerado
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_CONTATO, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, contato.getNome());
            stmt.setString(2, contato.getEmail());
            stmt.setString(3, contato.getTelefone());
            stmt.executeUpdate();

            // Recuperar o ID gerado
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    contato.setId(generatedId); // Atualizar o objeto com o ID gerado
                }
            }

            // Atualizar a tabela
            listarContatos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar contato: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void atualizarContato() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String nome = JOptionPane.showInputDialog("Atualize o nome:", tableModel.getValueAt(selectedRow, 1));
            String email = JOptionPane.showInputDialog("Atualize o email:", tableModel.getValueAt(selectedRow, 2));
            String telefone = JOptionPane.showInputDialog("Atualize o telefone:", tableModel.getValueAt(selectedRow, 3));

            Contato contato = new Contato(nome, email, telefone);
            contatoDAO.atualizarContato(id, contato);
            listarContatos();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um contato para atualizar.");
        }
    }

    private void excluirContato() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            contatoDAO.excluirContato(id);
            listarContatos();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um contato para excluir.");
        }
    }

    private void listarContatos() {
        tableModel.setRowCount(0); // Limpar a tabela
        List<Contato> contatos = contatoDAO.listarContatos();
        for (Contato contato : contatos) {
            Object[] row = {
                    contato.getId(),
                    contato.getNome(),
                    contato.getEmail(),
                    contato.getTelefone()
            };
            tableModel.addRow(row);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SwingUI frame = new SwingUI();
            frame.setVisible(true);
        });
    }
}
