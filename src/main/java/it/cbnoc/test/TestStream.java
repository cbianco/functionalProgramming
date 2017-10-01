package it.cbnoc.test;

import it.cbnoc.collection.List;
import it.cbnoc.function.Function;
import it.cbnoc.tuple.Tuple2;
import it.cbnoc.utils.Result;
import it.cbnoc.utils.Stream;

public class TestStream {

	/*private static Function<Integer, Integer> f = x -> {
		System.out.println("Mapping " + x);
		return x * 3;
	};

	private static Function<Integer, Boolean> p = x -> {
		System.out.println("Filtering " + x);
		return x % 2 == 0;
	};

	public static void mmain(String... args) {
		List<Integer> list = List.list(1, 2, 3, 4, 5).map(f).filter(p);
		System.out.println(list);
	}*/

	private static Stream<Integer> stream =
		Stream.cons(() -> 1,
			Stream.cons(() -> 2,
				Stream.cons(() -> 3,
					Stream.cons(() -> 4,
						Stream.cons(() -> 5, Stream.<Integer>empty())))));

	private static Function<Integer, Integer> f = x -> {
		System.out.println("Mapping " + x);
		return x * 3;
	};

	private static Function<Integer, Boolean> p = x -> {
		System.out.println("Filtering " + x);
		return x % 2 == 0;
	};

	private static Function<Integer, Boolean> find = x -> {
		System.out.println("Find " + x);
		return x == 12;
	};

	public static void main(String...args) {
	}

}
