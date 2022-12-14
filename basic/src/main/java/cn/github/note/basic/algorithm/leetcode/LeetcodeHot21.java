package cn.github.note.basic.algorithm.leetcode;


import cn.github.note.basic.algorithm.model.ListNode;

/**
 * 21. 合并两个有序链表
 * 将两个升序链表合并为一个新的 升序 链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。
 * <p>
 * 1  ----->  2  ----->  4
 * 1  ----->  2  ----->  4
 * -----------------------
 * 1 ---> 1 ---> 2 ---> 3 ---> 4 ---> 4
 *
 * <p>
 * 示例 1：
 * 输入：l1 = [1,2,4], l2 = [1,3,4]
 * 输出：[1,1,2,3,4,4]
 * <p>
 * 示例 2：
 * 输入：l1 = [], l2 = []
 * 输出：[]
 * <p>
 * 示例 3：
 * 输入：l1 = [], l2 = [0]
 * 输出：[0]
 */
public class LeetcodeHot21 {

    public static void main(String[] args) {
        int[] arr1 = {1, 2, 4};
        int[] arr2 = {1, 3, 4};

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

        ListNode mergeTwoLists = mergeTwoLists(l1.next, l2.next);

        System.out.println(mergeTwoLists.val);
        System.out.println(mergeTwoLists.next.val);
        System.out.println(mergeTwoLists.next.next.val);
        System.out.println(mergeTwoLists.next.next.next.val);
        System.out.println(mergeTwoLists.next.next.next.next.val);
        System.out.println(mergeTwoLists.next.next.next.next.next.val);
    }


    /**
     * 迭代法
     * 链表val两个对比，哪个值小放pre.next,
     * 当某个listNode为null,跳出while,将另一个listNade放pre.next
     */
    static ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        if (list1 == null && list2 == null) {
            return new ListNode(0);
        }
        ListNode res = new ListNode(0);
        ListNode pre = res;
        while (list1 != null && list2 != null) {
            if (list1.val < list2.val) {
                pre.next = list1;
                list1 = list1.next;
            } else {
                pre.next = list2;
                list2 = list2.next;
            }
            pre = pre.next;
        }
        pre.next = list1 == null ? list2 : list1;
        return res.next;
    }

    /**
     * 递归法
     */
    static ListNode mergeTwoLists2(ListNode l1, ListNode l2) {
        if (l1 == null || l2 == null) {
            return l1 == null ? l2 : l1;
        }
        if (l1.val <= l2.val) {
            l1.next = mergeTwoLists2(l1.next, l2);
            return l1;
        } else {
            l2.next = mergeTwoLists2(l1, l2.next);
            return l2;
        }
    }
}
