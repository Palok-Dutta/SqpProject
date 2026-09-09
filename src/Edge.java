public class Edge {
    private final Node first;
    private final Node second;

    public Edge(Node first, Node second) {
        this.first = first;
        this.second = second;
    }

    public Node getFirst() { return first; }
    public Node getSecond() { return second; }
}