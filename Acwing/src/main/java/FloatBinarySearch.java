public class FloatBinarySearch {
    public static void main(String[] args) {
        // 求一个数的平方根
        int target = 2;
        double l = 0, r = target;
        while (r - l > 1e-6) {
            double mid = (l + r) / 2;
            if(mid * mid >= target) r = mid;
            else l = mid;
        }
        System.out.printf("%f\n", l*l);
    }
}
