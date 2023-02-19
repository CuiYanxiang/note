package cn.github.note.basic.algorithm.leetcode;


import cn.github.note.basic.algorithm.model.TreeNode;
import cn.github.note.basic.utils.TreeNodeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 94. 二叉树的中序遍历
 * 给定一个二叉树的根节点 root ，返回 它的 中序 遍历 。
 * <p>
 * 示例 1：
 * 1
 * ---2
 * 3
 * 输入：root = [1,null,2,3]
 * 输出：[1,3,2]
 * <p>
 * 示例 2：
 * 输入：root = []
 * 输出：[]
 * <p>
 * 示例 3：
 * 输入：root = [1]
 * 输出：[1]
 * <p>
 * 提示：
 * 树中节点数目在范围 [0, 100] 内
 * -100 <= Node.val <= 100
 */
public class LeetcodeHot94 {

    public static void main(String[] args) {
        Integer[] arr = {1, null, 2, 3};
        TreeNode node = TreeNodeUtil.createTreeNode(arr);
        List<Integer> list = inorderTraversal(node);
        list.forEach(System.out::println);
    }

    static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        inorder(root, list);
        return list;
    }


    public static void inorder(TreeNode root, List<Integer> res) {
        if (root == null) {
            return;
        }
        inorder(root.left, res);
        res.add(root.val);
        inorder(root.right, res);
    }


}
