import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    private final JButton statusButton = new JButton("Show status");
    private final JLabel scoreLabel = new JLabel("Total score: 0");

    public MenuPanel(MainFrame mainFrame) {
        setLayout(new BorderLayout());
        scoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 18));
        add(scoreLabel, BorderLayout.NORTH);

        JPanel menuContent = new JPanel(new GridBagLayout());

        JLabel title = new JLabel("Welcome!");
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JButton playButton = new JButton("Play Trap the Tiger");
        playButton.addActionListener(e -> mainFrame.showScreen("GAME"));
        statusButton.addActionListener(e -> showStatus());
        statusButton.setVisible(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridy = 0;
        menuContent.add(title, gbc);

        gbc.gridy = 1;
        menuContent.add(playButton, gbc);

        gbc.gridy = 2;
        menuContent.add(statusButton, gbc);
        add(menuContent, BorderLayout.CENTER);
    }

    public void setLoggedInUser(Database.LoginResult user) {
        statusButton.setVisible(user.isAdmin());
        refreshScore(user);
    }

    public void refreshScore(Database.LoginResult user) {
        if (user == null || user.isAdmin()) {
            scoreLabel.setText("Total score: 0");
            return;
        }

        try {
            scoreLabel.setText("Total score: " + Database.getTotalScore(user.getUserId()));
        } catch (java.sql.SQLException exception) {
            scoreLabel.setText("Total score: unavailable");
        }
    }

    private void showStatus() {
        try {
            java.util.List<Database.PlayerStatus> players = Database.getPlayerStatuses();
            String[] columns = {"Username", "Password", "Matches played", "Score"};
            Object[][] rows = new Object[players.size()][columns.length];
            for (int row = 0; row < players.size(); row++) {
                Database.PlayerStatus player = players.get(row);
                rows[row][0] = player.getUsername();
                rows[row][1] = player.getPassword();
                rows[row][2] = player.getMatchesPlayed();
                rows[row][3] = player.getScore();
            }

            javax.swing.JTable table = new javax.swing.JTable(rows, columns);
            table.setEnabled(false);
            javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(520, 250));
            JOptionPane.showMessageDialog(this, scrollPane, "Player status", JOptionPane.INFORMATION_MESSAGE);
        } catch (java.sql.SQLException exception) {
            JOptionPane.showMessageDialog(this,
                    "Could not load player status.\n" + exception.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}