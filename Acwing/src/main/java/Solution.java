public class Solution {
    public static void main(String[] args) {
        int v = 7;
        int[][] arr = {{6,1}, {1,5}, {0,29}};
        int len = arr.length;
        // arr[0]: 占用空间，arr[1]: 数量
        int[][] dp = new int[len+1][v+1];
        int[] nums = new int[v+1];

        for (int i = 1; i <= len; i++) {
            for (int j = 1; j <= v; j++) {
                for (int k = 0; k <= arr[i-1][1]; k++) {
                    dp[i][j] = dp[i-1][j];

                    if(j >= k*arr[i-1][0]) {
                        if(dp[i-1][j-k*arr[i-1][0]] + k*arr[i-1][0] >= dp[i-1][j]) {
                            nums[j] = nums[j-k*arr[i-1][0]] + k;
                            dp[i][j] = dp[i-1][j-k*arr[i-1][0]] + k*arr[i-1][0];
                        }
                    }
                }
            }
        }
        System.out.printf("最大容量为: %d, 物品数目为: %d\n", dp[len][v], nums[v]);
    }
}
