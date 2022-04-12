import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

class Node {
    public int value;
    public Node next;
}
public class MapIterator {
    private static Node[] arr;
    private static boolean[] st;
    private static int[] dis;

    public static void main(String[] args) {
        int N = 6;
        int[][] input = {{0,1}, {1,4}, {4,2}, {1,3}, {3,5}, {5,2}};
        arr = new Node[N];
        st = new boolean[N];
        dis = new int[N];
        Arrays.fill(dis, -1);

        for (int[] ints : input) {
            add(ints[0], ints[1]);
        }

        bfs(0);
            for (int di : dis) {
            System.out.println("" + di);
        }
    }

    // 插入a->b
    public static void add(int a, int b) {
        Node cur = new Node();
        cur.value = b;
        cur.next = arr[a];
        arr[a] = cur;
    }
    public static void dfs(int u) {
        st[u] = true;
        System.out.println(u);
        Node cur = arr[u];
        while (cur != null) {
            int j = cur.value;
            cur = cur.next;
            if(!st[j]) dfs(j);
        }
    }

    public static void bfs(int start) {
        dis[start] = 0;
        Deque<Node> queue = new LinkedList<>();
        queue.addLast(arr[start]);
        int t = 0, n = 0;
        while (!queue.isEmpty()) {
            int len = queue.size();
            t = n;
            n = queue.peekLast().value;
            while (len-- > 0) {
                Node tmp = queue.removeFirst();
                while (tmp != null) {
                    int j = tmp.value;
                    if(dis[j] == -1) {
                        dis[j] = dis[t] + 1;
                        if(arr[j] != null) queue.addLast(arr[j]);
                    }
                    tmp = tmp.next;
                }
            }
        }
    }
}
