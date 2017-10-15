package it.cbnoc.tuple;

import it.cbnoc.collection.List;
import it.cbnoc.function.Function;

public interface Random3<A> extends Function<RNG, Tuple2<A, RNG>> {

	Random3<Integer> intRnd = RNG::nextInt;

	Random3<Boolean> booleanRnd = map(intRnd, x -> x % 2 == 0);

	Random3<Double> doubleRnd =
		map(intRnd, x -> x / (((double) Integer.MAX_VALUE) + 1.0));

	Random3<Tuple2<Integer, Integer>> intPairRnd =
		map2(intRnd, intRnd, x -> y -> new Tuple2<>(x, y));

	Function<Integer, Random3<List<Integer>>> integersRnd =
		length -> sequence(List.fill(length, () -> intRnd));

	Random3<Integer> notMultipleOfFiveRnd = flatMap(intRnd, x -> {
		int mod = x % 5;
		return mod != 0
			? unit(x)
			: Random3.notMultipleOfFiveRnd;
	});

	static <A> Random3<A> unit(A a) {
		return rng -> new Tuple2<>(a, rng);
	}

	static <A, B> Random3<B> map(Random3<A> s, Function<A, B> f) {
		return flatMap(s, a -> unit(f.apply(a)));
	}

	static <A, B, C> Random3<C> map2(
		Random3<A> ra, Random3<B> rb, Function<A, Function<B, C>> f) {

		return flatMap(ra, a -> map(rb, b -> f.apply(a).apply(b)));
	}

	static <A> Random3<List<A>> sequence(List<Random3<A>> rs) {
		return rs.foldLeft(unit(List.list()), acc -> r ->
			map2(r, acc, x -> y -> y.cons(x)));
	}

	static <A, B> Random3<B> flatMap(Random3<A> s, Function<A, Random3<B>> f) {
		return rng -> {
			Tuple2<A, RNG> tup = s.apply(rng);
			return f.apply(tup._1).apply(tup._2);
		};
	}

}
