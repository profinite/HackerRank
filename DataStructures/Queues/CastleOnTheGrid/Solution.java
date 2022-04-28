import java.io.*;
import java.util.*;
import java.awt.Point;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;

class Result {

    /*
     * Find the 'minimumMoves' to move a rook ("castle") 
     * across a chessboard to a goal square, avoiding obstacle pieces.
     * Rooks move in the four ordinal directions: N, S, E, W
     * 
     * 1) converts input to grid of Nodes
     * 2) finds reachable nodes
     * 3) performs unidirectional Breadth-First Search (BFS) for goal
     * 
     * 0(k^d) runtime, where k = number of nodes, d = required moves
     *
     */
    public static int minimumMoves(List<String> grid, int startX, int startY, int goalX, int goalY) {
        final Node[][] board = toChessboard(grid);
        Arrays.stream(board).flatMap(Arrays::stream).forEach(Node::addNeighbors);
        Node start = board[startX][startY];
        Node goal = board[goalX][goalY];
        return search(start, goal);
    }
    /* Canonical breadth first search (BFS), 
     * tracking number of steps to goal ("distance"). */
    private static int search(Node root, Node goal) {
        Deque<Node> queue = new ArrayDeque<>();
        queue.add(root);
        root.visited = true;
        while(!queue.isEmpty()) {
            Node nearest = queue.remove();
            if(nearest.equals(goal)) 
                return nearest.distance;
            for(Node n : nearest.neighbors) {
                if(!n.visited) {
                    n.visited = true;
                    n.distance = nearest.distance + 1;
                    queue.add(n);
                }
            }
        }
        throw new IllegalArgumentException("Goal square unreachable");
    }
  
    /* Parse textual grid into a board of (x, y) squares. */
    private static Node[][] toChessboard(List<String> grid) {
        final int width = grid.size();
        final int height = grid.get(0).length();
        Node[][] board = new Node[width][height];
      
        for(int i = 0; i < width; i++) {
            String row = grid.get(i);
            for(int j = 0; j < height; j++) {
                board[i][j] = new Node(i, j, board);
             
                if(row.charAt(j) == 'X') 
                    board[i][j].isBlocked = true;
            }
        }
        return board;
    }
}

/* Rook ("castle") may move in the four ordinal directions */
enum Direction {
    N(0, 1),
    S(0, -1),
    E(1, 0),
    W(-1, 0);
    private final int dx, dy;
    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
    public void move(Point p) {
        p.translate(dx, dy);
    }
}

/* Square of a chessboard, sited on (x, y) grid. 
 * Stores any neighbors reachable by a rook ("castle") */
class Node {
    final List<Node> neighbors = new ArrayList<>();
    final Node[][] board;
    final Point site;
    boolean isBlocked = false;
    boolean visited = false;
    int distance = 0;
    public Node(int x, int y, Node[][] board) {
        site = new Point(x, y);
        this.board = board;
    }
    /* Find squares reachable from me by a rook.
     * Continue in each ordinal path unless blocked */
    public void addNeighbors() {
        for(Direction ordinal : Direction.values()) {
            for(Point p = site.getLocation(); isValid(p); ordinal.move(p)) {
                Node current = board[p.x][p.y];
                if(current.isBlocked) break;
                neighbors.add(current);
            }
        }
    }
    private boolean isValid(Point p) {
        return p.x >= 0 && p.x < board.length
            && p.y >= 0 && p.y < board[0].length;
    }
}



/* Standard Hackerrank Java boilerplate */
public class Solution {
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
