public class Blocker {
    private Node position;

    public Blocker(Node startingPosition) { position = startingPosition; }
    public Node getPosition() { return position; }
    void moveTo(Node destination) { position = destination; }
}