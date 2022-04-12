public class Solution2 {
    public static void main(String[] args) {
        int[] arr = new int[]{-5,1,-2};
        int len = arr.length;
        int[] dp = new int[len];
        int[] dpReverse = new int[len];
        dp[0] = arr[0];
        dpReverse[0] = arr[len-1];
        for (int i = 1; i < len; i++) {
            dp[i] = Math.max(dp[i-1]+arr[i], arr[i]);
            dpReverse[i] = Math.max(dpReverse[i-1]+arr[len-1-i], arr[len-1-i]);
        }
        int re = dp[0];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len-i-1; j++) {
                re = Math.max(re, dp[i]+dpReverse[j]);
            }
        }
        System.out.println(re);
    }
}
