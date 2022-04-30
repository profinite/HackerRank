import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;

class Result {

    /*
     * Find all the Kaprekar numbers in a given range of integers.
     *
     * Kaprekars are integers whose square can form numerals which sum
     * to the original integer.
     * 
     * Begin by enumerating all integers in the range
     */

    public static void kaprekarNumbers(int p, int q) {
        List<Integer> K = IntStream.rangeClosed(p, q)
            .filter(Result::isKaprekar)
            .filter(i -> i > 0)
            .boxed()
            .collect(toList());
            
        /* format the numbers as required */
        String output = K.stream().map(String::valueOf).collect(Collectors.joining(" "));
        if(K.size() == 0)  {
            output = "INVALID RANGE";
        }
        
        System.out.println(output);
    }
  
    /* Detect if a Kaprekar number  */
    private static boolean isKaprekar(int n) {
        long nSquared = Math.round(Math.pow(n, 2));
        int d = String.valueOf(n).length();
        String str = String.valueOf(nSquared);
        if(!isValidLength(str, d))
            return false;

        int midpoint = str.length() - d;
        String right = str.substring(midpoint);
        String left = str.substring(0, midpoint);
        int r = Integer.valueOf(right);
        int l = 0;
        if(!left.equals(""))
            l = Integer.valueOf(left);
        return r + l == n;
    }
    private static boolean isValidLength(String str, int d) {
        return str.length() == 2 * d || str.length() == (2 * d - 1);       
    }

}






/* Standard Hackerrank boilerplate for Java */
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int p = Integer.parseInt(bufferedReader.readLine().trim());
        int q = Integer.parseInt(bufferedReader.readLine().trim());
        Result.kaprekarNumbers(p, q);
        bufferedReader.close();
    }
}
