package cn.github.note.basic.algorithm.leetcode;

import java.util.Stack;

/**
 * 20. 有效的括号
 * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效。
 * 有效字符串需满足：
 * 1). 左括号必须用相同类型的右括号闭合。
 * 2). 左括号必须以正确的顺序闭合。
 * 3). 每个右括号都有一个对应的相同类型的左括号。
 *
 * <p>
 * 示例 1：
 * 输入：s = "()"
 * 输出：true
 * <p>
 * 示例 2：
 * 输入：s = "()[]{}"
 * 输出：true
 * <p>
 * 示例 3：
 * 输入：s = "(]"
 * 输出：false
 */
public class LeetcodeHot20 {

    public static void main(String[] args) {
        String c = "(()[{}]{})";
        System.out.println(isValid(c));
    }

    /**
     * 有效说明成双成对，遇到（ [ { 存进栈内，
     * 遇到] ) } 从栈内取出来对比，不一样返回false
     * 最后栈内为空，正好字符串也遍历完毕返回true
     */
    static boolean isValid(String s) {
        if (s == null | s.length() == 1) {
            return false;
        }
        Stack<Character> stack = new Stack();
        for (Character ch : s.toCharArray()) {
            if (ch == '(' || ch == '{' || ch == '[') {
                stack.push(ch);
            } else if (ch == ']' && (stack.isEmpty() || stack.pop() != '[')) {
                return false;
            } else if (ch == ')' && (stack.isEmpty() || stack.pop() != '(')) {
                return false;
            } else if (ch == '}' && (stack.isEmpty() || stack.pop() != '{')) {
                return false;
            }
        }
        return stack.isEmpty();
    }
}
