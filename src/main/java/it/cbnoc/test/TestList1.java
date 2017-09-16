package it.cbnoc.test;

import it.cbnoc.function.Function;

import static it.cbnoc.utils.CollectionUtilities.*;

public class TestList1 {

	public static void main(String[] argss){

		System.out.println(range(1,10));
		System.out.println(range(1,10));

		System.out.println(mapLeft(range(0,5), x -> x * 5));

		System.out.println(unfold("ciao", a -> a + "ciao", a -> !a.equals("ciaociaociaociao")));

		Function<Integer, Integer> add = y -> y + 1;

		Function<String, String> f1 = x -> "(a" + x + ")";
		Function<String, String> f2 = x -> "{b" + x + "}";
		Function<String, String> f3 = x -> "[c" + x + "]";

		System.out.println(composeAllViaFoldLeft(list(f1,f2,f3)).apply("X"));
		System.out.println(composeAllViaFoldRight(list(f1,f2,f3)).apply("X"));
		System.out.println(andThenAllViaFoldLeft(list(f1,f2,f3)).apply("X"));
		System.out.println(andThenAllViaFoldRight(list(f1,f2,f3)).apply("X"));

		System.out.println();

	}





}
