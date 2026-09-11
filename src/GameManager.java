import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameManager {
    public enum Turn { TIGER, BLOCKER }

    private final GameBoard board;
    private final Tiger tiger;
    private final Blocker firstBlocker;
    private final Blocker secondBlocker;
    private Turn turn = Turn.TIGER;
    private Blocker selectedBlocker;
    private boolean tigerSelected;
    private boolean gameOver;
    private final Random random = new Random();

    public GameManager(GameBoard board) {
        this.board = board;
        tiger = new Tiger(board.center);
        firstBlocker = new Blocker(board.topLeft);
        secondBlocker = new Blocker(board.bottomRight);
    }

    public GameBoard getBoard() { return board; }
    public Tiger getTiger() { return tiger; }
    public Blocker getFirstBlocker() { return firstBlocker; }
    public Blocker getSecondBlocker() { return secondBlocker; }
    public Turn getTurn() { return turn; }
    public boolean isGameOver() { return gameOver; }

    public List<Node> getSelectableNodes() {
        if (gameOver) return Collections.emptyList();
        if (turn == Turn.TIGER && tigerSelected) return getFreeNeighbors(tiger.getPosition());
        if (turn == Turn.BLOCKER && selectedBlocker != null) return getFreeNeighbors(selectedBlocker.getPosition());
        return Collections.emptyList();
    }

    public boolean selectTiger(Node node) {
        if (turn != Turn.TIGER || gameOver || node != tiger.getPosition()) return false;
        tigerSelected = true;
        return true;
    }

    public boolean moveTiger(Node destination) {
        if (turn != Turn.TIGER || !tigerSelected || !getSelectableNodes().contains(destination)) return false;
        tiger.moveTo(destination);
        tigerSelected = false;
        turn = Turn.BLOCKER;
        return true;
    }

    public boolean moveTigerRandomly() {
        if (turn != Turn.TIGER || gameOver) return false;
        List<Node> availableNodes = getFreeNeighbors(tiger.getPosition());
        if (availableNodes.isEmpty()) {
            gameOver = true;
            return false;
        }
        tiger.moveTo(availableNodes.get(random.nextInt(availableNodes.size())));
        turn = Turn.BLOCKER;
        return true;
    }

    public boolean selectBlocker(Node node) {
        if (turn != Turn.BLOCKER || gameOver) return false;
        if (node == firstBlocker.getPosition()) selectedBlocker = firstBlocker;
        else if (node == secondBlocker.getPosition()) selectedBlocker = secondBlocker;
        else return false;
        return true;
    }

    public boolean moveBlocker(Node destination) {
        if (turn != Turn.BLOCKER || selectedBlocker == null || !getSelectableNodes().contains(destination)) return false;
        selectedBlocker.moveTo(destination);
        selectedBlocker = null;
        turn = Turn.TIGER;
        if (!hasTigerMove()) gameOver = true;
        return true;
    }

    public boolean hasTigerMove() {
        return !getFreeNeighbors(tiger.getPosition()).isEmpty();
    }

    private List<Node> getFreeNeighbors(Node position) {
        List<Node> freeNeighbors = new ArrayList<>();
        for (Node neighbor : position.getNeighbors()) {
            if (!isOccupied(neighbor)) freeNeighbors.add(neighbor);
        }
        return freeNeighbors;
    }

    private boolean isOccupied(Node node) {
        return node == tiger.getPosition()
                || node == firstBlocker.getPosition()
                || node == secondBlocker.getPosition();
    }
}