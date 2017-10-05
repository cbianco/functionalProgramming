package it.cbnoc.test;

import it.cbnoc.collection.Tree;

public class TestTree {

	public static void main(String[] args) {

		Tree<Integer> tree = Tree.empty();

		Tree<Integer> insert = tree.insert(4).insert(3).insert(11).insert(10).insert(4).insert(21).insert(6).insert(22);

		System.out.println(insert);
	}

}
