package it.cbnoc.collection;

public abstract class Tree<A extends Comparable<A>> {

	@SuppressWarnings("rawtypes")
	private static Tree EMPTY = new Empty<>() ;

	abstract Tree<A> right();

	abstract Tree<A> left();

	public abstract A value();

	public abstract Tree<A> insert(A a);

	abstract boolean member(A a);

	private static class Empty<A extends Comparable<A>> extends Tree<A> {

		@Override
		Tree<A> right() {
			throw new IllegalStateException("right call on empty");
		}

		@Override
		public A value() {
			throw new IllegalStateException("value call on empty");
		}

		@Override
		public Tree<A> insert(A insertedValue) {
			return new T<A>(empty(), insertedValue, empty());
		}

		@Override
		boolean member(A a) {
			return false;
		}

		@Override
		Tree<A> left() {
			throw new IllegalStateException("left call on empty");
		}

		@Override
		public String toString() {
			return "E";
		}
	}

	private static class T<A extends Comparable<A>> extends Tree<A> {

		private final Tree<A> left;
		private final Tree<A> right;
		private final A value;

		public T(Tree<A> left, A value, Tree<A> right) {
			this.left = left;
			this.right = right;
			this.value = value;
		}

		@Override
		Tree<A> right() {
			return right;
		}

		@Override
		public A value() {
			return value;
		}

		@Override
		public Tree<A> insert(A insertedValue) {
			return insertedValue.compareTo(this.value) < 0
				? new T<>(left.insert(insertedValue), this.value, right)
				: insertedValue.compareTo(this.value) > 0
					? new T<>(left, this.value, right.insert(insertedValue))
					: new T<>(this.left, insertedValue, this.right);
		}

		@Override
		public boolean member(A value) {
			return value.compareTo(this.value) < 0
				? left.member(value)
				: value.compareTo(this.value) == 0 || right.member(value);
		}

		@Override
		Tree<A> left() {
			return left;
		}

		@Override
		public String toString() {
			return String.format("(T %s, %s, %s)", left, value, right);
		}
	}

	public static <A extends Comparable<A>> Tree<A> empty() {
		return EMPTY;
	}
}
