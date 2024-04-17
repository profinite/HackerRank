import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

class Result {
    public static List<Integer> largestPermutation(int k, List<Integer> arr) {
        List<Element> orig = arr.stream().map(Element::new).toList();
        Deque<Element> sorted = orig.stream().sorted(comparingInt(Element::value).reversed()).collect(toCollection(ArrayDeque::new));
        arr.clear();
        for(Element current : orig) {
            Element largest = sorted.pop();
            if(largest.value() > current.value() && k > 0) {
                arr.add(largest.value());
                largest.swapped = current; // allow this value to be swapped later
                k--;
            } else {
                arr.add(current.value());
            }
        }
        return arr;
    }
    static class Element {
        private int value = 0;
        public Element swapped;
        public int value() {
            if(swapped == null)
                return value;
            return swapped.value();
        }
        public Element(int value) {
            this.value = value;
        }
    }
}


// HackerRank Boilerplate
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int n = Integer.parseInt(firstMultipleInput[0]);

        int k = Integer.parseInt(firstMultipleInput[1]);

        List<Integer> arr = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                .map(Integer::parseInt)
                .collect(toList());

        List<Integer> result = Result.largestPermutation(k, arr);
        System.out.println(result);
        bufferedReader.close();
    }
}
