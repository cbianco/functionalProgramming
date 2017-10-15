package it.cbnoc.collection;

import it.cbnoc.function.Function;
import it.cbnoc.tuple.Tuple2;
import it.cbnoc.utils.Result;
import it.cbnoc.utils.TailCall;


public abstract class Tree2<A extends Comparable<A>> {

	@SuppressWarnings("rawtypes")
	private static Tree2 EMPTY = new Empty();

	abstract Tree2<A> left();
	abstract Tree2<A> right();

	public abstract A value();
	public abstract Tree2<A> insert(A a);
	public abstract boolean member(A a);
	public abstract int size();
	public abstract int height();
	public abstract Result<A> max();
	public abstract Result<A> min();
	public abstract Tree2<A> remove(A a);
	public abstract boolean isEmpty();
	public abstract Tree2<A> merge(Tree2<A> a);
	public abstract Tree2<A> merge(A a, Tree2<A> right);

	/**
	 * Merges two subtrees with the particularity that all elements of one
	 * are either greater or smaller than all elements of the other.
	 *
	 * This is an optimized merge for removal of the value, when we need to merge
	 * the remaining right and left tree.
	 */
	protected abstract Tree2<A> removeMerge(Tree2<A> ta);

	public abstract <B> B foldLeft(B identity, Function<B, Function<A, B>> f, Function<B, Function<B, B>> g);

	public abstract <B> B foldRight(B identity, Function<A, Function<B, B>> f, Function<B, Function<B, B>> g);

	public abstract <B> B foldInOrder(B identity, Function<B, Function<A, Function<B, B>>> f);

	public abstract <B> B foldPreOrder(B identity, Function<A, Function<B, Function<B, B>>> f);

	public abstract <B> B foldPostOrder(B identity, Function<B, Function<B, Function<A, B>>> f);

	protected abstract Tree2<A> rotateLeft();

	protected abstract Tree2<A> rotateRight();

	public abstract List<A> toListInOrderRight();

	protected abstract Tree2<A> ins(A a);

	public <B extends Comparable<B>> Tree2<B> map(Function<A, B> f) {
		return foldInOrder(empty(), t1 -> i -> t2 -> Tree2.tree(t1, f.apply(i), t2));
	}

	private static class Empty<A extends Comparable<A>> extends Tree2<A> {

		@Override
		public A value() {
			throw new IllegalStateException("value() called on empty");
		}

		@Override
		Tree2<A> left() {
			throw new IllegalStateException("left() called on empty");
		}

		@Override
		Tree2<A> right() {
			throw new IllegalStateException("right() called on empty");
		}

		@Override
		public Tree2<A> insert(A value) {
			return new T<>(empty(), value, empty());
		}

		@Override
		public boolean member(A a) {
			return false;
		}

		@Override
		public int size() {
			return 0;
		}

		@Override
		public int height() {
			return -1;
		}

		@Override
		public Result<A> max() {
			return Result.empty();
		}

		@Override
		public Result<A> min() {
			return Result.empty();
		}

		@Override
		public Tree2<A> remove(A a) {
			return this;
		}

		@Override
		public boolean isEmpty() {
			return true;
		}

		@Override
		public Tree2<A> merge(Tree2<A> a) {
			return a;
		}

		@Override
		public Tree2<A> merge(A a, Tree2<A> right) {
			return right.min().map(min -> a.compareTo(min) < 0
				? new T<>(empty(), a, right)
				: right.insert(a)).getOrElse(this.insert(a));
		}

		@Override
		protected Tree2<A> removeMerge(Tree2<A> ta) {
			return ta;
		}

		@Override
		public <B> B foldLeft(B identity, Function<B, Function<A, B>> f, Function<B, Function<B, B>> g) {
			return identity;
		}

		@Override
		public <B> B foldRight(B identity, Function<A, Function<B, B>> f, Function<B, Function<B, B>> g) {
			return identity;
		}

		@Override
		public <B> B foldInOrder(B identity, Function<B, Function<A, Function<B, B>>> f) {
			return identity;
		}

		@Override
		public <B> B foldPreOrder(B identity, Function<A, Function<B, Function<B, B>>> f) {
			return identity;
		}

		@Override
		public <B> B foldPostOrder(B identity, Function<B, Function<B, Function<A, B>>> f) {
			return identity;
		}

		@Override
		protected Tree2<A> rotateLeft() {
			return this;
		}

		@Override
		protected Tree2<A> rotateRight() {
			return this;
		}

		@Override
		public List<A> toListInOrderRight() {
			return List.list();
		}

		@Override
		protected Tree2<A> ins(A a) {
			return insert(a);
		}

		@Override
		public String toString() {
			return "E";
		}
	}

	private static class T<A extends Comparable<A>> extends Tree2<A> {

		private final Tree2<A> left;
		private final Tree2<A> right;
		private final A value;
		private final int height;
		private final int size;

		private T(Tree2<A> left, A value, Tree2<A> right) {
			this.left = left;
			this.right = right;
			this.value = value;
			this.height = 1 + Math.max(left.height(), right.height());
			this.size = 1 + left.size() + right.size();
		}

		@Override
		public A value() {
			return value;
		}

		@Override
		Tree2<A> left() {
			return left;
		}

		@Override
		Tree2<A> right() {
			return right;
		}

		@Override
		public Tree2<A> insert(A a) {
			Tree2<A> t = ins(a);
			return t.height() > log2nlz(t.size()) * 20 ? balance(t) : t;
		}

		@Override
		public boolean member(A value) {
			return value.compareTo(this.value) < 0
				? left.member(value)
				: value.compareTo(this.value) <= 0 || right.member(value);
		}

		@Override
		public int size() {
			return size;
		}

		@Override
		public int height() {
			return height;
		}

		@Override
		public Result<A> max() {
			return right.max().orElse(() -> Result.success(value));
		}

		@Override
		public Result<A> min() {
			return left.min().orElse(() -> Result.success(value));
		}

		@Override
		public Tree2<A> remove(A a) {
			if (a.compareTo(this.value) < 0) {
				return new T<>(left.remove(a), value, right);
			} else if (a.compareTo(this.value) > 0) {
				return new T<>(left, value, right.remove(a));
			} else {
				return left.removeMerge(right);
			}
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public Tree2<A> merge(Tree2<A> a) {
			if (a.isEmpty()) {
				return this;
			}
			if (a.value().compareTo(this.value) > 0) {
				return new T<>( left, value, right.merge(new T<>(empty(), a.value(), a.right()))).merge(a.left());
			}
			if (a.value().compareTo(this.value) < 0) {
				return new T<>( left.merge(new T<>(a.left(), a.value(), empty())), value, right).merge(a.right());
			}
			return new T<>(left.merge(a.left()), value, right.merge(a.right()));
		}

		@Override
		public Tree2<A> merge(A a, Tree2<A> right) {
			return right.isEmpty()
				? max().map(max -> max.compareTo(a) > 0 // Default value can never be used, but is necessary
				? insert(a)
				: new T<>(this, a, right)).getOrElse(this)
				: max().flatMap(lmax -> right.min().map(rmin -> a.compareTo(lmax) > 0 && a.compareTo(rmin) < 0 ? new T<>(this, a, right) : merge(right).insert(a))).getOrElse(right);
		}

		protected Tree2<A> removeMerge(Tree2<A> ta) {
			if (ta.isEmpty()) {
				return this;
			}
			if (ta.value().compareTo(value) < 0) {
				return new T<>(left.removeMerge(ta), value, right);
			} else if (ta.value().compareTo(value) > 0) {
				return new T<>(left, value, right.removeMerge(ta));
			}
			throw new IllegalStateException("Shouldn't be merging two subtrees with the same value");
		}

		@Override
		public <B> B foldLeft(B identity, Function<B, Function<A, B>> f, Function<B, Function<B, B>> g) {
			// Post order right:
			return g.apply(right.foldLeft(identity, f, g)).apply(f.apply(left.foldLeft(identity, f, g)).apply(this.value));
		}

		@Override
		public <B> B foldRight(B identity, Function<A, Function<B, B>> f, Function<B, Function<B, B>> g) {
			// Pre order left
			return g.apply(f.apply(this.value).apply(left.foldRight(identity, f, g))).apply(right.foldRight(identity, f, g));
		}

		@Override
		public <B> B foldInOrder(B identity, Function<B, Function<A, Function<B, B>>> f) {
			return f.apply(left.foldInOrder(identity, f)).apply(value).apply(right.foldInOrder(identity, f));
		}

		@Override
		public <B> B foldPreOrder(B identity, Function<A, Function<B, Function<B, B>>> f) {
			return f.apply(value).apply(left.foldPreOrder(identity, f)).apply(right.foldPreOrder(identity, f));
		}

		@Override
		public <B> B foldPostOrder(B identity, Function<B, Function<B, Function<A, B>>> f) {
			return f.apply(left.foldPostOrder(identity, f)).apply(right.foldPostOrder(identity, f)).apply(value);
		}

		@Override
		protected Tree2<A> rotateLeft() {
			return right.isEmpty()
				? this
				: new T<>(new T<>(left, value, right.left()),
				right.value(), right.right());
		}

		@Override
		protected Tree2<A> rotateRight() {
			return left.isEmpty()
				? this
				: new T<>(left.left(), left.value(),
					new T<>(left.right(), value, right));
		}

		@Override
		public List<A> toListInOrderRight() {
			return unBalanceRight(List.list(), this).eval();
		}

		@Override
		protected Tree2<A> ins(A a) {
			return a.compareTo(this.value) < 0
				? new T<>(left.ins(a), this.value, right)
				: a.compareTo(this.value) > 0
					? new T<>(left, this.value, right.ins(a))
					: new T<>(this.left, value, this.right);
		}

		private TailCall<List<A>> unBalanceRight(List<A> acc, Tree2<A> tree2) {
			return tree2.isEmpty()
				? TailCall.ret(acc)
				: tree2.left().isEmpty()
					? TailCall.sus(() ->
				unBalanceRight(acc.cons(tree2.value()), tree2.right()))
					: TailCall.sus(() ->
				unBalanceRight(acc, tree2.rotateRight()));
		}

		@Override
		public String toString() {
			return String.format("(T %s %s %s)", left, value, right);
		}
	}

	@SuppressWarnings("unchecked")
	public static <A extends Comparable<A>> Tree2<A> empty() {
		return EMPTY;
	}

	public static <A extends Comparable<A>> Tree2<A> tree(List<A> list) {
		return list.foldLeft(empty(), t -> t::insert);
	}

	@SafeVarargs
	public static <A extends Comparable<A>> Tree2<A> tree(A... as) {
		return tree(List.list(as));
	}

	public static <A extends Comparable<A>> boolean lt(A first, A second) {
		return first.compareTo(second) < 0;
	}

	public static <A extends Comparable<A>> boolean lt(A first, A second, A third) {
		return lt(first, second) && lt(second, third);
	}

	public static <A extends Comparable<A>> Tree2<A> tree(Tree2<A> t1, A a, Tree2<A> t2) {
		return ordered(t1, a, t2)
			? new T<>(t1, a, t2)
			: ordered(t2, a, t1)
			? new T<>(t2, a, t1)
			: Tree2.<A>empty().insert(a).merge(t1).merge(t2);
	}

	public static <A extends Comparable<A>> boolean ordered(Tree2<A> left, A a, Tree2<A> right) {
		return left.max().flatMap(lMax -> right.min().map(rMin -> lt(lMax, a, rMin))).getOrElse(left.isEmpty() && right.isEmpty())
			|| left.min().flatMap(ignore -> right.min().map(rMin -> lt(a, rMin))).getOrElse(false)
			|| right.min().flatMap(ignore -> left.max().map(lMax -> lt(lMax, a))).getOrElse(false);
	}

	public static int log2nlz(int n) {
		return n == 0
			? 0
			: 31 - Integer.numberOfLeadingZeros(n);
	}

	public static <A extends Comparable<A>> Tree2<A> balance(Tree2<A> tree2) {
		return balance_(tree2.toListInOrderRight().foldLeft(Tree2.<A>empty(),
			t -> a -> new T<>(empty(), a, t)));
	}

	public static <A extends Comparable<A>> Tree2<A> balance_(Tree2<A> tree2) {
		return !tree2.isEmpty() && tree2.height() > log2nlz(tree2.size())
			? Math.abs(tree2.left().height() - tree2.right().height()) > 1
			? balance_(balanceFirstLevel(tree2))
			: new T<>(balance_(tree2.left()), tree2.value(),
			balance_(tree2.right()))
			: tree2;
	}

	private static <A extends Comparable<A>> Tree2<A>
		balanceFirstLevel(Tree2<A> tree2) {
			return unfold(tree2, t -> isUnBalanced(t)
				? tree2.right().height() > tree2.left().height()
				? Result.success(t.rotateLeft())
				: Result.success(t.rotateRight())
				: Result.empty());
	}

	static <A extends Comparable<A>> boolean isUnBalanced(Tree2<A> tree2) {
		return Math.abs(tree2.left().height() - tree2.right().height())
			> (tree2.size() - 1) % 2;
	}

	public static <A> A unfold(A a, Function<A, Result<A>> f) {
		Result<A> ra = Result.success(a);
		return unfold(new Tuple2<>(ra, ra), f).eval()._2.getOrElse(a);
	}

	private static <A> TailCall<Tuple2<Result<A>, Result<A>>> unfold(Tuple2<Result<A>,
		Result<A>> a, Function<A, Result<A>> f) {
		Result<A> x = a._2.flatMap(f::apply);
		return x.exists(y -> Boolean.TRUE)
			? TailCall.sus(() -> unfold(new Tuple2<>(a._2, x), f))
			: TailCall.ret(a);
	}
}


