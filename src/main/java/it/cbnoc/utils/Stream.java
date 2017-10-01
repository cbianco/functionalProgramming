package it.cbnoc.utils;

import it.cbnoc.collection.List;
import it.cbnoc.function.Function;
import it.cbnoc.function.Supplier;
import it.cbnoc.tuple.Tuple2;

import static it.cbnoc.utils.TailCall.ret;
import static it.cbnoc.utils.TailCall.sus;

public abstract class Stream<A> {

	@SuppressWarnings("rawtypes")
	private static Empty EMPTY = new Empty();

	public abstract Tuple2<A, Stream<A>> head();
	public abstract Stream<A> tail();
	public abstract boolean isEmpty();
	public abstract Stream<A> take(int n);
	public abstract Stream<A> drop(int n);
	public abstract Stream<A> takeWhile(Function<A, Boolean> p);
	public abstract Stream<A> dropWhile(Function<A, Boolean> p);
	public abstract <B> B foldRight(
		Supplier<B> z, Function<A, Function<Supplier<B>, B>> f);

	public Result<A> find(Function<A, Boolean> p) {
		return filter(p).headOption();
	}

	public Result<A> headOption() {
		return foldRight(Result::empty, a -> ignore -> Result.success(a));
	}

	public boolean exists(Function<A, Boolean> p) {
		return exists_(this, p).eval();
	}

	public <B> Stream<B> map(Function<A, B> f) {
		return foldRight(Stream::empty, a -> b -> cons(() -> f.apply(a), b));
	}

	public Stream<A> filter(Function<A, Boolean> p) {
		Stream<A> stream = this.dropWhile(x -> !p.apply(x));
		return stream.isEmpty()
			? stream
			: cons(() -> stream.head()._1,
				() -> stream.tail().filter(p));
	}

	public <B> Stream<B> flatMap(Function<A, Stream<B>> f) {
		return foldRight(Stream::empty, a -> b -> f.apply(a).append(b));
	}

	public Stream<A> append(Supplier<Stream<A>> s) {
		return foldRight(s, a -> b -> cons(() -> a, b));
	}

	private TailCall<Boolean> exists_(Stream<A> s, Function<A, Boolean> p) {
		return s.isEmpty()
			? ret(false)
			: p.apply(s.head()._1)
				? ret(true)
				: sus(() -> exists_(s.tail(), p));
	}

	public List<A> toList(){
		return toList_(List.list(), this).eval().reverse();
	}

	private TailCall<List<A>> toList_(List<A> list, Stream<A> aStream) {
		return aStream.isEmpty()
			? ret(list)
			: sus(
				() -> toList_(list.cons(aStream.head()._1), aStream.tail()));
	}

	private Stream(){}

	public static class Empty<A> extends Stream<A> {

		@Override
		public Tuple2<A, Stream<A>> head() {
			throw new IllegalStateException("head call on empty");
		}

		@Override
		public Stream<A> tail() {
			throw new IllegalStateException("tail call on empty");
		}

		@Override
		public boolean isEmpty() {
			return true;
		}

		@Override
		public Stream<A> take(int n) {
			return this;
		}

		@Override
		public Stream<A> drop(int n) {
			return this;
		}

		@Override
		public Stream<A> takeWhile(Function<A, Boolean> p) {
			return this;
		}

		@Override
		public Stream<A> dropWhile(Function<A, Boolean> p) {
			return this;
		}

		@Override
		public <B> B foldRight(Supplier<B> z, Function<A, Function<Supplier<B>, B>> f) {
			return z.get();
		}
	}

	public static class Cons<A> extends Stream<A> {

		private final Supplier<A> _head;
		private final Result<A> h;
		private final Supplier<Stream<A>> _tail;

		private Cons(Supplier<A> h, Supplier<Stream<A>> t) {
			_head = h;
			_tail = t;
			this.h = Result.empty();
		}

		private Cons(A h, Supplier<Stream<A>> t) {
			_head = () -> h;
			_tail = t;
			this.h = Result.success(h);
		}

		@Override
		public Tuple2<A, Stream<A>> head() {
			A a = h.getOrElse(_head.get());
			return h.isEmpty()
				? new Tuple2<>(a, new Cons<>(a, _tail))
				: new Tuple2<>(a, this);
		}

		@Override
		public Stream<A> tail() {
			return _tail.get();
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public Stream<A> take(int n) {
			return n <= 0
				? empty()
				: cons(_head, () -> tail().take(n - 1));
		}

		public Stream<A> drop(int n) {
			return drop_(this, n).eval();
		}

		private TailCall<Stream<A>> drop_(Stream<A> acc, int n) {
			return n <= 0
				? ret(acc)
				: sus(() -> drop_(acc.tail(), n - 1));
		}

		@Override
		public Stream<A> takeWhile(Function<A, Boolean> p) {
			return foldRight(Stream::empty, a -> b -> p.apply(a)
				? cons(() -> a, b)
				: empty()
			);
		}

		@Override
		public Stream<A> dropWhile(Function<A, Boolean> p) {
			return dropWhile_(this, p).eval();
		}

		@Override
		public <B> B foldRight(
			Supplier<B> z, Function<A, Function<Supplier<B>, B>> f) {

			return f.apply(head()._1).apply(() -> tail().foldRight(z, f));
		}

		private TailCall<Stream<A>> dropWhile_(
			Stream<A> acc, Function<A, Boolean> p) {

			return acc.isEmpty()
				? ret(acc)
				: p.apply(acc.head()._1)
					? sus(() -> dropWhile_(acc.tail(), p))
					: ret(acc);
		}
	}

	@SuppressWarnings("unchecked")
	public static <A> Stream<A> empty() {
		return EMPTY;
	}

	public static <A> Stream<A> cons(Supplier<A> h, Supplier<Stream<A>> t) {
		return new Cons<>(h,t);
	}

	public static <A> Stream<A> cons(Supplier<A> h, Stream<A> t) {
		return new Cons<>(h, () -> t);
	}

	public static Stream<Integer> from(int i) {
		return unfold(i, x -> Result.success(new Tuple2<>(x, x + 1)));
	}

	public static <A> Stream<A> repeat(A a) {
		return iterate(a, x -> x);
	}

	public static <A> Stream<A> iterate(A seed, Function<A, A> f) {
		return cons(() -> seed, () -> iterate(f.apply(seed), f));
	}

	public static <A> Stream<A> iterate(Supplier<A> seed, Function<A, A> f) {
		return cons(seed, () -> iterate(f.apply(seed.get()), f));
	}

	public static Stream<Integer> fibs() {
		return unfold(new Tuple2<>(0, 1),
			x -> Result.success(
				new Tuple2<>(x._1, new Tuple2<>(x._2, x._1 + x._2))));
	}
	public static <A, S> Stream<A> unfold(
		S z, Function<S, Result<Tuple2<A, S>>> f) {
		return f.apply(z).map(x -> cons(
			() -> x._1, () -> unfold(x._2, f))).getOrElse(empty());
	}
}
