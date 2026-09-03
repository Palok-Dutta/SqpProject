import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

interface Operations
{
    void SetNumbers(JLabel label);
    boolean IfAvailable(int n, int pos);
    boolean FillGrid(int pos);
    void RandomizeGrid();
    void SaveSolvedGrid();
    void AddCellListeners(JLabel label);
    void StartTimer(JLabel label1, JLabel label2);
}

class GamePanel extends JPanel implements Operations
{
    private int[] cellsCopy = new int[81];
    private JTextField[] cells = new JTextField[81];
    java.util.List<Integer> numbers = new ArrayList<>();
    private int secondsElapsed = 0;
    private int score = 1000;      // starting score, deducted over time/hints
    private int hintsUsed = 0;
    private final int MAX_HINTS = 81;

    private javax.swing.Timer swingTimer;

    public GamePanel(MainFrame mainFrame) {
        for (int i = 1; i <= 9; i++) {
            numbers.add(i);
        }
        setLayout(new BorderLayout()); // frame-level layout

        // ---- TOP PANEL (timer, hint, score) ----
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        JLabel timerLabel = new JLabel("Time: 0s");
        JLabel scoreLabel = new JLabel("Score: 1000");
        JButton hintButton = new JButton("Hint(3 left)");
        hintButton.isFocusable();
        hintButton.addActionListener(e -> useHint(hintButton, scoreLabel));

        topPanel.add(timerLabel);
        topPanel.add(scoreLabel);
        topPanel.add(hintButton);

        add(topPanel, BorderLayout.NORTH);

        // ---- BOARD PANEL (your existing grid) ----
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(9, 9));

        for (int i = 0; i < 81; i++) {
            JTextField cell = new JTextField();
            cell.setHorizontalAlignment(JTextField.CENTER);
            cell.setFont(new Font("Arial", Font.BOLD, 20));
            cells[i] = cell;
            boardPanel.add(cell);
        }

        add(boardPanel, BorderLayout.CENTER);

        SetNumbers(scoreLabel);
        StartTimer(timerLabel, scoreLabel);
        setVisible(true);
    }

    public void StartTimer(JLabel timerLabel, JLabel scoreLabel) {
        swingTimer = new javax.swing.Timer(1000, e -> {
            secondsElapsed++;
            timerLabel.setText("Time: " + secondsElapsed + "s");

            // optional: deduct score slowly over time
            if (secondsElapsed % 10 == 0 && score > 0) {
                score -= 2;
                scoreLabel.setText("Score: " + score);
            }
        });
        swingTimer.start();
    }

    public void SetNumbers(JLabel scoreLabel)
    {
        FillGrid(0);
        for (int i = 0; i<81; i++)
        {
            cells[i].setEditable(false);
        }
        SaveSolvedGrid();
        RandomizeGrid();
        AddCellListeners(scoreLabel);
    }

    public boolean IfAvailable(int n, int pos)
    {
        int rowIndex = pos/9;
        int columnIndex = pos%9;
        
        for (int i = rowIndex*9; i<(rowIndex*9)+9; i++)
        {
            if (i == pos) continue;

            if (cells[i].getText().equals(String.valueOf(n)))
            {
                return false;
            }
        }

        for (int i = columnIndex; i < 81; i += 9) {
            if (i == pos) continue;

            if (cells[i].getText().equals(String.valueOf(n))) {
                return false;
            }
        }

        int blockRowStart = (rowIndex / 3) * 3;
        int blockColStart = (columnIndex / 3) * 3;

        for (int r = blockRowStart; r < blockRowStart + 3; r++) {
            for (int c = blockColStart; c < blockColStart + 3; c++) {
                int i = r * 9 + c;
                if (i == pos) continue;

                if (cells[i].getText().equals(String.valueOf(n))) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean FillGrid(int pos) {
        if (pos == 81) return true;

        java.util.List<Integer> candidates = new ArrayList<>(numbers);
        Collections.shuffle(candidates);

        for (int candidate : candidates) {
            if (IfAvailable(candidate, pos)) {
                cells[pos].setText(String.valueOf(candidate));

                if (FillGrid(pos + 1)) {
                    return true;
                }

                cells[pos].setText(""); 
            }
        }

        return false;
    }

    public void RandomizeGrid()
    {
        Random rand = new Random();
        int number;

        for (int i = 0; i<81; i++)
        {
            number = rand.nextInt(2);
            if (number == 0)
            {
                cells[i].setEditable(true);
                cells[i].setText("");
            }
        }
    }

    public void SaveSolvedGrid() {
        for (int i = 0; i < 81; i++) {
            cellsCopy[i] = Integer.parseInt(cells[i].getText());
        }
    }

    public void AddCellListeners(JLabel scoreLabel) {
        for (int i = 0; i < 81; i++) {
            final int index = i;

            cells[i].addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    String text = cells[index].getText().trim();
                    if (text.isEmpty()) return;

                    try {
                        int entered = Integer.parseInt(text);
                        if (entered == cellsCopy[index]) {
                            cells[index].setForeground(Color.BLUE);
                            cells[index].setEditable(false);
                            score += 500; 
                            scoreLabel.setText("Score: " + score);
                            checkIfComplete();
                        } else {
                            cells[index].setForeground(Color.RED);
                        }
                    } catch (NumberFormatException e2) {
                        // ignore non-numeric input
                    }
                }
            });
        }
    }  
    
    private void useHint(JButton hintButton, JLabel scoreLabel) {
        if (hintsUsed >= MAX_HINTS) {
            hintButton.setEnabled(false);
            return;
        }

        java.util.List<Integer> blankIndices = new ArrayList<>();
        for (int i = 0; i < 81; i++) {
            if (cells[i].getText().trim().isEmpty()) {
                blankIndices.add(i);
            }
        }

        if (blankIndices.isEmpty()) return; // no blanks left, nothing to hint

        Random rand = new Random();
        int chosen = blankIndices.get(rand.nextInt(blankIndices.size()));

        cells[chosen].setText(String.valueOf(cellsCopy[chosen]));
        cells[chosen].setForeground(Color.BLUE);
        cells[chosen].setEditable(false);

        hintsUsed++;
        hintButton.setText("Hint (" + (MAX_HINTS - hintsUsed) + " left)");

        score -= 50; // penalty for using a hint
        scoreLabel.setText("Score: " + score);
        checkIfComplete();
    }

    private void checkIfComplete() {
        for (int i = 0; i < 81; i++) {
            if (!cells[i].getText().equals(String.valueOf(cellsCopy[i]))) {
                return; // not done yet
            }
        }
        swingTimer.stop();
        JOptionPane.showMessageDialog(this, "You solved it! Final Score: " + score);
    }
} 
    
