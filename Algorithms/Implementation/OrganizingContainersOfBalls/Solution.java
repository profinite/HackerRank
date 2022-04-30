import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;

class Result {

    /*
     * Sort each type of ball into its own container, limited
     * to only swap operations and other criteria. Output if
     * the sort is feasible.
     * 
     * Simply tally each container's contents to find their sizes.
     * Then count each ball type and see if a match is feasible.
     *
     * 0(n + m) runtime for n containers and m ball types
     */

    public static String organizingContainers(List<List<Integer>> containerList) {
        List<Integer> containerSizes = containerList.stream()
            .map(i -> i.stream().reduce(0, Integer::sum))
            .sorted()
            .collect(Collectors.toList());
            
        // sum each ball type to determine total count            
        int n = containerList.size();
        List<Integer> ballCounts = IntStream.range(0, n)
            .map(i -> sumColumn(containerList, i))
            .sorted()
            .boxed().collect(Collectors.toList());

        String result = "Impossible";
        if(containerSizes.equals(ballCounts))
            result = "Possible";
            
        System.err.println(containerSizes.toString() + " balls: " 
                + ballCounts.toString());
        System.out.println(result);        
        return result;
    }
    private static int sumColumn(final List<List<Integer>> arr, final int col) {
        return IntStream.range(0, arr.size())
                .map(x -> arr.get(x).get(col))
                .reduce(0, Integer::sum);
    }
}








/* Standard hackerrank boilerplate for Java. */
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        int q = Integer.parseInt(bufferedReader.readLine().trim());
        IntStream.range(0, q).forEach(qItr -> {
            try {
                int n = Integer.parseInt(bufferedReader.readLine().trim());
                List<List<Integer>> container = new ArrayList<>();
                IntStream.range(0, n).forEach(i -> {
                    try {
                        container.add(
                            Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                                .map(Integer::parseInt)
                                .collect(toList())
                        );
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                String result = Result.organizingContainers(container);
                bufferedWriter.write(result);
                bufferedWriter.newLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        bufferedReader.close();
        bufferedWriter.close();
    }
}
