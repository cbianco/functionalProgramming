package it.cbnoc.test;

import it.cbnoc.collection.Tree2;

public class TestTree {

	public static void main(String[] args) {

		Tree2<Integer> tree2 = Tree2.empty();

		Tree2<Integer> insert = tree2.tree(1,0,2,4,5,6,7,8,23233,-1,-3,-4);

		Tree2<Integer> a1 = Tree2.tree(1 ,2 ,3 ,4 ,5 ,6 ,7);
		Tree2<Integer> a2 = Tree2.tree(4,5,6);


		System.out.println(Tree2.log2nlz(16));
	}

}
