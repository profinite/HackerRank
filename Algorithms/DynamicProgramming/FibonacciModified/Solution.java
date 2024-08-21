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
     * Simple computation of a recurrence relation
     * https://www.hackerrank.com/challenges/fibonacci-modified/
     *
     * 0(n) runtime
     * 0(1) space complexity
     */

    public static BigInteger fibonacciModified(int t1, int t2, int n) {
        BigInteger tt1 = BigInteger.valueOf(t1), tt2 = BigInteger.valueOf(t2);
        for(int i = 2; i < n; i++) {
            BigInteger last = fibMod(tt1, tt2);
            tt1 = tt2;
            tt2 = last;
        }
        return tt2;
    }
    /* Similar recurrence to Fibonacci series
     * T[i + 2] = t[i] + t[i + 1] ^ 2 
     */
    private static BigInteger fibMod(BigInteger t1, BigInteger t2) {
        return t1.add(t2.multiply(t2));
    }
}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int t1 = Integer.parseInt(firstMultipleInput[0]);

        int t2 = Integer.parseInt(firstMultipleInput[1]);

        int n = Integer.parseInt(firstMultipleInput[2]);

        BigInteger result = Result.fibonacciModified(t1, t2, n);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
