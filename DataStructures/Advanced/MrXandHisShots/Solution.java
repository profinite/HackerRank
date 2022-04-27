import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;

class Result {

    /*
     * Tally the batsman shots fieldable by each player.
     * Uses quicksort for 0(n log n + m log m) initialization.
     * Once sorted, prune the shots inapplicable for future players.
     */

    public static int solve(List<List<Integer>> shots, List<List<Integer>> team) {
        final List<Range> batter = toRange(shots);
        int strength = 0;
        for(Range fielder : toRange(team)) {
            Iterator<Range> it = batter.iterator();
            while(it.hasNext()) {
                Range shot = it.next();
                if(shot.end < fielder.start) {
                    it.remove();
                    continue;
                }
                if(shot.start > fielder.end) break;
                strength++;
            }
        }
        return strength;
    }
    private static List<Range> toRange(List<List<Integer>> list) {
        return list.stream()
            .map(Range::new)
            .sorted(Comparator.comparingInt(x -> x.start))
            .collect(toList());
    }
    
}
class Range {
    final int start;
    final int end;
    public Range(List<Integer> range) {
        start = range.get(0);
        end = range.get(1);
    }
    @Override
    public String toString() {
        return "{" + start + ", " + end + "}";
    }
}



public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int n = Integer.parseInt(firstMultipleInput[0]);

        int m = Integer.parseInt(firstMultipleInput[1]);

        List<List<Integer>> shots = new ArrayList<>();

        IntStream.range(0, n).forEach(i -> {
            try {
                shots.add(
                    Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                        .map(Integer::parseInt)
                        .collect(toList())
                );
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        List<List<Integer>> players = new ArrayList<>();

        IntStream.range(0, m).forEach(i -> {
            try {
                players.add(
                    Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                        .map(Integer::parseInt)
                        .collect(toList())
                );
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        int result = Result.solve(shots, players);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
