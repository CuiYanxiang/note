package cn.github.note.basic.algorithm;

import cn.github.note.basic.algorithm.model.TreeNode;
import cn.github.note.basic.utils.TreeNodeUtil;

import java.util.*;

/** 1 / \ 2 2 /\ /\ 3 4 4 3 */
public class BinaryTree {
    public static void main(String[] args) {
        char a = 'd';
        //        Integer[] arr = {1, 2, 2, 3, 4, 4, 3};
        //        TreeNode node = TreeNodeUtil.createTreeNode(arr);
        //        List<Integer> res = new ArrayList<>();
        //        pre(node, res);  //1, 2, 3, 4, 2, 4, 3
        //        mid(node, res);  //3, 2, 4, 1, 4, 2, 3
        //        next(node, res);  //3, 4, 2, 4, 3, 2, 1
        //        System.out.println(res);

        //        System.out.println(preIter2(node));
        //        System.out.println(midIter(node));
        //        System.out.println(nextIter(node));
        Integer[] nums = {1, 3, 4, 2, 2};


    }

    static void pre(TreeNode root, List<Integer> res) {
        if (root == null) return;
        res.add(root.val);
        pre(root.left, res);
        pre(root.right, res);
    }

    static List<Integer> preIter(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode cur = stack.pop();
            res.add(cur.val);
            if (cur.right != null) {
                stack.push(cur.right);
            }
            if (cur.left != null) {
                stack.push(cur.left);
            }
        }
        return res;
    }

    static List<Integer> preIter2(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        Stack<TreeNode> stack = new Stack<>();
        TreeNode cur = root;
        while (cur != null || !stack.isEmpty()) {
            while (cur != null) {
                res.add(cur.val);
                stack.push(cur);
                cur = cur.left;
            }
            cur = stack.pop();
            cur = cur.right;
        }

        return res;
    }

    static void mid(TreeNode root, List<Integer> res) {
        if (root == null) return;
        mid(root.left, res);
        res.add(root.val);
        mid(root.right, res);
    }

    static List<Integer> midIter(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        Stack<TreeNode> stack = new Stack<>();
        TreeNode cur = root;
        while (cur != null || !stack.isEmpty()) {
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            cur = stack.pop();
            res.add(cur.val);
            cur = cur.right;
        }
        return res;
    }

    static void next(TreeNode root, List<Integer> res) {
        if (root == null) return;
        next(root.left, res);
        next(root.right, res);
        res.add(root.val);
    }

    static List<Integer> nextIter(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        Stack<TreeNode> stack = new Stack<>();
        TreeNode cur = root;
        while (cur != null || !stack.isEmpty()) {
            while (cur != null) {
                res.add(cur.val);
                stack.push(cur);
                cur = cur.right;
            }
            cur = stack.pop();
            cur = cur.left;
        }
        Collections.reverse(res);
        return res;
    }
}
