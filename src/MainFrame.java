import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private TigerTrapPanel tigerTrapPanel;
    private MenuPanel menuPanel;
    private Database.LoginResult loggedInUser;

    public MainFrame() {
        setTitle("Trap the Tiger");
        setSize(650, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add each screen as a "card"
        mainPanel.add(new LoginPanel(this), "LOGIN");
        menuPanel = new MenuPanel(this);
        mainPanel.add(menuPanel, "MENU");
        tigerTrapPanel = new TigerTrapPanel(this::finishGame);
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

    public void login(Database.LoginResult user) {
        loggedInUser = user;
        menuPanel.setLoggedInUser(user);
        showScreen("MENU");
    }

    private void finishGame() {
        if (loggedInUser != null && !loggedInUser.isAdmin()) {
            try {
                Database.recordMatch(loggedInUser.getUserId(), 1);
            } catch (java.sql.SQLException exception) {
                JOptionPane.showMessageDialog(this,
                        "The game ended, but its statistics could not be saved.\n"
                                + exception.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        returnToMenu();
    }

    private void returnToMenu() {
        showScreen("MENU");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}