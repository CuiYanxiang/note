package cn.github.note.basic.algorithm.leetcode;


import cn.github.note.basic.algorithm.model.TreeNode;
import cn.github.note.basic.utils.TreeNodeUtil;

/**
 * 101. 对称二叉树
 * 给你一个二叉树的根节点 root ， 检查它是否轴对称。
 * <p>
 * 示例 1：
 * ---1
 * -2    2
 * 3  4  4  3
 * 输入
 * 输入：root = [1,2,2,3,4,4,3]
 * 输出：true
 * <p>
 * 示例 2：
 * --1
 * -2   2
 * --3   3
 * 输入：root = [1,2,2,null,3,null,3]
 * 输出：false
 * <p>
 * 提示：
 * 树中节点数目在范围 [1, 1000] 内
 * -100 <= Node.val <= 100
 */
public class LeetcodeHot101 {

    public static void main(String[] args) {
        Integer[] arr = {1, 2, 2, 3, 4, 4, 3};
        TreeNode node = TreeNodeUtil.createTreeNode(arr);
        System.out.println(isSymmetric(node, node));
    }

    static boolean isSymmetric(TreeNode left, TreeNode right) {
        if (left == null && right == null) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        return left.val == right.val && isSymmetric(left.left, right.right) && isSymmetric(left.right, right.left);
    }


}
