import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TigerTrapPanel extends JPanel {
    private static final int NODE_RADIUS = 16;
    private final Runnable onGameOver;
    private GameManager game;
    private String message = "Tiger's turn: click the tiger to see its moves.";

    public TigerTrapPanel(Runnable onGameOver) {
        this.onGameOver = onGameOver;
        resetGame();
        setPreferredSize(new Dimension(600, 600));
        setBackground(new Color(248, 248, 244));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                handleClick(event.getX(), event.getY());
            }
        });
    }

    public void resetGame() {
        game = new GameManager(new GameBoard());
        message = "Tiger's turn: click the tiger to see its moves.";
        repaint();
    }

    private void handleClick(int x, int y) {
        Node clicked = findNode(x, y);
        if (clicked == null || game.isGameOver()) return;

        if (game.getTurn() == GameManager.Turn.TIGER) {
            if (!game.selectTiger(clicked)) {
                if (game.moveTiger(clicked)) message = "Blocker's turn: select a blocker.";
                else message = "Click the tiger, then click a highlighted point.";
            } else {
                message = "Tiger selected: choose a highlighted point.";
            }
        } else {
            if (game.selectBlocker(clicked)) {
                message = "Blocker selected: choose a highlighted point.";
            } else if (game.moveBlocker(clicked)) {
                if (game.isGameOver()) {
                    message = "The tiger is trapped. Blockers win!";
                    repaint();
                    javax.swing.JOptionPane.showMessageDialog(this, message);
                    onGameOver.run();
                    return;
                }
                message = "Tiger's turn: click the tiger to see its moves.";
            } else {
                message = "Select a blocker, then click a highlighted point.";
            }
        }
        repaint();
    }

    private Node findNode(int x, int y) {
        for (Node node : game.getBoard().getNodes()) {
            if (node.getPosition().distance(x, y) <= NODE_RADIUS + 8) return node;
        }
        return null;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(new Color(55, 55, 55));
        g.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (Edge edge : game.getBoard().getEdges()) {
            drawLine(g, edge.getFirst(), edge.getSecond());
        }

        for (Node node : game.getSelectableNodes()) {
            drawCircle(g, node, 25, new Color(255, 205, 70));
        }
        for (Node node : game.getBoard().getNodes()) {
            drawCircle(g, node, NODE_RADIUS, new Color(235, 235, 230));
        }

        drawPiece(g, game.getTiger().getPosition(), new Color(208, 55, 45), "T");
        drawPiece(g, game.getFirstBlocker().getPosition(), new Color(45, 95, 170), "1");
        drawPiece(g, game.getSecondBlocker().getPosition(), new Color(35, 135, 90), "2");

        g.setColor(new Color(35, 35, 35));
        g.setFont(new Font("SansSerif", Font.BOLD, 18));
        g.drawString(message, 28, 38);
        g.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g.drawString("Yellow rings show valid destinations.", 28, getHeight() - 22);
        g.dispose();
    }

    private void drawLine(Graphics2D g, Node first, Node second) {
        g.drawLine(first.getPosition().x, first.getPosition().y,
                second.getPosition().x, second.getPosition().y);
    }

    private void drawCircle(Graphics2D g, Node node, int radius, Color color) {
        int x = node.getPosition().x - radius;
        int y = node.getPosition().y - radius;
        g.setColor(color);
        g.fillOval(x, y, radius * 2, radius * 2);
    }

    private void drawPiece(Graphics2D g, Node node, Color color, String label) {
        drawCircle(g, node, 13, color);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        int textWidth = g.getFontMetrics().stringWidth(label);
        g.drawString(label, node.getPosition().x - textWidth / 2, node.getPosition().y + 5);
    }
}