import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.util.Arrays;
import java.util.Map;
import java.util.List;
import java.awt.Point;
import static java.util.stream.Collectors.toList;

class Result {
    
    /* Count the squares available to a queen on large (10000x10000) 
     * chessboard, for the given starting square and ~9000 obstacle pieces. 
     * Queens move in both cardinal and ordinal directions. 
     * 
     * As an optimization, cache the nearest obstacles. 
     *
     * For elegance, capture the valid cardinals and abstract 
     * identical movement and checking patterns for each vector.
     * 
     * FIXME: Overall delineation of objects here could be more crisp.
     *
     * 0(n + k) runtime, for nxn board and k obstacles
     */
    public static int queensAttack(int n, int k, int r_q, int c_q, List<List<Integer>> obstacles) {
        Square queen = new Square(r_q, c_q);
        reachable = setBlankBoard(queen, n);
        for(List<Integer> ob : obstacles.stream().distinct().collect(toList())) {
            Square piece = new Square(ob.get(0), ob.get(1));
            if (queen.inRange(piece)) 
                addIfCloser(queen, piece);
        }
        return reachable.values()
            .stream()
            .mapToInt(queen::span)
            .reduce(0, Integer::sum);
    }
  
    /* Count each square the queen can move to. i
     * As optimization, cache previous obstacles. */
    static void addIfCloser(Square queen, Square piece) {
        Direction heading = queen.directionOf(piece);
        Square endpoint = heading.previous(piece);
        if(queen.span(endpoint) < queen.span(reachable.get(heading)))
            reachable.replace(heading, endpoint);
    }
    static Map<Direction, Square> setBlankBoard(Square queen, int n) {
        return Direction.all()
                .collect(Collectors.toMap(d->d, d->queen.translate(d, n)));
    }
    private static Map<Direction, Square> reachable;
}

/* Each node of the (x, y) chessboard. */
class Square extends Point {
    Square(int x, int y) {
        super(x, y);
    }
    Square copy() {
        return new Square(x, y);
    }
    int span(Square p) {
        return Math.max(Math.abs(this.x - p.x), Math.abs(this.y - p.y));
    }
    Direction directionOf(Square c) {
        return Direction.match(subtrahend(c.x, x), subtrahend(c.y, y));
    }
    boolean inRange(Square p) {
        return isCardinal(p) || isOrdinal(p);
    }
    Square translate(Direction d, int n) {
        Square endpoint = this.copy();
        Square next = this.copy();
        while(next.isInside(n)) {
            endpoint = next.copy();
            next = d.next(next);
        }
        return endpoint;
    }
    private boolean isInside(int n) {
        return 0 < x && x <= n &&
                0 < y && y <= n;
    }
    private int subtrahend(int p, int q) {
        return Math.toIntExact(Math.round(Math.signum(p - q)));
    }
    private boolean isCardinal(Square p) {
        return x == p.x || y == p.y;
    }
    private boolean isOrdinal(Square p) {
        return Math.abs(x - p.x) == Math.abs(y - p.y);
    }
}

/* To simplify code, capture all ordinal and cardinal 
 * directions as movement of Euclidean points. */
enum Direction {
    N( 1,   0),
    S(-1,  0),
    E( 0,   1),
    W( 0,  -1),
    NE(1,   1),
    SE(-1,  1),
    SW(-1, -1),
    NW(1,  -1);

    private final int dx, dy;
    private Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
    private Square move(Square input, int x, int y) {
        Square result = input.copy();
        result.translate(x, y);
        return result;
    }
    Square next(Square input) {
        return move(input, dx, dy);
    }
    Square previous(Square input) {
        return move(input, -dx, -dy);
    }
    static Direction match(int dx, int dy) {
        return Direction.all().filter(i -> i.equals(dx, dy)).findFirst().get();
    }
    boolean equals(int dx, int dy) {
        return (this.dx == dx) && (this.dy == dy);
    }
    static Stream<Direction> all() {
        return Arrays.stream(Direction.values());
    }
}



/* Standard Hackerrank Java boilerplate. */
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");
        int n = Integer.parseInt(firstMultipleInput[0]);
        int k = Integer.parseInt(firstMultipleInput[1]);
        String[] secondMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");
        int r_q = Integer.parseInt(secondMultipleInput[0]);
        int c_q = Integer.parseInt(secondMultipleInput[1]);
        List<List<Integer>> obstacles = new ArrayList<>();
        IntStream.range(0, k).forEach(i -> {
            try {
                obstacles.add(
                    Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                        .map(Integer::parseInt)
                        .collect(toList())
                );
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        int result = Result.queensAttack(n, k, r_q, c_q, obstacles);
        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();
        bufferedReader.close();
        bufferedWriter.close();
    }
}
