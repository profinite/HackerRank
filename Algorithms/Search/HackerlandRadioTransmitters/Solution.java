import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;

class Result {

    /**
     * Find the minimum radio antennae to cover the given
     * houses each at a given distance.
     * https://www.hackerrank.com/challenges/hackerland-radio-transmitters/
     *
     * Applies basic sliding window, with a BST to find
     * the optimal house within range for installation.
     *
     * 0(n log n) runtime complexity due to sorting
     *
     * @param x house locations
     * @return minimum antenna stations
     */
    public static int hackerlandRadioTransmitters(List<Integer> x, int k) {
        TreeMap<Integer, Boolean> atlas = new TreeMap<>();
        for(Integer index : x)
            atlas.put(index, false);
        int count = 0;
        Collections.sort(x);
        for(Integer index : x) {
            if(atlas.get(index))
                continue;
            int furthest = atlas.floorKey(index + k);
            atlas.subMap(index, ++furthest + k).replaceAll((a, b) -> true);
            count++;
        }
        return count;
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int n = Integer.parseInt(firstMultipleInput[0]);

        int k = Integer.parseInt(firstMultipleInput[1]);

        List<Integer> x = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                .map(Integer::parseInt)
                .collect(toList());

        int result = Result.hackerlandRadioTransmitters(x, k);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
