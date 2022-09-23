import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class astar {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Map file to use: ");
        String mapFileString = in.nextLine();
        File mapFile = new File(mapFileString);
        String mapFileVal;
        System.out.print("barber file to use: ");
        String barberFileString = in.nextLine();
        File barberFile = new File(barberFileString);
        int numRows = 0;
        int numCols = 0;
        int timeStamp = 0;
        Node[][] map = null;
        Node start = new Node();
        Node goal = new Node();
        ArrayList<Node> openList = new ArrayList<Node>();
        ArrayList<Node> closedList = new ArrayList<Node>();
        ArrayList<Node> path = new ArrayList<Node>();
        ArrayList<Node> barbers = new ArrayList<Node>();
        try {
            Scanner sc = new Scanner(mapFile);
            while (sc.hasNextLine()) {
                mapFileVal = sc.next();
                switch(mapFileVal) {
                    case("M"):
                        numRows = sc.nextInt();
                        numCols = sc.nextInt();
                        map = new Node[numRows][numCols];
                        break;
                    case("S"):
                        int x = sc.nextInt();
                        int y = sc.nextInt();
                        start = new Node(mapFileVal, x, y);
                        map[x][y] = start;
                        break;
                    case("G"):
                        x = sc.nextInt();
                        y = sc.nextInt();
                        goal = new Node(mapFileVal, x, y);
                        map[x][y] = goal;
                        break;
                    case("W"):
                        x = sc.nextInt();
                        y = sc.nextInt();
                        Node wall = new Node(mapFileVal, x, y);
                        map[x][y] = wall;
                        break;
                    default:
                        break;
                }
            }
            sc.close();
            Scanner scanner = new Scanner(barberFile);
            while (scanner.hasNextLine()) {
                if (scanner.hasNextLine()) {
                    int time = scanner.nextInt();
                    if (time == -1) {
                        break;
                    }
                    int row = scanner.nextInt();
                    int col = scanner.nextInt();
                    Node barber = new Node(String.valueOf(time), row, col);
                    barbers.add(barber);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        //Fill in the holes of the map
        fillMap(map, numRows, numCols);

        //place timestep barbers on map
        placeBarbers(map, barbers, timeStamp);
        //add the start node to the open list
        //set the huristic value of the start node
        openList.add(start);
        start.setG(0);
        start.setH(getDistanceVal(start, goal));
        start.setF();


        //Find the path using the astar algorithm
        Astar(start, goal, map, openList, closedList, path);
        timeStamp = printMap(map, timeStamp);

        //recaluclate the path
        while (path.size() > 0) {
            start = recalculate(map, start, goal, openList, closedList, path);   //resets the Lists and adds the new start to the list
            placeBarbers(map, barbers, timeStamp);      //Places the new barbers to the map
            Astar(start, goal, map, openList, closedList, path);    //Algo
            timeStamp = printMap(map, timeStamp);   //Prints the map, increments timestamp
        }
    }

    public static void  fillMap(Node[][] map, int numRows, int numCols) {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (map[i][j] == null) {
                    map[i][j] = new Node(" ", i, j);
                }
            }
        }
    }

    public static void placeBarbers(Node[][] map, ArrayList<Node> barbers, int timeStamp) {
        for (int i = 0; i < barbers.size(); i++) {
            int barberTime = Integer.parseInt(barbers.get(i).getType());
            if (timeStamp == barberTime) {
                map[barbers.get(i).getX()][barbers.get(i).getY()].setType("B");
            }
        }
    }

    //removes the barbers, Moves Scandro one step down the path and resets all the values and lists
    public static Node recalculate(Node[][] map, Node start, Node goal, ArrayList<Node> openList, ArrayList<Node> closedList, ArrayList<Node> path) {
        //remove the barbers and path
        for (Node[] nodes : map) {
            for (int j = 0; j < map[0].length; j++) {
                if (nodes[j].getType().equals("B") || nodes[j].getType().equals("*")) {
                    nodes[j].setType(" ");
                }
            }
        }
        if (path.size() > 0) {
            Collections.reverse(path);
            start = path.get(0);  //Moves scandro to the next node
            start.setType("S");     //Sets the new start node to S
            path.clear();
            openList.clear();
            closedList.clear();
            openList.add(start);
            start.setG(0);
            start.setH(getDistanceVal(start, goal));
            start.setF();
        }
        return start;
    }
    public static int printMap(Node[][] map, int timeStamp) {
        System.out.println("Time Stamp: " + timeStamp);
        timeStamp +=1;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                System.out.print(map[i][j].getType() + " ");
            }
            System.out.println();
        }
        System.out.println();
        return timeStamp;
    }

    //Astar algorithm
    public static void Astar(Node start, Node goal, Node[][] map, ArrayList<Node> openList, ArrayList<Node> closedList, ArrayList<Node> path) {
        Node current;
        while (!openList.isEmpty()) {
            current = openList.get(0);
            for (Node node : openList) {
                if (node.getF() < current.getF()) {
                    current = node;
                }
            }
            if (current == goal) {
                path.add(current);
                while (current != start) {
                    current = current.getParent();
                    path.add(current);
                }
                break;
            }
            openList.remove(current);
            closedList.add(current);
            ArrayList<Node> neighbors = getNeighbors(current, map);
            for (Node neighbor : neighbors) {
                if (closedList.contains(neighbor)) {
                    continue;
                }
                int tempG = current.getG() + getDistanceVal(current, neighbor);    //gets the gVal by getting the gVal of the current and adding 1 (distance to neighbor) to it
                if (!openList.contains(neighbor)) {
                    openList.add(neighbor);
                } else if (tempG >= neighbor.getG()) {
                    continue;
                }
                neighbor.setParent(current);
                neighbor.setG(tempG);
                neighbor.setH(getDistanceVal(neighbor, goal));
                neighbor.setF();
            }
        }
        if (path.isEmpty()) {
            System.out.println("NO PATH");
        }
        path.remove(0);
        path.remove(path.size() - 1);
        for (int i = 0; i < path.size(); i++) {
            map[path.get(i).getX()][path.get(i).getY()].setType("*");
        }
    }

    private static ArrayList<Node> getNeighbors(Node current, Node[][] map) {
        ArrayList<Node> neighbors = new ArrayList<Node>();
        int col = current.getX();
        int row = current.getY();
        if (col > 0) {
            if (!map[col - 1][row].getType().equals("W") && !map[col - 1][row].getType().equals("B")) {
                neighbors.add(map[col - 1][row]);   //up
            }
        }
        if (col < map.length - 1) {
            if (!map[col + 1][row].getType().equals("W") && !map[col + 1][row].getType().equals("B")) {
                neighbors.add(map[col + 1][row]);      //down
            }
        }
        if (row < map[0].length - 1) {
            if (!map[col][row + 1].getType().equals("W") && !map[col][row + 1].getType().equals("B")) {
                neighbors.add(map[col][row + 1]);   //right
            }
        }
        if (row > 0) {
            if (!map[col][row - 1].getType().equals("W") && !map[col][row - 1].getType().equals("B")) {
                neighbors.add(map[col][row - 1]);   //left
            }
        }
        return neighbors;
    }

    public static int getDistanceVal(Node a, Node b) {
        //a.setH(hValue);
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }
}
