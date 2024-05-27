import java.math.BigInteger;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.range;





        /**
         * Model a game with stacks of plates
         * Use streams to model the Sieve of Eratosthenes
         */
    static IntPredicate isPrime = x -> true;
    static IntStream primes = IntStream.iterate(2, i -> i + 1)
            .filter(i -> isPrime.test(i))
            .peek(i -> isPrime = isPrime.and(v -> v % i != 0));
    public static List<Integer> waiter(List<Integer> numbers, int q) {
        List<Integer> memo = primes.limit(q).boxed().collect(toList());
        List<Integer> answers = new ArrayList<>();
        for(int i = 0; i < q; i++) {
            Deque<Integer> nonfactors = new ArrayDeque<>();
            int prime = memo.get(i);
            for(Integer plate : numbers) {
                if(plate % prime == 0)
                    answers.add(plate);
                else
                    nonfactors.push(plate);
            }
            numbers.clear();
            numbers.addAll(nonfactors);
        }
        Collections.reverse(numbers);
        answers.addAll(numbers);
        return answers;

    }

