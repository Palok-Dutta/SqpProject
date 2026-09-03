import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    private MainFrame mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Sudoku Arena");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Username:"), gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1;
        add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 2;
        add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0; gbc.gridy = 3;
        add(loginButton, gbc);

        JButton registerButton = new JButton("Register");
        gbc.gridx = 1; gbc.gridy = 3;
        add(registerButton, gbc);

        messageLabel = new JLabel(" ");
        messageLabel.setForeground(Color.RED);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(messageLabel, gbc);

        loginButton.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Placeholder — replace with real DB check later
        if (username.equals("test") && password.equals("test")) {
            mainFrame.showScreen("MENU"); // go to menu screen after login
        } else {
            messageLabel.setText("Invalid username or password.");
        }
    }
}