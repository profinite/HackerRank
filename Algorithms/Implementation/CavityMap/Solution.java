import java.io.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

class Result {
    /* 
     * Detect all the pits ("cavities") on an oceanographic map. 
     * 
     * Begin by parsing the string into a cartesian grid,
     * then inspect neighbors of each (x, y) cell.
     *
     * For elegance, unifies the search pattern with
     * a central representaion of the four ordinal directions,
     * in pursuit of the DRY principle.
     * 
     * 0(n) runtime, as predictable.
     */
    enum Ordinal {
        N(1, 0),
        S(-1, 0),
        E(0, 1),
        W(0, -1);
        private final Point p;
        Ordinal(int x, int y) {
            p = new Point(x, y);
        }
        public Point translate(Point c) {
            return new Point(c.x + p.x, c.y + p.y);
        }
    }
    public static List<String> cavityMap(List<String> grid) {
        final int n = grid.size();
        List<String> topo = new ArrayList<>();
        for (int i = 0; i < n; i++)
            topo.add(mapLatitude(grid, i));
        return topo;
    }
    
    private static String mapLatitude(List<String> grid, int row) {
        StringBuffer latitude = new StringBuffer();
        for (int i = 0; i < grid.get(row).length(); i++) {
            Point current = new Point(row, i);
            if (isCavity(grid, current)) {
                latitude.append('X');
            } else {
                latitude.append(grid.get(row).charAt(i));
            }
        }
        return latitude.toString();
    }
    
    private static boolean isCavity(List<String> grid, Point p) {
        if(isBorder(grid, p)) {
            return false;
        }
        return isLocalMin(grid, p);
    }
    
    private static boolean isBorder(List<String> grid, Point p) {
       final int maxRow = grid.size() - 1;
       final int maxCol = grid.get(p.x).length() - 1;
       return p.x == 0 || p.y == 0 || p.x == maxRow || p.y == maxCol;
    }
    
    private static int getDepth(List<String> grid, Point p) {
        return Character.digit(grid.get(p.x).charAt(p.y), 10);
    }
    
    private static boolean isLocalMin(List<String> grid, Point current) {
        int depth = getDepth(grid, current);
        for(Ordinal d : Ordinal.values()) {
            int neighbor = getDepth(grid, d.translate(current));
            if(neighbor >= depth) return false;
        }
        return true;
    }
}





/* Standard Hackerrank boilerplate for Java. */
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

        List<String> result = Result.cavityMap(grid);

        bufferedWriter.write(
            result.stream()
                .collect(joining("\n"))
            + "\n"
        );

        bufferedReader.close();
        bufferedWriter.close();
    }
}
