public class Arrangement {
    private static boolean[] bool;
    private static int[] path;
    private static int N = 10;
    private static int n = 9;

    public static void main(String[] args) {
        bool = new boolean[N];
        path = new int[N];
        dfs(0);
    }

    public static void dfs(int u) {
        if(u == n) {
            for (int i = 0; i < n; i++) {
                System.out.printf("%d", path[i]);
            }
            System.out.println();
            return;
        }
        for (int i = 1; i <= n; i++) {
            if(!bool[i]) {
                path[u] = i;
                bool[i] = true;
                dfs(u+1);
                bool[i] = false;
            }
        }
    }

}
