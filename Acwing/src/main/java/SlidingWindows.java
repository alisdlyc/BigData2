import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class SlidingWindows {
    public static void main(String[] args) {
        int[] input = new int[]{1,3,-1,-3,5,3,6,7};
        int winSize = 3;
        Deque<Integer> deque = new LinkedList<>();

        deque.addFirst(0);
        for (int i = 0; i < winSize; i++) {
            while (!deque.isEmpty() && input[i] >= input[deque.peekLast()]) deque.pollLast();
            deque.offerLast(i);
        }

        List<Integer> re = new LinkedList<>();
        re.add(input[deque.peekFirst()]);
        for (int i = winSize; i < input.length; i++) {
            while (!deque.isEmpty() && input[i] >= input[deque.peekLast()]) deque.pollLast();
            deque.offerLast(i);
            while (deque.peekFirst() <= i - winSize) deque.pollFirst();
            re.add(input[deque.peekFirst()]);
        }
        for(int x : re) System.out.println(x);
        re.toArray();
    }
}
