public class Tiger {
    private Node position;

    public Tiger(Node startingPosition) { position = startingPosition; }
    public Node getPosition() { return position; }
    void moveTo(Node destination) { position = destination; }
}