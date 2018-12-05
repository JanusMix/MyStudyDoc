package janus.alg.easy;

import java.util.*;

/**
 * Title:Two Sum
 * Description:Given an array of integers, return indices of the two numbers such that they add up to a specific target.
 * You may assume that each input would have exactly one solution, and you may not use the same element twice.
 * Project: MyStudyDoc
 * Author: JanusMix
 * Create Time:2018/12/5 10:30
 */
public class TwoSum {

    /**
     * solution ： 考虑和为目标值的两个数a和b，target - a = b，可以使用map。将每个target - a作为map的key，对于每个数组的
     * 数n，如果是map中的key，说明n + key = target。
     * Time ： O(n)
     * Space ：O(n)
     */
    private static int[] twoSum(int[] nums, int target) {

        if (nums == null || nums.length == 0)
            throw new IllegalArgumentException("Wrong array");

        Map<Integer, Integer> valueMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int diff = target - nums[i];
            if (valueMap.containsKey(nums[i]))
                return new int[]{valueMap.get(nums[i]), i};
            valueMap.put(diff, i);
        }

        throw new IllegalArgumentException("No solution");
    }

    public static void main(String[] args) {
        int[] nums = new int[]{2, 4, 5, 7};
        int target = 9;
        System.out.println(Arrays.toString(twoSum(nums, target)));
    }

}
