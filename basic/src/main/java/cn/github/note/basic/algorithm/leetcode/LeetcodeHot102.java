package cn.github.note.basic.algorithm.leetcode;


import cn.github.note.basic.algorithm.model.TreeNode;

import java.util.Arrays;
import java.util.List;

/**
 * 102. 二叉树的层序遍历
 * 给你二叉树的根节点 root ，返回其节点值的 层序遍历 。 （即逐层地，从左到右访问所有节点）。
 * <p>
 * 示例 1：
 * ----3
 * 9      20
 * ----25     7
 * 输入：root = [3,9,20,null,null,15,7]
 * 输出：[[3],[9,20],[15,7]]
 * <p>
 * 示例 2：
 * 输入：root = [1]
 * 输出：[[1]]
 * <p>
 * 示例 3：
 * 输入：root = []
 * 输出：[]
 * <p>
 * 提示：
 * 树中节点数目在范围 [0, 2000] 内
 * -1000 <= Node.val <= 1000
 */
public class LeetcodeHot102 {

    public static void main(String[] args) {

    }

    static List<List<Integer>> levelOrder(TreeNode root) {
        return Arrays.asList(Arrays.asList(0));
    }


}
