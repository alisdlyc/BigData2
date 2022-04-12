public class BinarySearch {
    public static void main(String[] args) {
        // 返回元素k在数组里面的起始位置和终止位置，若不存在返回-1,-1
        int[] arr = new int[]{1,2,2,3,3,4};
        int target = 2;
        int l = 0, r = arr.length-1;
        while (l < r) {
            int mid = l + r >> 1;
            if(arr[mid] >= target) r = mid;
            else l = mid + 1;
        }
        // 此时l为答案的左边界
        if(arr[l] != target) System.out.println("-1,-1");
        else {
            System.out.printf("%d, ", l);
            l = 0; r = arr.length - 1;
            while (l < r) {
                int mid = l + r + 1 >> 1;
                if(arr[mid] <= target) l = mid;
                else r = mid - 1;
            }
            // 此时r为答案的右边界
            System.out.printf("%d\n", r);
        }
    }
}