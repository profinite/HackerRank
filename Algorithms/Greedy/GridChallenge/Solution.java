import java.io.*;
import java.math.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class Result {
    static List<String> sorted(List<String> grid) {
        List<String> ordered = new ArrayList<>();
        for(String s : grid) {
            char[] sorted = s.toCharArray();
            Arrays.sort(sorted);
            ordered.add(new String(sorted));
        }
        return ordered;
    }
    public static String gridChallenge(List<String> grid) {
        List<String> ordered = sorted(grid);   
        List<String> columns = new ArrayList<>();
        for(int i = 0; i < ordered.get(0).length(); i++) {
            String current = "";
            for(int j = 0; j < grid.size(); j++) {
                current += ordered.get(j).charAt(i);
            }
            columns.add(current);
        }
        List<String> sorted = sorted(columns);
        if(sorted.equals(columns))
            return "YES";
        return "NO";
    }

}
