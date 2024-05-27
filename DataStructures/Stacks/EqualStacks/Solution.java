import java.util.*;
import java.util.stream.Collectors;

class Result {
    /**
     * Equalize a trio of stacks by removing their elements.
     * https://www.hackerrank.com/challenges/equal-stacks/
     * 
     * Here we hash each stack of cylinders to its height.
     * Elephantine but explores usage of IdentityHashMap in mapping
     * mutable Collections, generally a deprecated practice.
     * 
     * 𝚯(N) where N is total number of cylinders
     *
     * @param hx stacks of cylinders
     * @return final stack size
     * 
     * TODO: for some reason hashing an ArrayDeque doesn't trigger a
     * duplicate key for test case #2 (1 1 1). Interesting!
     */
    @SafeVarargs
    public static int equalStacks(List<Integer>... hx) {
        Map<Deque<Integer>, Integer> stacks = Arrays.stream(hx)
                .map(ArrayDeque::new)
                .collect(Collectors.toMap(h -> h,
                        h -> h.stream().reduce(0, Math::addExact),
                        (x, y) -> x,
                        IdentityHashMap::new)); // experimental
        int minHeight = Collections.min(stacks.values());
        while (stacks.values().stream().distinct().count() != 1)
            for (Map.Entry<Deque<Integer>, Integer> e : stacks.entrySet()) {
                while (e.getValue() > minHeight) // trim to the lowest stack
                    e.setValue(e.getValue() - e.getKey().pop());
                minHeight = e.getValue();
            }
        return minHeight;
    }
}
