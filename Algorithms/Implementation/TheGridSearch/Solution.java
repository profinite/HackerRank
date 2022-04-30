import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;

class Result {

    /*
     * Find a special pattern of digits, given a decimal string.
     * Simply parses the string and applies native string matching abilities.
     * 
     *
     */
    public static String gridSearch(List<String> G, List<String> P) {
        for(int i = 0; i < G.size(); i++) {
            if(isMatchAtRow(G, P, i))
                return "YES";
        }
        return "NO";        
    }
    
    private static boolean isMatchAtRow(List<String> G, List<String> P, int row) {       
        String gridline = G.get(row);
        for(int i = 0; i < gridline.length(); i++) {
           i = gridline.indexOf(P.get(0), i);
           if(i == -1) break;
           if(isMatchAtColumn(G, P, row, i)) 
                return true;
        }
        return false;
    }
    
    private static boolean isMatchAtColumn(List<String> G, List<String> P, int row, int col) {
        for(int i = 1; i < P.size(); i++) {
            boolean matched = G.get(row + i).startsWith(P.get(i), col);
            if(!matched) 
                return false;
        }
        return true;
    }
}





/* Standard boilerplate for Hackerrank Java problems. */
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        int t = Integer.parseInt(bufferedReader.readLine().trim());
        IntStream.range(0, t).forEach(tItr -> {
            try {
                String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");
                int R = Integer.parseInt(firstMultipleInput[0]);
                int C = Integer.parseInt(firstMultipleInput[1]);
                List<String> G = IntStream.range(0, R).mapToObj(i -> {
                    try {
                        return bufferedReader.readLine();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                    .collect(toList());
                String[] secondMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");
                int r = Integer.parseInt(secondMultipleInput[0]);
                int c = Integer.parseInt(secondMultipleInput[1]);
                List<String> P = IntStream.range(0, r).mapToObj(i -> {
                    try {
                        return bufferedReader.readLine();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                    .collect(toList());
                String result = Result.gridSearch(G, P);
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
