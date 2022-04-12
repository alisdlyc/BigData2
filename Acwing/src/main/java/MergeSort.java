public class MergeSort {
    private static int[] tmp;

    public static void main(String[] args) {
        int[] arr = new int[]{5,4,3,2,1,8,9,6,4,2,6,8};
        tmp = new int[arr.length];
        mergesort(arr, 0, arr.length-1);
        for(int x: arr) System.out.printf("%d ", x);
    }
    public static void mergesort(int[] arr, int l, int r) {
        if(l >= r) return;
        int mid = l + r >> 1;
        mergesort(arr, l, mid);
        mergesort(arr, mid+1, r);

        int i = l, j = mid + 1, k = 0;
        while (i <= mid && j <= r) {
            if(arr[i] <= arr[j]) tmp[k++] = arr[i++];
            else tmp[k++] = arr[j++];
        }
        while (i <= mid) tmp[k++] = arr[i++];
        while (j <= r) tmp[k++] = arr[j++];

        for (i = l, j = 0; i <= r; i++, j++) arr[i] = tmp[j];
    }
}
