public class BigCalculate {
    public static void main(String[] args) {
        String A = "123456789000000000000000000000";
        String B = "54313215645465454564654654655";
        char[] arrA = A.toCharArray();
        char[] arrB = B.toCharArray();

    }

    public static char[] add(char[] A, char[] B) {
        if(A.length < B.length) {
            char[] tmp = A;
            A = B;
            B = tmp;
        }
        char[] re = new char[A.length+1];
        int addFlag = 0;


        return re;
    }
}
