package janus.alg.easy;

/**
 * Title:Palindrome Number
 * Description:
 * Project: MyStudyDoc
 * Author: JanusMix
 * Create Time:2018/12/13 14:49
 */
public class PalindromeNumber {

    /**
     * 和反转相似，原数字逆序然后和原来的比较相等。
     * 几种别的方法：最后一位开始一位一位的往前取比较取得数和剩余数，如121，取1和12/10比较；1221，取1，得122和1，取2，得12和12，比较。
     * 转换字符串，循环一半，每一位和对应位比较
     */
    private static boolean isPalindrome(int x) {
        if (x < 0)
            return false;
        int backwards = 0;
        int origin = x;
        while (x != 0) {
            int last = x % 10;
            x /= 10;
            backwards = backwards * 10 + last;
        }
        return backwards == origin;
    }

    public static void main(String[] argus) {
        System.out.println(isPalindrome(1));
    }


}
