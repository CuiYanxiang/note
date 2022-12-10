package cn.github.note.basic.algorithm.leetcode;

import cn.github.note.basic.algorithm.model.ListNode;

/**
 * 25. K 个一组翻转链表
 * <curl>https://leetcode.cn/problems/reverse-nodes-in-k-group/</curl>
 * 给你链表的头节点 head ，每k个节点一组进行翻转，请你返回修改后的链表。
 * k 是一个正整数，它的值小于或等于链表的长度。如果节点总数不是k的整数倍，那么请将最后剩余的节点保持原有顺序。
 * 你不能只是单纯的改变节点内部的值，而是需要实际进行节点交换。
 * <p>
 * 示例 1：
 * 输入：head = [1,2,3,4,5], k = 2
 * 输出：[2,1,4,3,5]
 * <p>
 * 示例 2：
 * 输入：head = [1,2,3,4,5], k = 3
 * 输出：[3,2,1,4,5]
 * <p>
 * 提示：
 * 链表中的节点数目为 n
 * 1 <= k <= n <= 5000
 * 0 <= Node.val <= 1000
 */
public class LeetcodeHot25 {

    public static void main(String[] args) {

    }

    static ListNode reverseKGroup(ListNode head, int k) {
        return new ListNode(0);
    }

}
