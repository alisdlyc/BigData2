public class NQueen {

    private static int N = 20;
    private static int n = 4;
    private static char[][] row;
    private static boolean[] col, dg, udg;

    public static void main(String[] args) {
        row = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                row[i][j] = '.';
            }
        }
        col = new boolean[N];
        dg = new boolean[N];
        udg = new boolean[N];
        dfs(0);
    }

    public static void dfs(int u) {
        if(u == n) {
            for (int i = 0; i < row.length; i++) {
                System.out.println(row[i]);
            }
            System.out.println();
            return;
        }
        for (int i = 0; i < n; i++) {
            if(!col[i] && !dg[u+i] && !udg[n-u+i]) {
                row[u][i] = 'Q';
                col[i] = dg[u+i] = udg[n-u+i] = true;
                dfs(u+1);
                col[i] = dg[u+i] = udg[n-u+i] = false;
                row[u][i] = '.';
            }
        }
    }
}
