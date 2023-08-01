package cn.github.note.basic.algorithm.leetcode;

import java.util.HashSet;

/**
 * 3. 无重复字符的最长子串 给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。
 *
 * <p>示例 1： 输入: s = "abcabcbb" 输出: 3 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 *
 * <p>示例 2： 输入: s = "bbbbb" 输出: 1 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
 *
 * <p>示例 3： 输入: s = "pwwkew" 输出: 3 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。 请注意，你的答案必须是 子串
 * 的长度，"pwke" 是一个子序列，不是子串。
 *
 * <p>提示： 0 <= s.length <= 5 * 104 s 由英文字母、数字、符号和空格组成
 */
public class LeetcodeHot3 {

    public static void main(String[] args) {
        int res = lengthOfLongestSubstring("abcabcbb");
        System.out.println(res);
    }

    static int lengthOfLongestSubstring(String s) {
        HashSet<Character> set = new HashSet<>();
        int res = 0;
        int tmp = 0;
        for (char c : s.toCharArray()) {
            if (!set.add(c)) {
                res = Math.max(res, tmp);
                tmp = 0;
            }
            tmp += 1;
        }
        return res;
    }
}
