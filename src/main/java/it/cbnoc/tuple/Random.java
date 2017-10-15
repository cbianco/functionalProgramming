package it.cbnoc.tuple;

import it.cbnoc.function.Function;
import it.cbnoc.state.State;

public class Random<A> extends State<RNG,A> {

	public Random(Function<RNG, Tuple2<A, RNG>> run) {
		super(run);
	}
	public static State<RNG, Integer> intRnd = new Random<>(RNG::nextInt);
}
