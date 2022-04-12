import java.util.Arrays;

public class Solution1 {
    public static void main(String[] args) {
        int[] arr = new int[]{1,1,3};
        Arrays.sort(arr);
        int len = arr.length;
        int evenLast = 0;
        int oddLast = 1;
        int evenRe = 1;
        int oddRe = 1;
        for (int i = 1; i < len; i++) {
            if(arr[i]-arr[evenLast]>1) {
                evenRe++;
                evenLast = i;
            }
            if(arr[i]-arr[oddLast]>1) {
                oddRe++;
                oddLast = i;
            }
        }
        System.out.println(Math.max(oddRe, evenRe));
    }
}
