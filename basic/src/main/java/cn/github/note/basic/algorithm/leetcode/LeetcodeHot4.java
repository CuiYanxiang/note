package cn.github.note.basic.algorithm.leetcode;

/**
 * 4. 寻找两个正序数组的中位数 给定两个大小分别为 m 和 n 的正序（从小到大）数组 nums1 和 nums2。请你找出并返回这两个正序数组的 中位数 。 算法的时间复杂度应该为 O(log
 * (m+n)) 。
 *
 * <p>示例 1： 输入：nums1 = [1,3], nums2 = [2] 输出：2.00000 解释：合并数组 = [1,2,3] ，中位数 2
 *
 * <p>示例 2： 输入：nums1 = [1,2], nums2 = [3,4] 输出：2.50000 解释：合并数组 = [1,2,3,4] ，中位数 (2 + 3) / 2 = 2.5
 *
 * <p>提示： nums1.length == m nums2.length == n 0 <= m <= 1000 0 <= n <= 1000 1 <= m + n <= 2000 -106
 * <= nums1[i], nums2[i] <= 106
 */
public class LeetcodeHot4 {

    public static void main(String[] args) {
        System.out.println(Math.round(4 / 2));
    }

    static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int[] arr = {};
        int i = 0;
        int j = 0;
        for (int n = 0; n < nums1.length; n++) {
        }
        int mid = (arr.length - 1) / 2;

        return 0;
    }
}
