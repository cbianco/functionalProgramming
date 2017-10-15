package it.cbnoc.tuple;

import it.cbnoc.collection.List;

public class Generator {
	public static Tuple2<Integer, RNG> integer(RNG rng) {
		return rng.nextInt();
	}

	public static Tuple2<Integer, RNG> integer(RNG rng, int limit) {
		Tuple2<Integer, RNG> random = rng.nextInt();
		return new Tuple2<>(Math.abs(random._1 % limit), random._2);
	}

	public static Tuple2<List<Integer>, RNG> integers(RNG rng, int length) {
		Tuple2<List<Tuple2<Integer, RNG>>, RNG> result = List.range(0, length)
			.foldLeft(new Tuple2<>(List.list(), rng), tuple -> i -> {
				Tuple2<Integer, RNG> t = integer(tuple._2);
				return new Tuple2<>(tuple._1.cons(t), t._2);
			});
		List<Integer> list = result._1.map(x -> x._1);
		return new Tuple2<>(list, result._2);
	}
}