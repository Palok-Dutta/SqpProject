import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node {
    private final String name;
    private final Point position;
    private final List<Node> neighbors = new ArrayList<>();

    public Node(String name, int x, int y) {
        this.name = name;
        this.position = new Point(x, y);
    }

    public String getName() { return name; }
    public Point getPosition() { return new Point(position); }
    public List<Node> getNeighbors() { return Collections.unmodifiableList(neighbors); }

    void connect(Node other) {
        if (!neighbors.contains(other)) neighbors.add(other);
    }
}