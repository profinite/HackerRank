import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.*;

class Result {
  
    /*
     * Count the maximal subset of integers in a sequence ('S')
     * which are not divisible by a constant ('k').
     *
     * Builds a map of each integer to its modulus ("modulae") then
     * filters out the potential factors of each divisor, for 0(n) runtime.
     *
     * FIXME: Overall delineation and flow could be more crisp.
     */
    public static int nonDivisibleSubset(int k, List<Integer> s) {
        if(s.stream().distinct().count() != s.stream().count()) 
            throw new IllegalArgumentException("Set contains duplicates");
            
        s = s.stream().sorted().collect(toList());
        
        Map<Integer, List<Integer>> modulae = s.stream().collect(groupingBy(i -> i % k));
        modulae.forEach((key, value) -> System.err.println(key + ":" + value));                
        
        if(modulae.containsKey(0))
            limitOne(modulae.get(0));
        
        modulae = filterSummandsOf(modulae, k);
        long cardinality = modulae
                .values()
                .stream()
                .flatMap(List::stream)
                .count();
        
        return Math.toIntExact(cardinality);
    }
    
    /* Helper function to collect one element. */
    private static List<Integer> limitOne(List<Integer> l) {
        l.retainAll(l.stream().limit(1).collect(toList()));
        return l;
    }
    /* 
     * Filter out any potential summands in the map. Must
     * use an iterator to modify the list concurrently.
     *
     * FIXME: Improve flow and loop internals
     */
    private static Map<Integer, List<Integer>> filterSummandsOf(Map<Integer, List<Integer>> m, int k) {
        Iterator<Integer> it = m.keySet().iterator();
        while (it.hasNext()) {
            Integer x = it.next();
            final int y = k - x;
            if(x > k || x < 0 || y < 1) 
                throw new IllegalArgumentException("Internal hashmap error.");
            if(!m.containsKey(y)) 
                continue;
            if(x == y) {
                limitOne(m.get(x));
                continue;
            }
            if(m.get(x).size() <= m.get(y).size()) 
                it.remove(); 
        }
        return m;
    }

}




/* Standard Hackerrank boilerplate for Java. */
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");
        int n = Integer.parseInt(firstMultipleInput[0]);
        int k = Integer.parseInt(firstMultipleInput[1]);
        List<Integer> s = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
            .map(Integer::parseInt)
            .collect(toList());
        int result = Result.nonDivisibleSubset(k, s);
        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();
        bufferedReader.close();
        bufferedWriter.close();
    }
}
