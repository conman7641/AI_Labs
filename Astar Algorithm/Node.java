public class Node {

    private String type;
    private int x;
    private int y;
    private int g;
    private int h;
    private int f;
    private Node parent;

    public Node(String type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public Node() {
        this.type = "";
        this.x = -1;
        this.y = -1;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getF() {
        return f;
    }

    public void setF() {
        this.f = g + h;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public String toString() {
        return  "(" + x + ", " + y + ")";
    }

}
