import java.io.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class Result {

    /*
     * Count occurrences of a partial string
     * 0(m) - find, 0(m) - add
     * where m is length of string
     */
    public static List<Integer> contacts(List<List<String>> queries) {
        List<Integer> counts = new ArrayList<>();
        Trie contacts = new Trie();
        for(List<String> query : queries) {
            String command = query.get(0);
            String clause = query.get(1);
            if(command.equals("add")) {
                contacts.add(clause);
            } else if (command.equals("find")) {
                counts.add(contacts.count(clause));
            }
        }
        return counts;
    }
}
/* Simplified Trie
 * Note the terminal is signified by an empty string "" */
class Trie {
    HashMap<Character, Trie> trie = new HashMap<>();
    int size = 0;
    void add(String clause) {
        if(clause.length() == 0) {
            size++;
            return;
        }
        Character head = clause.charAt(0);
        String tail = clause.substring(1);
        trie.computeIfAbsent(head, k -> new Trie()).add(tail);
        size++;
    }
    int count(String partial) {
        if(partial.length() == 0)
            return size;
        Character head = partial.charAt(0);
        String tail = partial.substring(1);
        if(!trie.containsKey(head)) 
            return 0;
        return trie.get(head).count(tail);
    }
}







public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int queriesRows = Integer.parseInt(bufferedReader.readLine().trim());

        List<List<String>> queries = new ArrayList<>();

        IntStream.range(0, queriesRows).forEach(i -> {
            try {
                queries.add(
                    Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                        .collect(toList())
                );
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        List<Integer> result = Result.contacts(queries);

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
