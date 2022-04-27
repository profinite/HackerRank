import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;

class Result {

    /*
     * Count potential flights between buildings of commensurate height (h). 
     * Only count when the interstitial skyline is of lesser height.
     * Double the tally to count return trips.
     *
     * 0(n log n)
     *
     * Uses an AVL tree, to efficiently prune shorter buildings (headMap.clear).
     *
     * Note: as provided, the original snippet has a bug for Java. Contestants must
     * expand the return type to long or fail test cases 13 and 14.
     */

    public static long countFlights(List<Integer> bldgs) {
        TreeMap<Integer, Long> skyline = new TreeMap<>();
        long flightSum = 0;
        for(Integer h : bldgs) {
            flightSum += skyline.getOrDefault(h, 0l);
            skyline.merge(h, 1l, Long::sum);
            skyline.headMap(h).clear();
        }
        flightSum *= 2;
        return flightSum;
    }

}



public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int arrCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> arr = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
            .map(Integer::parseInt)
            .collect(toList());

        long result = Result.countFlights(arr);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
