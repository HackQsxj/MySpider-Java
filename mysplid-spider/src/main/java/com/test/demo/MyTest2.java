package com.test.demo;

import java.util.ArrayList;

import org.junit.Test;

public class MyTest2 {

	public static class TreeNode {
		int val = 0;
		TreeNode left = null;
		TreeNode right = null;

		public TreeNode(int val) {
			this.val = val;

		}

	}

	private ArrayList<ArrayList<Integer>> pathList = new ArrayList<ArrayList<Integer>>();
	private ArrayList<Integer> path = new ArrayList<>();

	public ArrayList<ArrayList<Integer>> FindPath(TreeNode root, int target) {

		if (root == null) {
			return pathList;
		}
		path.add(root.val);

		if (root.left == null && root.right == null && target - root.val == 0) {
			pathList.add(new ArrayList<Integer>(path));
		}

		FindPath(root.left, target - root.val);
		FindPath(root.right, target - root.val);

		path.remove(path.size() - 1);
		return pathList;
	}

	@Test
	public void test() {
		TreeNode t1 = new TreeNode(10);
		TreeNode t2 = new TreeNode(5);
		TreeNode t3 = new TreeNode(12);
		TreeNode t4 = new TreeNode(4);
		TreeNode t5 = new TreeNode(7);
		TreeNode t6 = new TreeNode(3);
		t1.right = t2;
		t1.left = t3;
		t2.left = t4;
		t2.right = t5;
		t4.left = t6;
		ArrayList<ArrayList<Integer>> res = FindPath(t1, 22);
		for (ArrayList<Integer> path : res)
			System.out.println(path);
	}
}
