package cn.github.note.basic.algorithm.leetcode;

import java.util.Arrays;
import java.util.List;

/**
 * 78. 子集
 * 给你一个整数数组nums ，数组中的元素 互不相同 。返回该数组所有可能的子集（幂集）。
 * 解集 不能 包含重复的子集。你可以按 任意顺序 返回解集。
 * <p>
 * 示例 1：
 * 输入：nums = [1,2,3]
 * 输出：[[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
 * <p>
 * 示例 2：
 * 输入：nums = [0]
 * 输出：[[],[0]]
 * <p>
 * 提示：
 * 1 <= nums.length <= 10
 * -10 <= nums[i] <= 10
 * nums 中的所有元素 互不相同
 */
public class LeetcodeHot78 {

    public static void main(String[] args) {

    }

    static List<List<Integer>> subsets(int[] nums) {
        return Arrays.asList(Arrays.asList(0));
    }

}
