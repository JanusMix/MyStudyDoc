package janus.alg.easy;

/**
 * Title:Reverse Integer
 * Description:Given a 32-bit signed integer, reverse digits of an integer.
 * Project: MyStudyDoc
 * Author: JanusMix
 * Create Time:2018/12/5 11:15
 */
public class ReverseInteger {

    private static int reverse(int x) {
        int result = 0;
        while (x != 0) {
            int last = x % 10;
            x /= 10;
            //超出2147483647
            if (result > Integer.MAX_VALUE / 10 || (result == Integer.MAX_VALUE / 10 && last > 7))
                return 0;
            //小于-2147483648
            if (result < Integer.MIN_VALUE / 10 || (result == Integer.MIN_VALUE / 10 && last < -8))
                return 0;
            result = result * 10 + last;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(reverse(212235));
    }

}
