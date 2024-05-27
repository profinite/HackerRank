import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;

class Result {

    /**
     * Find the first string within a list which creates a prefix
     * relationship with any other string.
     * https://www.hackerrank.com/challenges/one-week-preparation-kit-no-prefix-set/p
     *
     * Applies a sorted map. Reporting the first occurrence is
     * rather troublesome: This requires tracking the index of
     * each prefix relationship and making a second pass.
     *
     * (n log n) or higher runtime
     * 0(n) space complexity (red-black tree)
     */
    public static void noPrefix(List<String> words) {
       TreeMap<String, Integer> sorted = new TreeMap<>();
       int min = Integer.MAX_VALUE; // index of first observable prefix
       int index = 0;
       for (String word : words) {
            if (sorted.containsKey(word)) // check for identical
                min = Math.min(min, index);
            sorted.putIfAbsent(word, index++); // sort the strings
        }
        for (String word : words.stream().distinct().toList()) {
            String next = sorted.higherKey(word);
            while (next != null && next.startsWith(word)) {
                min = Math.min(min, Math.max(sorted.get(word), sorted.get(next)));
                next = sorted.higherKey(next);
            }
        }
        if(min != Integer.MAX_VALUE)
            System.out.println("BAD SET\n" + words.get(min));
        else
            System.out.println("GOOD SET");
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("/tmp/input.txt")));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<String> words = IntStream.range(0, n).mapToObj(i -> {
                    try {
                        return bufferedReader.readLine();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .collect(toList());

        Result.noPrefix(words);

        bufferedReader.close();
    }
}

import java.io.*;
        import java.math.*;
        import java.security.*;
        import java.text.*;
        import java.util.*;
        import java.util.concurrent.*;
        import java.util.regex.*;


        import java.util.Collections;
class Result {

    /*
     * Complete the 'pairs' function below.
     *
     * The function is expected to return an INTEGER.
     * The function accepts following parameters:
     *  1. INTEGER k
     *  2. INTEGER_ARRAY arr
     */

    // x - y = k
// y = -k + x
// y = k - x

    public static int pairs(int k, List<Integer> arr) {
        //List<Integer> minuends = arr.stream().map(x -> x - k).sorted().collect(Collectors.toList());
        List<Integer> minuends = new ArrayList<>(arr);
        for(int i = 0; i < minuends.size(); i++) {
            minuends.set(i, Math.abs(minuends.get(i) - k));
        }

        Collections.sort(minuends);
        Collections.sort(arr);
        System.err.println(arr);
        System.err.println(minuends);
        int count = 0;
        for(int i = 0, j = 0; i < arr.size() && j < arr.size(); ) {
            if(Objects.equals(arr.get(i), minuends.get(j))) {
                count++;
                j++;
            }
            else if(arr.get(i) > minuends.get(j)) {
                j++;
            } else {
                i++;
            }
        }
        System.err.println("Result is: " + count);
        return count;
    }

}

import java.io.*;
        import java.math.*;
        import java.security.*;
        import java.text.*;
        import java.util.*;
        import java.util.concurrent.*;
        import java.util.function.*;
        import java.util.regex.*;
        import java.util.stream.*;
        import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;



class Result {

    /*
     * Complete the 'palindromeIndex' function below.
     *
     * The function is expected to return an INTEGER.
     * The function accepts STRING s as parameter.
     */

    public static int palindromeIndex(String s) {
        final int mid = s.length() / 2;
        final int end = s.length() - 1;
        int pruneIndex = -1 ;
        System.err.println(s);
        for(int i = 0, j = end; i <= mid; i++, j--) {
            char first = s.charAt(i);
            char last = s.charAt(j);
            if(first != last) {
                if(pruneIndex != -1)
                    return -1;
                if(j == mid || i == mid) // no characters to match
                    return -1;
                char penultimate = s.charAt(j - 1);
                char next = s.charAt(i + 1);
                if(first == penultimate) {
                    pruneIndex = j;
                    j--;
                }
                else if (next == last) {
                    pruneIndex = i;
                    i++;
                }
                else
                    return -1;
            }
        }
        return pruneIndex;
    }

}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int q = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, q).forEach(qItr -> {
            try {
                String s = bufferedReader.readLine();

                int result = Result.palindromeIndex(s);

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

import java.io.*;
        import java.math.*;
        import java.security.*;
        import java.text.*;
        import java.util.*;
        import java.util.concurrent.*;
        import java.util.function.*;
        import java.util.regex.*;
        import java.util.stream.*;
        import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;



class Result {

    /**
     * Find the shortest distance from a start node to every other node.
     * https://www.hackerrank.com/challenges/bfsshortreach/problem
     * 
     * Straight-forward BFS overall, must take care of isolated nodes though,
     * including when the start node itself is isolated.
     */
    public static List<Integer> bfs(int n, int m, List<List<Integer>> edges, int s) {
        Map<Integer, Node> forest = new HashMap<>();
        for(List<Integer> e : edges) {
            int source = e.get(0);
            int sink = e.get(1);
            forest.putIfAbsent(source, new Node(source));
            forest.putIfAbsent(sink, new Node(sink));
            Node x = forest.get(source);
            Node y = forest.get(sink);
            x.neighbors.add(y);
            y.neighbors.add(x);
        }
        List<Integer> distances = new ArrayList<>();
        Node start = forest.get(s);
        System.err.println(forest);
        for(int i = 1; i <= n; i++) {
            if(i == s)
                continue;
            if(!forest.containsKey(i)) {
                distances.add(-1);
                continue;
            }
            distances.add(distanceTo(i, start));
        }
        return distances;
    }
    static int distanceTo(int target, Node start) {
        Deque<Node> queue = new ArrayDeque<>();
        Node current = start;
        final int EDGE_WEIGHT = 6;
        queue.add(current);
        while(!queue.isEmpty()) {
            current = queue.pop();
            if(current.visited)
                continue;
            current.visited = true;
            if(current.name == target) {
                return current.distance;
            }
            for(Node n : current.neighbors){
                if(n.visited)
                    continue;
                n.distance = current.distance + EDGE_WEIGHT;
            }
            queue.addAll(current.neighbors);
        }
        System.err.println("Couldn't find: " + target);
        return -1;
    }
    static class Node {
        boolean visited = false;
        int distance = 0;
        int name = 0;
        Set<Node> neighbors = new HashSet<>();
        public Node(int name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "visited=" + visited +
                    ", distance=" + distance +
                    ", name=" + name +
                    ", neighbors=" + neighbors +
                    '}';
        }
    }

}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int q = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, q).forEach(qItr -> {
            try {
                String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

                int n = Integer.parseInt(firstMultipleInput[0]);

                int m = Integer.parseInt(firstMultipleInput[1]);

                List<List<Integer>> edges = new ArrayList<>();

                IntStream.range(0, m).forEach(i -> {
                    try {
                        edges.add(
                                Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                                        .map(Integer::parseInt)
                                        .collect(toList())
                        );
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                int s = Integer.parseInt(bufferedReader.readLine().trim());

                List<Integer> result = Result.bfs(n, m, edges, s);

                bufferedWriter.write(
                        result.stream()
                                .map(Object::toString)
                                .collect(joining(" "))
                                + "\n"
                );
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        bufferedReader.close();
        bufferedWriter.close();
    }
}

