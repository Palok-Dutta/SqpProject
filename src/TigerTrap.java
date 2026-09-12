import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class TigerTrap extends JFrame {
    public TigerTrap() {
        setTitle("Trap the Tiger");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new TigerTrapPanel(score -> { }));
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TigerTrap().setVisible(true));
    }
}
