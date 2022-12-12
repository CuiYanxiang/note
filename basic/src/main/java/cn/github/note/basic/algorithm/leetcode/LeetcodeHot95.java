package cn.github.note.basic.algorithm.leetcode;

import cn.github.note.basic.algorithm.model.TreeNode;

import java.util.Arrays;
import java.util.List;

/**
 * 95. 不同的二叉搜索树 II
 * <curl>https://leetcode.cn/problems/unique-binary-search-trees-ii/</curl>
 * 给你一个整数 n ，请你生成并返回所有由 n 个节点组成且节点值从 1 到 n 互不相同的不同 二叉搜索树 。可以按 任意顺序 返回答案。
 * <p>
 * 示例 1：
 * 输入：n = 3
 * 输出：[[1,null,2,null,3],[1,null,3,2],[2,1,3],[3,1,null,null,2],[3,2,null,1]]
 * <p>
 * 示例 2：
 * 输入：n = 1
 * 输出：[[1]]
 * <p>
 * 提示：
 * 1 <= n <= 8
 */
public class LeetcodeHot95 {

    public static void main(String[] args) {

    }

    static List<TreeNode> generateTrees(int n) {
        return Arrays.asList(new TreeNode());
    }

}
