import java.lang.reflect.Array;
import java.util.Arrays;

public class Dijkstra {
    static int N = 10;
    static int m = 3, n = 3;
    static int[][] g = new int[N][N];
    static int[][] d = new int[N][N];
    private static final int[] dist = new int[N];
    static boolean[] st = new boolean[N];
    public static void main(String[] args) {
        int[][] input = {{1,2,2}, {2,3,1}, {1,3,4}};
        for(int[] x : g) Arrays.fill(x, 0x3f);
        for(int[] x: input) g[x[0]][x[1]] = Math.min(g[x[0]][x[1]], x[2]);

        int re = dijkstra(1, 2);
        System.out.println("dijkstra: " + re);

        floyd();
        System.out.println("floyd: " + g[1][2]);
    }

    private static int dijkstra(int begin, int end) {
        Arrays.fill(dist, 0x3f);

        dist[begin] = 0;
        for (int i = 0; i < n; i++) {
            int t = -1;
            for (int j = 1; j <= n; j++) {
                if(!st[j] && (t == -1 || dist[t]>dist[j])) {
                    t = j;
                }
            }
            st[t] = true;
            for (int j = 1; j <= n; j++) {
                dist[j] = Math.min(dist[j], dist[t]+g[t][j]);
            }
        }
        return dist[end];
    }

    private static void floyd() {
        for (int k = 1; k <= n; k++) {
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    g[i][j] = Math.min(g[i][j], g[i][k]+g[k][j]);
                }
            }
        }
    }
}
