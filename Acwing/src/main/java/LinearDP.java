public class LinearDP {
    public static void main(String[] args) {
        int[][] arr = {{7}, {3,8}, {8,1,0}, {2,7,4,4}, {4,5,2,6,5}};
        int N = arr.length+1;
        int[][] input = new int[N][N];

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                input[i+1][j+1] = arr[i][j];
            }
        }

        int[][] dp = new int[N][N];
        for (int i = 1; i < N; i++) {
            for (int j = 1; j <= i; j++) {
                dp[i][j] = Math.max(dp[i-1][j-1], dp[i][j-1]) + input[i][j];
                System.out.printf("%d,", dp[i][j]);
            }
            System.out.println();
        }
        int re = 0;
        for (int i = 1; i < N; i++) {
            re = Math.max(re, dp[N-1][i]) ;
        }
        System.out.println(re);

        addSubSequences();

        String a = "wq";
        a.length();
    }

    private static void addSubSequences() {
        int[] arr = new int[]{3,1,2,1,8,5,6};
        // 1, 2, 5, 6
        int[] dp = new int[arr.length];
        dp[0] = 1;
        for (int i = 1; i < arr.length; i++) {
            dp[i] = dp[i-1];
            if(arr[i] > arr[i-1]) dp[i]++;
        }
        System.out.println(""+dp[arr.length-1]);
    }
}
