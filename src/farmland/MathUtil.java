package farmland;

public final class MathUtil {

    private MathUtil() {
    }

    /**
     * Returns greatest common divisor using Euclidean algorithm.
     */
    public static int gcd(int a, int b) {

        a = Math.abs(a);
        b = Math.abs(b);

        if (a == 0) return b;
        if (b == 0) return a;

        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }

        return a;
    }
}