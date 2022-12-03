package cn.github.note.basic.algorithm.leetcode;

import cn.github.note.basic.algorithm.model.ListNode;

/**
 * 19. 删除链表的倒数第 N 个结点
 * 给你一个链表，删除链表的倒数第 n 个结点，并且返回链表的头结点。
 * <p>
 * 示例 1：
 * 1 -----> 2 -----> 3 -----> <br>4</br> -----> 5
 * |
 * 1 -----> 2 -----> 3 ----------------- -----> 5
 * 输入：head = [1,2,3,4,5], n = 2
 * 输出：[1,2,3,5]
 * <p>
 * 示例 2：
 * 输入：head = [1], n = 1
 * 输出：[]
 * <p>
 * 示例 3：
 * 输入：head = [1,2], n = 1
 * 输出：[1]
 * <p>
 * 提示：
 * 链表中结点的数目为 sz
 * 1 <= sz <= 30
 * 0 <= Node.val <= 100
 * 1 <= n <= sz
 */
public class LeetcodeHot19 {

    public static void main(String[] args) {

    }

    static ListNode removeNthFromEnd(ListNode head, int n) {
        return new ListNode(0);
    }
}
