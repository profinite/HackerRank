import java.io.*;
import java.util.*;
import static java.util.stream.Collectors.*;

class Result {
    /**
     * Find the smallest possible region of a gene to splice
     * such that all 4 nucleotides are balanced (aka 'steady')
     * https://www.hackerrank.com/challenges/bear-and-steady-gene/problem
     *
     * Key observation:
     * 1. Identify surplus nucleotide(s) if any ("excess"). Ignore others.
     * 2. Form a candidate region ("intron")
     * 3. Slide the intron region to the right via sliding window
     * 4. Track the minimum intron length
     *
     * 𝚯(N) runtime for string of length N
     *
     * @param seq gene to be balanced
     * @return length of subsequence to splice for balance
     */
    public static int steadyGene(String seq) {
        List<Character> gene = seq.codePoints().mapToObj(c -> (char) c).toList();
        final int BALANCED = gene.size() / 4;
        Map<Character, Integer> excess = gene.stream().collect(groupingBy(x -> x, reducing(-BALANCED, e -> 1, Integer::sum)));
        excess.values().removeIf(e -> e < 1); // ignore minority nucleotides
        if(excess.isEmpty()) // already balanced
            return 0;
        Intron intron = new Intron(gene, excess);
        int smallest = intron.length();
        while(intron.head.hasNext()) { // sliding window
            intron.next(excess);
            smallest = Math.min(smallest, intron.length());
        }
        return smallest;
    }
    
    /** A genetic region to be spliced out ("intron")
     * https://en.wikipedia.org/wiki/Intron */
    static class Intron {
        final ListIterator<Character> head;
        final ListIterator<Character> tail;
        /** Form an initial, non-optimal intron region */
        public Intron(List<Character> gene, Map<Character, Integer> excess) {
            head = gene.listIterator();
            tail = gene.listIterator();
            do excess.computeIfPresent(head.next(), (k, v) -> --v);
            while (excess.values().stream().anyMatch(v -> v > 0));
        }

        /** Advance to the next viable intron
         * @param excess a count of surplus characters */
        public void next(Map<Character, Integer> excess) {
            excess.computeIfPresent(head.next(), (k, v) -> --v);
            Character last = tail.next(); // now prune the tail at left
            while (!excess.containsKey(last) || excess.get(last) < 0) {
                excess.computeIfPresent(last, (k, v) -> ++v);
                last = tail.next();
            }
            tail.previous();
        }
        /** Size of the genetic region to splice */
        public int length() {
            return head.previousIndex() - tail.previousIndex();
        }
    }
}
