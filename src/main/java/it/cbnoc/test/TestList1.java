package it.cbnoc.test;

import it.cbnoc.manning.collection.CollectionUtilities;
import it.cbnoc.manning.function.Function;

import java.util.ArrayList;
import java.util.List;
import static it.cbnoc.manning.collection.CollectionUtilities.*;

public class TestList1 {

	public static void main(String[] argss){

		System.out.println(range(1,10));

		System.out.println(unfold("ciao", a -> a + "ciao", a -> !a.equals("ciaociaociaociao")));
	}

	public static List<Integer> range(int start, int end) {
		List<Integer> result = new ArrayList<>();
		int temp = start;
		while (temp < end) {
			result = CollectionUtilities.append(result, temp);
			temp = temp + 1;
		}
		return result;
	}



}
