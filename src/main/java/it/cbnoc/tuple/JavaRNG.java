package it.cbnoc.tuple;

import java.util.Random;

public class JavaRNG implements RNG {

	private final Random random;

	private JavaRNG(long seed) {
		this.random = new Random(seed);
	}

	@Override
	public Tuple2<Integer, RNG> nextInt() {
		return new Tuple2<>(random.nextInt(), this);
	}

	public static RNG rng(long seed) {
		return new JavaRNG(seed);
	}
}