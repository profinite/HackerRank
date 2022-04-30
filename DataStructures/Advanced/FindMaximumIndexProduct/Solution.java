import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;

class Result {

    /*
     * For each sequent, find the nearest neighbor (dominant) in each
     * direction and ultimately the maximum of their products.
     *
     * 0(n) runtime using a simple stack solution.
     * Adds 0(n) extra space.
     * 
     * In pursuit of the DRY principle, to handle the lefthand product
     * we just reverse the list and flip the indices. Could be clearer
     * but it saves writing a new function.
     *
     * Note the original boilerplate has a bug, this function must
     * return a long to handle test cases #0 and #11.
     */
    public static long solve(List<Integer> arr) {
        List<Integer> right = nearestDominants(arr);
        Collections.reverse(arr);
        List<Integer> left = reverse(nearestDominants(arr));
        
        return IntStream.range(0, arr.size())
            .mapToLong(i-> (long)right.get(i) * left.get(i))
            .max()
            .orElse(0);
    }
   
    /* Starting from the end, test if each element dominates any previous ones.
     * If so, add them to the appropriate location in the list. 
     *
     * Note history tracks the index of each integer element rather than
     * the integer itself. Since dominants are found out-of-sequence,
     * uses a placeholder array to store them. */
    private static List<Integer> nearestDominants(List<Integer> list) {
        Deque<Integer> history = new ArrayDeque<>();
        final int length = list.size();
        Integer[] dominants = new Integer[length];
        
        /* Begin from the righthand side. */
        final int start = length - 1;
        for(int i = start; i >= 0; i--) {
            while(!history.isEmpty()) {
                int prior = history.peek();
                if(list.get(i) <= list.get(prior)) {
                     break;
                }
                dominants[prior] = i + 1;
                history.pop();
            }
            history.push(i);
        }
        /* For any unmatched extrema, assign zero as required. */
        while(!history.isEmpty()) {
            int extremum = history.pop();
            dominants[extremum] = 0;
        }
        return Arrays.asList(dominants);
    }
    
    /* Helper function to handle the lefthand product case. 
     * Flip indices but also preserve the value of extrema (0) */
    private static List<Integer> reverse(List<Integer> arr) {
        Collections.reverse(arr);
        return arr.stream()
            .map(i -> (i == 0) ? 0: arr.size() - i + 1)
            .collect(toList());
    }
 
}





/* Hackerrank's cringeworthy boilerplate for Java  */
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        int arrCount = Integer.parseInt(bufferedReader.readLine().trim());
        List<Integer> arr = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
            .map(Integer::parseInt)
            .collect(toList());
        long result = Result.solve(arr);
        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();
        bufferedReader.close();
        bufferedWriter.close();
    }
}
