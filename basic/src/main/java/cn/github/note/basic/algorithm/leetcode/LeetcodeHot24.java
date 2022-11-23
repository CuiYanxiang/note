package cn.github.note.basic.algorithm.leetcode;


import java.util.ArrayList;
import java.util.List;

/**
 * 22. 两两交换链表中的节点
 * 给你一个链表，两两交换其中相邻的节点，并返回交换后链表的头节点。你必须在不修改节点内部的值的情况下完成本题（即，只能进行节点交换）。
 * <p>
 *   1  ----->  2  ----->  3  ----->  4
 *                   ||
 *   2  ----->  1  ----->  4  ----->  3
 * <p>
 * 示例 1：
 * 输入：head = [1,2,3,4]
 * 输出：[2,1,4,3]
 * <p>
 * 示例 2：
 * 输入：head = []
 * 输出：[]
 * 示例 3：
 * 输入：head = [1]
 * 输出：[1]
 */
public class LeetcodeHot24 {

    public static void main(String[] args) {
       
    }

    static List<String> generateParenthesis(int n) {
        ArrayList<String> res = new ArrayList<>();
        if (n < 1) {
            return res;
        }
        generate(n, res, 0, 0, "");
        return res;
    }


    /**
     * 有效的  ()
     * 无效的  )(  或者 ())
     * 递归退出条件: 当左括号、右括号都用完说明left == right && left == n
     */
    static void generate(int n, List<String> res, int left, int right, String str) {
        if (left == right && left == n) {
            res.add(str);
        }
        if (left < n) {
            generate(n, res, left + 1, right, str + "(");
        }
        if (left > right) {
            generate(n, res, left, right + 1, str + ")");
        }
    }
}
