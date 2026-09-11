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

        JLabel title = new JLabel("Trap Arena");
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
        registerButton.addActionListener(e -> handleRegister());
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Enter a username and password to register.");
            return;
        }

        try {
            if (Database.registerUser(username, password)) {
                messageLabel.setForeground(new Color(0, 128, 0));
                messageLabel.setText("Registration successful. You can now log in.");
                passwordField.setText("");
            } else {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("That username or password is already in use.");
            }
        } catch (java.sql.SQLException exception) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Database connection failed.");
            JOptionPane.showMessageDialog(this,
                    exception.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Enter both username and password.");
            return;
        }

        try {
            Database.LoginResult result = Database.authenticate(username, password);
            if (result != null) {
                mainFrame.login(result);
                messageLabel.setText(" ");
            } else {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Invalid username or password.");
            }
        } catch (java.sql.SQLException exception) {
            messageLabel.setText("Database connection failed.");
            JOptionPane.showMessageDialog(this,
                    exception.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}