package it.cbnoc.io;

import it.cbnoc.collection.List;
import it.cbnoc.function.Function;
import it.cbnoc.function.Supplier;
import it.cbnoc.state.Nothing;
import it.cbnoc.utils.Stream;

public interface IO<A> {

	A run();

	IO<Nothing> empty = () -> Nothing.instance;

	static <A> IO<A> unit(A a) {
		return () -> a;
	}

	default <B> IO<B> map(Function<A,B> f) {
		return () -> f.apply(this.run());
	}

	default <B> IO<B> flatMap(Function<A,IO<B>> f) {
		return () -> f.apply(this.run()).run();
	}

	static <A, B, C> IO<C> map2(
		IO<A> ioa, IO<B> iob, Function<A, Function<B, C>> f) {
		return ioa.flatMap(a -> iob.map(b -> f.apply(a).apply(b)));
	}

	static <A> IO<List<A>> repeat(int n, IO<A> io) {
		return Stream.fill(n, () -> io)
			.foldRight(() -> unit(List.list()), ioa -> sioLa -> map2(ioa,
				sioLa.get(), a -> la -> List.cons(a, la)));
	}

	static <A, B> IO<B> forever(IO<A> ioa) {
		Supplier<IO<B>> t = () -> forever(ioa);
		return ioa.flatMap(x -> t.get());
	}

}


