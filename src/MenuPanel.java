import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {

    public MenuPanel(MainFrame mainFrame) {
        setLayout(new GridBagLayout());

        JLabel title = new JLabel("Welcome! Choose difficulty:");
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JButton playButton = new JButton("Play Sudoku");
        playButton.addActionListener(e -> mainFrame.showScreen("GAME"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridy = 0;
        add(title, gbc);

        gbc.gridy = 1;
        add(playButton, gbc);
    }
}