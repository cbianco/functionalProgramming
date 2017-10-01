package it.cbnoc.test;

import it.cbnoc.collection.List;
import it.cbnoc.example.Payment;
import it.cbnoc.tuple.Tuple2;
import it.cbnoc.utils.Result;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestList3 {

	public static void main(String[] args) {

		List<Integer> list = List.list();
		List<Integer> list2 = List.list();
		long l = System.currentTimeMillis();

		for(int i = 0 ; i < 10 ; i++) {
			list = list.cons(i);
			list2 = list2.cons(i);
		}

		Payment ciao = new Payment("ciao", 1);
		Payment ciao1 = new Payment("ciao2", 1);
		Payment ciao2 = new Payment("ciao2", 1);
		Payment ciao3 = new Payment("ciao", 3);
		Payment ciao4 = new Payment("ciao3", 1);
		Payment ciao5 = new Payment("ciao", 3);
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		/*List<Payment> list1 = List.list(ciao, ciao1, ciao2, ciao3, ciao4, ciao5);

		Result<List<Payment>> result =
			list1.parMap(executorService, a -> new Payment(a.name, a.amount + 1));

		System.out.println(result);

		System.out.println(List.range(0, 50).divide(6));

		List.product(List.list(1, 2, 3), List.list(4, 5, 6),
			x -> y -> new Tuple2<>(x, y));
		List<Tuple2<Integer, Integer>> x1 = List.zipWith(List.list(1, 2, 3), List.list(4, 5, 6),
			x -> y -> new Tuple2<>(x, y));
*/

		List<Integer> range = list.range(0, 1000000);

		long ms = System.currentTimeMillis();

		/*Result<List<Double>> listResult =
			range.parMap(executorService, a -> a * 1.10);*/

		List<Double> av = range.map(a -> a * 1.10);

		//System.out.println(listResult);

		System.out.println(System.currentTimeMillis() - ms);

		executorService.shutdown();

	}



}
