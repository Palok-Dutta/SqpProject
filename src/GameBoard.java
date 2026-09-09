import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameBoard {
    public final Node topLeft = new Node("A", 150, 100);
    public final Node topCenter = new Node("B", 300, 100);
    public final Node topRight = new Node("C", 450, 100);
    public final Node center = new Node("D", 300, 270);
    public final Node bottomLeft = new Node("E", 150, 440);
    public final Node bottomCenter = new Node("F", 300, 440);
    public final Node bottomRight = new Node("G", 450, 440);

    private final List<Node> nodes = Arrays.asList(
            topLeft, topCenter, topRight, center, bottomLeft, bottomCenter, bottomRight);
    private final List<Edge> edges;

    public GameBoard() {
        edges = Arrays.asList(
                connect(topLeft, topCenter), connect(topCenter, topRight),
                connect(bottomLeft, bottomCenter), connect(bottomCenter, bottomRight),
                connect(topCenter, center), connect(center, bottomCenter),
                connect(topLeft, center), connect(topRight, center),
                connect(bottomLeft, center), connect(bottomRight, center));
    }

    private Edge connect(Node first, Node second) {
        first.connect(second);
        second.connect(first);
        return new Edge(first, second);
    }

    public List<Node> getNodes() { return Collections.unmodifiableList(nodes); }
    public List<Edge> getEdges() { return Collections.unmodifiableList(edges); }
}