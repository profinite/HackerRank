import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class Result {

    /*
     * Parse the time in numerals into English words.
     */
    public static String timeInWords(int h, int m) {
        final int halfHour = 30;
        String time = "";
        String hour = Time.toWord(h);
        if(m == 0) {
            time = hour + " o' clock";
        } else if(m <= halfHour) {
            time = Time.asMinutes(m) + " past " + hour;
        } else {
            int nextHour = h + 1;
            if(h == 12) 
                nextHour = 1;
            time = Time.asMinutes(2*halfHour - m) + " to " + Time.toWord(nextHour);
        }
        return time;
    }

}
class Time {
    public static String toWord(int i) {
        final int cap = 20;
        String rightHand = "";
        final List<String> numbers = List.of("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
                "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen");
        assert(i != 0);
        if(i < numbers.size()) 
            return numbers.get(i);
        if(i > cap) 
            rightHand = " " + numbers.get(i - cap);
        return "twenty" + rightHand;
    }
    
    public static String asMinutes(int m) {
        Map<Integer, String> quarters = Map.of(15, "quarter", 30, "half");
        if(quarters.containsKey(m)) 
            return quarters.get(m);
        String descriptor = "minute";
        if(m != 1) 
            descriptor += "s";
        return toWord(m) + " " + descriptor;
    }
}





/* Standard boilerplate for Hackerrank Java problems. */
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        int h = Integer.parseInt(bufferedReader.readLine().trim());
        int m = Integer.parseInt(bufferedReader.readLine().trim());
        String result = Result.timeInWords(h, m);
        bufferedWriter.write(result);
        bufferedWriter.newLine();
        bufferedReader.close();
        bufferedWriter.close();
    }
}
