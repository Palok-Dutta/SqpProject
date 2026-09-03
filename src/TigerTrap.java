import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class TigerTrap extends JFrame{
    private JButton[] cells = new JButton[25];
    private int tigerPos = 0;              
    private final int GOAL = 24;           
    private boolean[] trapped = new boolean[25]; 
    private boolean placingTraps = true; 
    private ImageIcon resizedIcon;

    public TigerTrap() {
        setTitle("Trap the Tiger");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon tigerIcon = new ImageIcon("E:\\Photos\\red.png");
        Image scaledImage = tigerIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        resizedIcon = new ImageIcon(scaledImage); // field assignment
        
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

        add(boardPanel);
        setVisible(true);
    }

    private java.util.List<Integer> getAdjacent(int pos) {
        java.util.List<Integer> adj = new ArrayList<>();
        int row = pos / 5;
        int col = pos % 5;

        if (row > 0) adj.add(pos - 5); // up
        if (row < 4) adj.add(pos + 5); // down
        if (col > 0) adj.add(pos - 1); // left
        if (col < 4) adj.add(pos + 1); // right

        if (row > 0 && col > 0) adj.add(pos - 6);       // up-left
        if (row > 0 && col < 4) adj.add(pos - 4);       // up-right
        if (row < 4 && col > 0) adj.add(pos + 4);       // down-left
        if (row < 4 && col < 4) adj.add(pos + 6);

        return adj;
    }
    private void handleClick(int pos) {
        if (placingTraps) {
            placeTrap(pos);
        } else {
            moveTiger(pos);
        }
    }

    private void placeTrap(int pos) {
        if (pos == tigerPos || trapped[pos]) return; // invalid: tiger's cell or already trapped

        trapped[pos] = true;
        cells[pos].setText("X");
        cells[pos].setEnabled(false);

        if (!tigerHasValidMove()) {
            JOptionPane.showMessageDialog(this, "Trap-setter wins! Tiger is stuck.");
            disableAllButtons();
            return;
        }

        placingTraps = false; // now it's tiger's turn
    }

    private void moveTiger(int newPos) {
        if (!getAdjacent(tigerPos).contains(newPos)) return; // not adjacent
        if (trapped[newPos]) return; // can't move into a trap

        cells[tigerPos].setIcon(null); // clear old position
        tigerPos = newPos;
        cells[tigerPos].setIcon(resizedIcon); // move icon to new position

        if (tigerPos == GOAL) {
            JOptionPane.showMessageDialog(this, "Tiger wins! Reached the corner.");
            disableAllButtons();
            return;
        }

        placingTraps = true; // back to trap-setter's turn
    }

    private boolean tigerHasValidMove() {
        for (int adj : getAdjacent(tigerPos)) {
            if (!trapped[adj]) return true;
        }
        return false;
    }

    private void disableAllButtons() {
        for (JButton b : cells) {
            b.setEnabled(false);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TigerTrap());
    }
}
