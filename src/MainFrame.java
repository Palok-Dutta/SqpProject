import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MainFrame() {
        setTitle("Sudoku Arena");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add each screen as a "card"
        mainPanel.add(new LoginPanel(this), "LOGIN");
        mainPanel.add(new MenuPanel(this), "MENU");
        mainPanel.add(new GamePanel(this), "GAME");

        add(mainPanel);
        setVisible(true);

        showScreen("LOGIN"); // start on login screen
    }

    public void showScreen(String name) {
        cardLayout.show(mainPanel, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}