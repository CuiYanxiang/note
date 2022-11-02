package cn.github.note.basic.algorithm.leetcode;

import java.util.Arrays;
import java.util.HashMap;

/**
 * 1. 两数之和
 * 给定一个整数数组nums 和一个整数target，返回这两个数字的索引，使它们加起来为target。
 * 您可能会假设每个输入都只有一个解决方案，并且您可能不会两次使用相同的元素。
 * <p>
 * 示例 1：
 * 输入： nums = [2,7,11,15], target = 9
 * 输出： [0,1]
 * 解释：因为 nums[0] + nums[1] == 9，所以我们返回 [0, 1]。
 * <p>
 * 示例 2：
 * 输入： nums = [3,2,4]，目标 = 6
 * 输出： [1,2]
 * <p>
 * 示例 3：
 * 输入： nums = [3,3]，目标 = 6
 * 输出： [0,1]
 */
public class LeetcodeHot1 {

    public static void main(String[] args) {
        int[] nums = {3, 2, 4};
        int[] res = twoSum(nums, 6);
        Arrays.stream(res).forEach(System.out::println);
    }

    /**
     * 1.先遍历一次将 value 与 index 放map
     * 2.再遍历每一个值，使用 target - 当前值 = 差值
     * 3.通过差值看map内是够存在，若在则返回当前index 与 差值在map内对应的value
     * 时间复杂度 O(n)
     */
    static int[] twoSum(int[] nums, int target) {
        int[] res = new int[2];
        HashMap<Integer, Integer> map = new HashMap<>();
        int length = nums.length;
        for (int i = 0; i < length; i++) {
            map.put(nums[i], i);
        }
        for (int j = 0; j < length; j++) {
            int sub = target - nums[j];
            if (map.containsKey(sub) && map.get(sub) != j) {
                res[0] = j;
                res[1] = map.get(sub);
                return res;
            }
        }
        return res;
    }


    /**
     * 1.两重循环，两值相加是否等于target
     * 时间复杂度 O(n²)
     */
    static int[] twoSum2(int[] nums, int target) {
        int[] res = new int[2];
        int length = nums.length;
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                if (nums[i] + nums[j] == target) {
                    res[0] = i;
                    res[1] = j;
                    return res;
                }
            }
        }
        return res;
    }
}
