public class Solution2 {
    public static void main(String[] args) {
        int[] arr = new int[]{2,1};
        int last = 0;
        int re = 0;
        for (int i = 0; i < arr.length; i++) {
            re += reachTarget(arr[i] - last);
            System.out.println(re);
            last = arr[i];
        }
    }

    public static int reachTarget(int end) {
        int k = 0;
        end = end > 0 ? end : -end;
        while (end > 0) end -= ++k;
        return end % 2 == 0 ? k : k+1+k%2;
    }
}
