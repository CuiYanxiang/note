package cn.github.note.basic.algorithm.leetcode;


import java.util.Arrays;

/**
 * 2. 两数相加
 * 给你两个 非空 的链表，表示两个非负的整数。它们每位数字都是按照 逆序 的方式存储的，并且每个节点只能存储 一位 数字。
 * 请你将两个数相加，并以相同形式返回一个表示和的链表。
 * 你可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 * <p>
 * 2  ----->  4  ----->  3
 * 5  ----->  6  ----->  4
 * -----------------------
 * 7  ----->  0  ----->  8
 * <p>
 * 示例 1：
 * 输入：l1 = [2,4,3], l2 = [5,6,4]
 * 输出：[7,0,8]
 * 解释：342 + 465 = 807.
 * <p>
 * 示例 2：
 * 输入：l1 = [0], l2 = [0]
 * 输出：[0]
 * <p>
 * 示例 3：
 * 输入：l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
 * 输出：[8,9,9,9,0,0,0,1]
 */
public class LeetcodeHot2 {

    public static void main(String[] args) {
        int[] arr1 = {2, 4, 3};
        int[] arr2 = {5, 6, 4};

        ListNode l1 = new ListNode(0);
        ListNode pre = l1;
        for (int i : arr1) {
            ListNode node = new ListNode(i);
            pre.next = node;
            pre = node;
        }

        ListNode l2 = new ListNode(0);
        ListNode pre2 = l2;
        for (int i : arr2) {
            ListNode node = new ListNode(i);
            pre2.next = node;
            pre2 = node;
        }

        ListNode addTwoNumbers = addTwoNumbers(l1.next, l2.next);

        System.out.println(addTwoNumbers.val);
        System.out.println(addTwoNumbers.next.val);
        System.out.println(addTwoNumbers.next.next.val);

    }

    static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode res = new ListNode(0);
        int sum = 0; // 结果
        int tmp = 0; // 进位
        ListNode pre = res;
        while (l1 != null || l2 != null || tmp != 0) {
            int l1Val = l1 == null ? 0 : l1.val;
            int l2Val = l2 == null ? 0 : l2.val;

            sum = l1Val + l2Val + tmp;
            tmp = sum / 10;  // 取进位数 1

            ListNode node = new ListNode(sum % 10); // 去结果

            pre.next = node;
            pre = node;

            l1 = l1 == null ? null : l1.next;
            l2 = l2 == null ? null : l2.next;
        }
        return res.next;
    }
}

class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        this.val = x;
    }

}
