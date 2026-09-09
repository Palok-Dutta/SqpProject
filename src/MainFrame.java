import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private TigerTrapPanel tigerTrapPanel;

    public MainFrame() {
        setTitle("Trap the Tiger");
        setSize(650, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add each screen as a "card"
        mainPanel.add(new LoginPanel(this), "LOGIN");
        mainPanel.add(new MenuPanel(this), "MENU");
        tigerTrapPanel = new TigerTrapPanel(this::returnToMenu);
        mainPanel.add(tigerTrapPanel, "GAME");

        add(mainPanel);
        setVisible(true);

        showScreen("LOGIN"); // start on login screen
    }

    public void showScreen(String name) {
        if (name.equals("GAME")) {
            tigerTrapPanel.resetGame();
        }
        cardLayout.show(mainPanel, name);
    }

    private void returnToMenu() {
        showScreen("MENU");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}