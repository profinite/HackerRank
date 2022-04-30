
import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class Result {

    /*
     * Tabulate the number of times the athlete breaks
     * season records for max and min scored points.
     *
     * Trial with reductions, contains odd pattern with non-entangling 
     * side effect as an experiment.
     *
     * 0(n) runtime. Java 15
     */
    public static List<Integer> breakingRecords(List<Integer> scores) {
        final int initial = scores.get(0);
        int min = scores.stream().reduce(initial, Result::tallyMin);
        int max = scores.stream().reduce(initial, Result::tallyMax);
        return List.of(exceedCount, deceedCount);
    }
    
    private static int tallyMax(Integer max, Integer score) {
        if(score > max) exceedCount++;
        return Math.max(score, max);        
    }
    private static int exceedCount = 0;
    private static int deceedCount = 0;
    private static int tallyMin(Integer min, Integer score) {
        if(score < min) deceedCount++;
        return Math.min(score, min);        
    }
}




public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> scores = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
            .map(Integer::parseInt)
            .collect(toList());

        List<Integer> result = Result.breakingRecords(scores);

        bufferedWriter.write(
            result.stream()
                .map(Object::toString)
                .collect(joining(" "))
            + "\n"
        );

        bufferedReader.close();
        bufferedWriter.close();
    }
}
