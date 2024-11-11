import java.util.Scanner;
import Entities.SwingUI;
import Entities.ConsoleUI;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Escolha a interface:");
        System.out.println("1. Console");
        System.out.println("2. Swing");
        int escolha = scanner.nextInt();

        if (escolha == 1) {
            // Chama o CRUD via console
            ConsoleUI consoleUI = new ConsoleUI();
            consoleUI.iniciar();
        } else if (escolha == 2) {
            // Inicia a interface Swing
            SwingUtilities.invokeLater(() -> {
                SwingUI swingUI = new SwingUI();
                swingUI.setVisible(true);
            });
        } else {
            System.out.println("Opção inválida. Saindo do programa.");
        }
    }
}