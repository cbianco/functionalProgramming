package it.cbnoc.test;

import it.cbnoc.function.Function;

import java.util.List;

import static it.cbnoc.utils.CollectionUtilities.*;

public class TestCollection {

	public static void main(String[] args) {

		List<Integer> list = list(1,2,3,4,5,6,7);

		String identity = "0";

		System.out.println(foldLeft(list, 0, x -> y -> x - y));

		Function<String, Function<Integer, String>> f = x -> y -> addSI(x, y);

		System.out.println(foldLeft(list, identity, f));

		Function<Integer, Function<String, String>> f2 = x -> y -> addIS(x, y);

		System.out.println(foldRight(list, identity, f2));

		System.out.println("prepend: " + prepend(5, list));

		System.out.println("reverse2: " + reverse(list));

		System.out.println("map : " + mapLeft(list, a -> "ciao : " + a ));

		System.out.println("map : " + mapRight(list, a -> "ciao : " + a ));

	}

	private static String addIS(Integer i, String s) {
		return "(" + i + " + " + s + ")";
	}

	static String addSI(String s, Integer i) {
		return "(" + s + " + " + i + ")";
	}

}
