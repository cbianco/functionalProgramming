package it.cbnoc.test;

import it.cbnoc.collection.List;
import it.cbnoc.tuple.Tuple2;
import it.cbnoc.utils.Result;

import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelFoldLeftBenchmark {


	public static void main(String[] args) {
		int testLimit = 35000;
		int numberOfThreads = 15;

		List<Long> testList = List.unfold(0L, i -> i < 10L
			? Result.success(new Tuple2<>(i, i + 1))
			: Result.empty());
		ExecutorService es = Executors.newFixedThreadPool(numberOfThreads);

		test(5, testList, es);
		long start = System.currentTimeMillis();
		test(10, testList, es);
		System.out.println(System.currentTimeMillis() - start);
		es.shutdown();
	}

	private static void test(final int n,
							 final List<Long> list,
							 final ExecutorService es) {
		for (int i = 0; i < n; i++) {
			Result<BigInteger> result = list.parFoldLeft(es, BigInteger.ZERO, a -> b -> a.add(BigInteger.valueOf(fibo(b))), a -> a::add);
			result.forEachOrThrow(r -> System.out.println("Result:   " + r));
		}
	}

	private static long fibo(long x) {
		return x == 0
			? 0
			: x == 1
			? 1
			: fibo(x - 1) + fibo(x - 2);
	}

}