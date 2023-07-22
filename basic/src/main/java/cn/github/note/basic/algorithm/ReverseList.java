package cn.github.note.basic.algorithm;

import cn.github.note.basic.algorithm.model.ListNode;

public class ReverseList {
    public static void main(String[] args) {
        int[] arr = {2, 4, 3};

        ListNode l1 = new ListNode(0);
        ListNode pre = l1;
        for (int i : arr) {
            ListNode node = new ListNode(i);
            pre.next = node;
            pre = node;
        }
        ListNode reverse = reverse(l1);
        System.out.println(reverse.val);
        System.out.println(reverse.next.val);
        System.out.println(reverse.next.next.val);
        System.out.println(reverse.next.next.next.val);
        System.out.println(reverse.next.next.next.next == null);
    }

    static ListNode reverse(ListNode head) {
        ListNode pre = null;
        ListNode cur = head;
        ListNode next = null;
        while (cur != null) {
            next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }
}
