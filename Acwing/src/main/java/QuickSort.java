public class QuickSort {
    public static void main(String[] args) {
        int[] arr = {5, 4, 4, 3, 2, 6};
        quicksort(arr, 0, arr.length - 1);
        for (int i : arr) System.out.printf("%d ", i);
    }

    public static void quicksort(int[] arr, int l, int r) {
        if(l >= r) return;
        int x = arr[(l+r)/2], i = l - 1, j = r + 1;
        while (i < j) {
            while (arr[++i] < x);
            while (arr[--j] > x);
            if(i < j) {
                int tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
            }
        }
        quicksort(arr, l, j);
        quicksort(arr, j+1, r);
    }
}
