package cn.github.note.basic.algorithm.leetcode;

import java.util.HashSet;
import java.util.Set;

/**
 * 5. 最长回文子串 给你一个字符串 s，找到 s 中最长的回文子串。
 *
 * <p>示例 1： 输入：s = "babad" 输出："bab" 解释："aba" 同样是符合题意的答案。
 *
 * <p>示例 2： 输入：s = "cbbd" 输出："bb"
 *
 * <p>提示： 1 <= s.length <= 1000 s 仅由数字和英文字母组成
 */
public class LeetcodeHot5 {

    public static void main(String[] args) {}

    static String longestPalindrome(String s) {
        Set<Character> set =new HashSet<>();
        StringBuffer buffer = new StringBuffer();
        for (char c : s.toCharArray()) {
            if(set.add(c)){

            }
        }

        return buffer.toString();
    }
}
