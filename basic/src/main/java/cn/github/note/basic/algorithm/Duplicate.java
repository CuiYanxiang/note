package cn.github.note.basic.algorithm;

import cn.github.note.basic.algorithm.model.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Duplicate {
    public static void main(String[] args) {
        int[] nums = {4, 1, 3, 4, 5, 2};
        //        System.out.println(duplicate(nums));
        //        String s1 = "Hello World";
        //        for (int i=0;i<s1.length();i++){
        //            System.out.println(s1.charAt(i));
        //        }
        System.out.println(Character.isLetter('1'));
    }

    static int duplicate(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            while (nums[i] != i) {
                if (nums[i] == nums[nums[i]]) {
                    return nums[i];
                }
                swap(nums, i, nums[i]);
            }
            swap(nums, i, nums[i]);
        }
        return -1;
    }

    static void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    static List<Integer> pre(TreeNode node) {
        List<Integer> res=new ArrayList<>();
        if (node !=null) return  res;
        Stack<TreeNode> stack = new Stack<>();
        TreeNode cur = node;
        while (cur != null || !stack.isEmpty()){
            while (cur != null){
                res.add(cur.val);
                stack.push(cur);
                cur = cur.left;
            }
            cur = stack.pop();
            cur = cur.right;
        }
        return  res;
    }


    static List<Integer> mid(TreeNode node) {
        List<Integer> res=new ArrayList<>();
        if (node !=null) return  res;
        Stack<TreeNode> stack = new Stack<>();
        TreeNode cur = node;
        while (cur != null || !stack.isEmpty()){
            while (cur != null){
                stack.push(cur);
                cur = cur.left;
            }
            cur = stack.pop();
            res.add(cur.val);
            cur = cur.right;
        }
        return  res;
    }
}
