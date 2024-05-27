import java.io.*;
import java.util.*;

/** Easy/basic
  * https://www.hackerrank.com/challenges/qheap1/problem
  */
public class Solution {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int q = sc.nextInt();
        PriorityQueue<Long> queue = new PriorityQueue<>();
        while(sc.hasNext()) {
            int command = sc.nextInt();
            if(command == 3) {
                System.out.println(queue.peek());
                continue;
            }
            long clause = sc.nextLong();
            switch(command) {
                case 1:
                    queue.add(clause);
                    break;
                case 2:
                    queue.remove(clause);
                    break;
                default:
                    throw new IllegalStateException("invalid command: " + command);
            }
        }
    }
}
