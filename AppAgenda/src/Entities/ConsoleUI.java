package Entities;
import DataConnection.ContatoDAO;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private ContatoDAO contatoDAO;
    private Scanner scanner;

    public ConsoleUI() {
        contatoDAO = new ContatoDAO();
        scanner = new Scanner(System.in);
    }

    public void iniciar() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- Agenda de Contatos (Console) ---");
            System.out.println("1. Listar contatos");
            System.out.println("2. Adicionar contato");
            System.out.println("3. Atualizar contato");
            System.out.println("4. Excluir contato");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opcao) {
                case 1:
                    listarContatos();
                    break;
                case 2:
                    adicionarContato();
                    break;
                case 3:
                    atualizarContato();
                    break;
                case 4:
                    excluirContato();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void listarContatos() {
        List<Contato> contatos = contatoDAO.listarContatos();
        System.out.println("\n--- Lista de Contatos ---");
        for (Contato contato : contatos) {
            System.out.println("ID: " + contato.getId());
            System.out.println("Nome: " + contato.getNome());
            System.out.println("Email: " + contato.getEmail());
            System.out.println("Telefone: (" + contato.getTelefone());
            System.out.println("-------------------------");
        }
    }

    private void adicionarContato() {
        System.out.print("Digite o nome: ");
        String nome = scanner.nextLine();
        System.out.print("Digite o email: ");
        String email = scanner.nextLine();
        System.out.print("Digite o numero de telefone: ");
        String telefone = scanner.nextLine();

        Contato contato = new Contato(nome, email, telefone);
        contatoDAO.inserirContato(contato);
        System.out.println("Contato adicionado com sucesso!");
    }

    private void atualizarContato() {
        System.out.print("Digite o ID do contato que deseja atualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir a nova linha
        System.out.print("Digite o novo nome: ");
        String nome = scanner.nextLine();
        System.out.print("Digite o novo email: ");
        String email = scanner.nextLine();
        System.out.print("Digite o numero de telefone: ");
        String telefone = scanner.nextLine();

        Contato contato = new Contato(nome, email, telefone);
        contatoDAO.atualizarContato(id, contato);
        System.out.println("Contato atualizado com sucesso!");
    }

    private void excluirContato() {
        System.out.print("Digite o ID do contato que deseja excluir: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir a nova linha

        contatoDAO.excluirContato(id);
        System.out.println("Contato excluído com sucesso!");
    }
}

