
import java.io.*;
import java.util.*;
import static java.util.stream.Collectors.toList;
import java.awt.Point;
import java.util.stream.IntStream;

class Result {
    /*
     * Implementation problem
     * Potential 0(n^y) space complexity
     */
    public static int minimumMoves(List<String> grid, int startX, int startY, int goalX, int goalY) {
        Deque<Node> queue = new ArrayDeque<>();
        Node[][] world = worldOf(grid);
        Node start = world[startX][startY];
        Node goal = world[goalX][goalY];
        if(goal.isBlocked || start.isBlocked)
            throw new IllegalStateException("Unreachable goal");
        queue.add(start);
        start.distance = 0;
        while(!queue.isEmpty()) {
            Node current = queue.pop();
            if(current.equals(goal))
                return current.distance;
            queue.addAll(neighborsOf(current, world));
        }
        throw new IllegalStateException("Couldn't find goal: " + goal);
    }
    private static List<Node> neighborsOf(Node c, Node[][] world) {
        List<Point> ordinals = Arrays.asList(
                new Point(1, 0),  // RIGHT
                new Point(-1, 0), // LEFT
                new Point(0, 1),  // UP
                new Point(0, -1));// DOWN
        List<Node> neighbors = new ArrayList<>();
        for(Point dx : ordinals) {
            for(Point p = c.getLocation(); isValid(p, world.length); p.translate(dx.x, dx.y)) {
                Node next = world[p.x][p.y];
                if(next.isBlocked)
                    break;
                if(next.isFresh()) {
                    next.distance = c.distance + 1;
                    neighbors.add(next);
                }
            }
        }
        return neighbors;
    }
    final private static class Node extends Point {
        int distance = -1;
        boolean isBlocked = false;
        public boolean isFresh() {
            return distance == -1;
        }
        public Node (int x, int y){
            super(x, y);
        }
    }
    private static Node[][] worldOf(List<String> grid) {
        final int n = grid.size();
        Node[][] world = new Node[n][n];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                world[x][y] = new Node(x, y);
                if(grid.get(x).charAt(y) == 'X')
                    world[x][y].isBlocked = true;
            }
        }
        return world;
    }
    private static boolean isValid(Point c, int n) {
        return c.x >= 0 && c.y >= 0 && c.x < n && c.y < n;
    }
}
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<String> grid = IntStream.range(0, n).mapToObj(i -> {
                    try {
                        return bufferedReader.readLine();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .collect(toList());

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int startX = Integer.parseInt(firstMultipleInput[0]);

        int startY = Integer.parseInt(firstMultipleInput[1]);

        int goalX = Integer.parseInt(firstMultipleInput[2]);

        int goalY = Integer.parseInt(firstMultipleInput[3]);

        int result = Result.minimumMoves(grid, startX, startY, goalX, goalY);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
