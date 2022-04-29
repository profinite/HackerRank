import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class Result {

    /*
     * Rank a player successively, relative to a sequence of 
     * leaderboard scores ("ranked"), wherein equal results 
     * share a tier: aka dense ranking.
     * 
     * For elegance, creates histogram of leaderboard. As 
     * optimization, filters duplicate leads.
     * 
     * 0((m + n)log m) runtime, for m leads and n scores.
     * 0(m) added space complexity for tree
     */
    public static List<Integer> climbingLeaderboard(List<Integer> ranked, List<Integer> player) {
        final TreeMap<Integer, Integer> histogram = ranked.stream()
                .distinct()
                .collect(TreeMap::new, 
                    (m, s) -> m.put(s, m.size() + 1), 
                    Map::putAll); 
                    
        return player.stream()
            .map(i -> getRank(histogram, i))
            .collect(toList());
    }
    /* Find rank of any leads below our score, otherwise take the lowest rank. */
    private static Integer getRank(TreeMap<Integer, Integer> m, Integer score) {
        Map.Entry<Integer, Integer> rank = m.floorEntry(score);
        if(rank == null) 
            return m.size() + 1;
        return rank.getValue(); 
    }
}





/* Standard Hackerrank Boilerplate for java */
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        int rankedCount = Integer.parseInt(bufferedReader.readLine().trim());
        List<Integer> ranked = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
            .map(Integer::parseInt)
            .collect(toList());
        int playerCount = Integer.parseInt(bufferedReader.readLine().trim());
        List<Integer> player = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
            .map(Integer::parseInt)
            .collect(toList());
        List<Integer> result = Result.climbingLeaderboard(ranked, player);
        bufferedWriter.write(
            result.stream()
                .map(Object::toString)
                .collect(joining("\n"))
            + "\n"
        );
        bufferedReader.close();
        bufferedWriter.close();
    }
}
