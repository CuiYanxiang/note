package cn.github.note.basic.algorithm.leetcode;


import java.util.ArrayList;
import java.util.List;

/**
 * 22. 括号生成
 * 数字 n 代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且 有效的 括号组合。
 *
 * <p>
 * 示例 1：
 * 输入：n = 3
 * 输出：["((()))","(()())","(())()","()(())","()()()"]
 * <p>
 * 示例 2：
 * 输入：n = 1
 * 输出：["()"]
 */
public class LeetcodeHot22 {

    public static void main(String[] args) {
        List<String> stringList = generateParenthesis(3);
        stringList.forEach(System.out::println);
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
