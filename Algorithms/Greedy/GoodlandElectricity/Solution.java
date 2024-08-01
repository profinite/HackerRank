import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;

class Result {
    /**
     * Calculate the power plants to service a country's power grid
     * https://www.hackerrank.com/challenges/pylons/problem
     *
     * No need for greedy algorithms, here we just store the
     * buildable cities with an interval tree and count the
     * number of intervals, achieving a sublinear runtime once
     * the tree is built.
     *
     * 𝚯(N) setup and space
     * 𝚯(|M| log |N|) runtime for M stations and N cities.
     *
     * @param k range of each power plant
     * @param nation all cities [0, 1, 0, 0] etc
     * @return minimal number of power plants or -1 if impossible
     */
    public static int pylons(int k, List<Integer> nation) {
        NavigableSet<Integer> cities = IntStream.range(0, nation.size())
                .filter(i -> nation.get(i) == 1) // only store buildable cities
                .boxed()
                .collect(Collectors.toCollection(TreeSet::new));
        int count = 0;
        for(int i = 0; i < nation.size(); i += k, count++) {
            Integer station = cities.lower(i + k); // next buildable city
            if(station == null) return -1;
            i = station;
            cities = cities.tailSet(station, false);
        }
        return count;
    }

    /**
     * Naive alternative using greedy algorithm for comparison
     */
    public static int pylons2(int k, List<Integer> arr) {
        final int length = arr.size();
        if(length == 1)
            return arr.getFirst() == 1 ? 1 : 0;
        int leftmost = Math.min(k, length);
        leftmost--; // zero-based
        while(arr.get(leftmost) == 0) {
            if(leftmost == 0) return -1; // base case
            leftmost--;
        }
        int count = -1;
        for(int i = Math.min(length - 1, leftmost + k); count == -1; i--) {
            if(i == leftmost) return -1;
            count = pylons(k, arr.subList(i, length));
        }
        return count + 1;
    }
}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("/tmp/input.txt")));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int n = Integer.parseInt(firstMultipleInput[0]);

        int k = Integer.parseInt(firstMultipleInput[1]);

        List<Integer> arr = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                .map(Integer::parseInt)
                .collect(toList());

        int result = Result.pylons(k, arr);
        System.out.println(result);

        bufferedReader.close();
    }
}
