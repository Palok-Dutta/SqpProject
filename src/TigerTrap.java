import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;


public class TigerTrap extends JFrame{
    private JButton[] cells = new JButton[25];
    private int tigerPos = 0;              
    private final int GOAL = 24;           
    private boolean[] trapped = new boolean[25]; 
    private boolean placingTraps = true; 
    private ImageIcon resizedIcon;

public class TigerTrap extends JFrame {
    public TigerTrap() {
        setTitle("Trap the Tiger");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        resizedIcon = loadTigerIcon();
        
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(5, 5));

        for (int i = 0; i < 25; i++) {
            JButton cell = new JButton();
            cells[i] = cell;
            boardPanel.add(cell);
        }
        cells[0].setIcon(resizedIcon);

        for (int i = 0; i < 25; i++) {
            final int index = i;
            cells[i].addActionListener(e -> handleClick(index));
        }

        // Use the constructed board panel as the content pane so pack() sizes correctly
        setContentPane(boardPanel);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TigerTrap().setVisible(true));
    }
}
