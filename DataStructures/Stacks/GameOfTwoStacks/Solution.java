import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class Result {

    /*
     * Remove maximal quantity of integers from two stacks while
     * keeping their sum below a particular limit ("maxSum").
     *
     * Solvable by simply pushing a maximum quantity of stack1 integers into a
     * queue. Then progressively adding elements from stack2, exploring for
     * the highest count.
     *
     * 0(m + n) runtime, for m and n elements in the two stacks.
     */
    public static int twoStacks(int maxSum, List<Integer> a, List<Integer> b) {
        Deque<Integer> queue = new ArrayDeque<>();
        int sum = 0;
        for(Integer x : a) {
            if(sum + x > maxSum) 
                break;
            queue.push(x);
            sum += x;
        }
        int count = queue.size();
        int maxCount = count;
        
        /* Now progressively add elements from the second stack. */
        search:
        for(Integer x : b) {
            while(sum + x > maxSum) {
                if(queue.isEmpty())
                    break search;
                sum -= queue.pop();
                count--;
            }
            sum += x;
            count++;
            maxCount = Math.max(maxCount, count);
        }
        return maxCount;
    }
}






/* Hackerrank's standard boilerplate stub for Java. */
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int g = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, g).forEach(gItr -> {
            try {
                String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");
                int maxSum = Integer.parseInt(firstMultipleInput[2]);
                List<Integer> a = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                    .map(Integer::parseInt)
                    .collect(toList());
                List<Integer> b = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                    .map(Integer::parseInt)
                    .collect(toList());

                int result = Result.twoStacks(maxSum, a, b);
                bufferedWriter.write(String.valueOf(result));
                bufferedWriter.newLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        bufferedReader.close();
        bufferedWriter.close();
    }
}
