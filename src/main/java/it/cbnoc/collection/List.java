package it.cbnoc.collection;

import it.cbnoc.function.Function;
import it.cbnoc.tuple.Tuple2;
import it.cbnoc.utils.Result;
import it.cbnoc.utils.TailCall;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import static it.cbnoc.utils.TailCall.ret;
import static it.cbnoc.utils.TailCall.sus;

public abstract class List<A> {

    public abstract A head();

    public abstract List<A> tail();

    public abstract boolean isEmpty();

    public abstract List<A> setHead(A h);

    public abstract List<A> reverse();

    public abstract List<A> init();

    public abstract int length();

    public abstract <B> B foldLeft(B identity, Function<B, Function<A, B>> f);

    public abstract <B> B foldRight(B identity, Function<A, Function<B, B>> f);

    public abstract List<A> drop(int n);

    public abstract List<A> dropWhile(Function<A, Boolean> f);

    public abstract int lengthMemoized();

    public abstract Result<A> headOption();

    public abstract <B> Tuple2<B, List<A>> foldLeft(
        B identity, B zero, Function<B, Function<A, B>> f);

    public <B> Result<List<B>> parMap(ExecutorService es, Function<A, B> g) {
        return Result.success(this.map(a -> es.submit(() -> g.apply(a))).map(x -> {
            try {
                return x.get();
            }
            catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }));
    }


    public boolean exists(Function<A, Boolean> p) {
        return foldLeft(false, true, x -> y -> x || p.apply(y))._1;
    }

    public boolean forAll(Function<A, Boolean> p) {
        return foldLeft(true, false, x -> y -> x && p.apply(y))._1;
    }

    public List<List<A>> splitListAt(int i) {
        return splitListAt_(list(), this.reverse(), i).eval();
    }

    private TailCall<List<List<A>>> splitListAt_(
        List<A> acc, List<A> list, int i) {
        return i == 0 || list.isEmpty()
            ? ret(List.list(list.reverse(), acc))
            : sus(() -> splitListAt_(acc.cons(list.head()), list.tail(), i - 1));
    }

    public List<List<A>> divide(int depth) {
        return this.isEmpty()
            ? list(this)
            : divide_(list(this), depth);
    }

    private List<List<A>> divide_(List<List<A>> list, int depth) {
        return list.head().length() < depth || depth < 2
            ? list
            : divide_(
                list.flatMap(x -> x.splitListAt(x.length() / 2)), depth / 2);
    }

    public <B> Result<B> parFoldLeft(
        ExecutorService es, B identity, Function<B, Function<A, B>> f,
        Function<B, Function<B, B>> m) {

        final int chunks = 1024;
        final List<List<A>> dList = divide(chunks);

        try {
            List<B> result =
                dList.map(x ->
                    es.submit(() -> x.foldLeft(identity, f))).map(x -> {
                try {
                    return x.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });
            return Result.success(result.foldLeft(identity, m));
        } catch (Exception e) {
            return Result.failure(e);
        }

    }

    public <B> Map<B, List<A>> groupBy(Function<A, B> f) {
        return foldRight(Map.empty(), t -> mt -> {
            final B k = f.apply(t);
            return mt.put(k, mt.get(k).getOrElse(list()).cons(t));
        });
    }

    public Result<A> getAt(int index) {
        return index < 0 || index >= length()
            ? Result.failure("Index out of bound")
            : getAt_(this, index).eval();
    }

    private static <A> TailCall<Result<A>> getAt_(List<A> list, int index) {
        return index == 0
            ? TailCall.ret(Result.success(list.head()))
            : TailCall.sus(() -> getAt_(list.tail(), index - 1));
    }


    public Tuple2<List<A>, List<A>> splitAt(int index) {

        class Tuple3<T, U, V> {
            public final T _1;
            public final U _2;
            public final V _3;

            public Tuple3(T t, U u, V v) {
                this._1 = Objects.requireNonNull(t);
                this._2 = Objects.requireNonNull(u);
                this._3 = Objects.requireNonNull(v);
            }

            @Override
            public boolean equals(Object o) {
                if (!(o.getClass() == this.getClass()))
                    return false;
                else {
                    @SuppressWarnings("rawtypes")
                    Tuple3 that = (Tuple3) o;
                    return _3.equals(that._3);
                }
            }
        }

        Tuple3<List<A>, List<A>, Integer> zero =
            new Tuple3<>(list(), list(), 0);

        Tuple3<List<A>, List<A>, Integer> identity =
            new Tuple3<>(list(), list(), index);

        Tuple2<Tuple3<List<A>, List<A>, Integer>, List<A>> rt =
            index <= 0
                ? new Tuple2<>(identity, this)
                : foldLeft(identity, zero, ta -> a -> ta._3 < 0
                    ? ta
                    : new Tuple3<>(ta._1.cons(a), ta._2, ta._3 - 1));

        return new Tuple2<>(rt._1._1.reverse(), rt._2);
    }

    private TailCall<Tuple2<List<A>,List<A>>> splitAt_(
        List<A> acc, List<A> list, int i) {

        return i == 0 || list.isEmpty()
            ? ret(new Tuple2<>(list.reverse(), acc))
            : sus(() -> splitAt_(acc.cons(list.head()), list.tail(), i - 1));
    }

    public <A1, A2> Tuple2<List<A1>, List<A2>> unzip(
        Function<A, Tuple2<A1, A2>> f) {

        return this.foldRight(new Tuple2<>(list(), list()), a -> tl -> {
            Tuple2<A1, A2> t = f.apply(a);
            return new Tuple2<>(tl._1.cons(t._1), tl._2.cons(t._2));
        });
    }

    public Result<A> lastOption() {
        return foldLeft(Result.empty(), x -> Result::success);
    }

    public List<A> cons(A a) {
        return new Cons<>(a, this);
    }

    public <B> List<B> map(Function<A, B> mapper) {
        return foldRight(list(), a -> b -> b.cons(mapper.apply(a)));
    }

    public List<A> filter(Function<A, Boolean> f) {
        return foldRight(list(), h -> t -> f.apply(h) ? new Cons<>(h,t) : t);
    }

    public <B> List<B> flatMap(Function<A, List<B>> f) {
        return foldRight(list(), a -> b -> concat(f.apply(a), b));
    }

    @SuppressWarnings("rawtypes")
    private static final List _NIL = new Nil();

    private List(){}

    private static class Nil<A> extends List<A> {

        private Nil(){}

        @Override
        public A head() {
            throw new IllegalStateException("head called en empty list");
        }

        @Override
        public List<A> tail() {
            throw new IllegalStateException("tail called en empty list");
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public List<A> setHead(A h) {
            throw new IllegalStateException("setHead called on an empty list");
        }

        @Override
        public List<A> reverse() {
            return this;
        }

        @Override
        public List<A> init() {
            throw new IllegalStateException("init called on an empty list");
        }

        @Override
        public int length() {
            return 0;
        }

        @Override
        public <B> B foldLeft(B identity, Function<B, Function<A, B>> f) {
            return identity;
        }

        @Override
        public <B> Tuple2<B, List<A>> foldLeft(
            B identity, B zero, Function<B, Function<A, B>> f) {

            return new Tuple2<>(identity, list());
        }

        @Override
        public <B> B foldRight(B identity, Function<A, Function<B, B>> f) {
            return identity;
        }

        @Override
        public List<A> drop(int n) {
            return this;
        }

        @Override
        public List<A> dropWhile(Function<A, Boolean> f) {
            return this;
        }

        @Override
        public int lengthMemoized() {
            return 0;
        }

        @Override
        public Result<A> headOption() {
            return Result.empty();
        }

        @Override
        public String toString() {
            return "[NIL]";
        }
    }

    private static class Cons<A> extends List<A> {

        private final A _head;
        private final List<A> _tail;
        private int _length;

        private Cons(A head, List<A> tail) {
            _head = head;
            _tail = tail;
           // _length = tail().length() + 1;
        }

        @Override
        public A head() {
            return _head;
        }

        @Override
        public List<A> tail() {
            return _tail;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public List<A> setHead(A h) {
            return new Cons<>(h, tail());
        }

        @Override
        public List<A> reverse() {
            return reverse_(list(), this).eval();
        }

        @Override
        public List<A> init() {
            return reverse().tail().reverse();
        }

        @Override
        public int length() {
            List<A> these = this;
            int len = 0;
            while (!these.isEmpty()) {
                len += 1;
                these = these.tail();
            }
            return len;
        }

        @Override
        public <B> B foldLeft(B identity, Function<B, Function<A, B>> f) {
            return foldLeft_(this, identity, f).eval();
        }

        @Override
        public <B> Tuple2<B, List<A>> foldLeft(
            B identity, B zero, Function<B, Function<A, B>> f) {

            return foldLeft(identity, zero, this, f).eval();
        }

        @Override
        public <B> B foldRight(B identity, Function<A, Function<B, B>> f) {
            return foldRight_(this.reverse(), identity, f).eval();
        }

        @Override
        public List<A> drop(int n) {
            return n <= 0 ? this : drop_(n, this).eval();
        }

        @Override
        public List<A> dropWhile(Function<A, Boolean> f) {
            return dropWhile_(this, f).eval();
        }

        @Override
        public int lengthMemoized() {
            return _length;
        }

        @Override
        public Result<A> headOption() {
            return Result.success(_head);
        }

        @Override
        public String toString() {
            return String.format("[%sNIL]",
                    toString(new StringBuilder(), this).eval());
        }

        private <B> TailCall<B> foldLeft_(
            List<A> list, B acc, Function<B, Function<A, B>> f) {

            return list.isEmpty()
                ? ret(acc)
                : sus(() -> foldLeft_(
                list.tail(), f.apply(acc).apply(list.head()), f));
        }

        private <B> TailCall<Tuple2<B, List<A>>> foldLeft(
            B acc, B zero, List<A> list, Function<B, Function<A, B>> f) {

            return list.isEmpty() || acc.equals(zero)
                ? ret(new Tuple2<>(acc, list))
                : sus(() -> foldLeft(
                    f.apply(acc).apply(list.head()), zero, list.tail(), f));
        }

        private <B> TailCall<B> foldRight_(
            List<A> list, B acc, Function<A, Function<B, B>> f) {

            return list.isEmpty()
                ? ret(acc)
                : sus(() -> foldRight_(
                list.tail(), f.apply(list.head()).apply(acc), f));
        }

        private TailCall<List<A>> reverse_(List<A> acc, List<A> list) {
            return list.isEmpty()
                ? ret(acc)
                : sus(() -> reverse_(acc.cons(list.head()), list.tail()));
        }

        private TailCall<List<A>> drop_(int n, List<A> acc) {
            return n <= 0 || acc.isEmpty()
                ? ret(acc) : sus(() -> drop_(n - 1, acc.tail()));
        }

        private TailCall<List<A>> dropWhile_(
            List<A> list, Function<A, Boolean> f) {

            return !list.isEmpty() && f.apply(list.head())
                ? sus(() -> dropWhile_(list.tail(), f)) : ret(list);
        }

        private TailCall<StringBuilder> toString(
            StringBuilder acc, List<A> list) {

            return list.isEmpty() ? ret(acc) : sus(() -> toString(
                    acc.append(list.head()).append(", "), list.tail()));
        }
    }

    @SuppressWarnings("unchecked")
    public static <A> List<A> list() {
        return _NIL;
    }

    @SafeVarargs
    public static <A> List<A> list(A...a) {
        List<A> n = list();
        for (int i = a.length - 1 ; i >= 0 ; i--) {
            n = new Cons<>(a[i], n);
        }
        return n;
    }

    public static <A> List<A> setHead(List<A> list, A h) {
        return list.setHead(h);
    }

    public static <A> List<A> concat(List<A> list1, List<A> list2) {
        return foldRight(list1, list2, x -> y -> new Cons<>(x, y));
    }

    public static <A, B> B foldRight(
        List<A> list, B identify, Function<A, Function<B,B>> f) {

        return list.isEmpty() ? identify
            : f.apply(list.head()).apply(foldRight(list.tail(), identify, f));
    }

    public static Integer sum(List<Integer> list) {
        return list.foldLeft(0, x -> y -> x + y);
    }

    public static Double product(List<Double> list) {
        return list.foldLeft(1.0, a -> b -> a * b);
    }

    public static <A> Integer lengthViaFoldLeft(List<A> list) {
        return list.foldLeft(0, x -> ignore -> x + 1);
    }

    public static <A> List<A> reverseViaFoldLeft(List<A> list) {
        return list.foldLeft(list(), a -> a::cons);
    }

    public static <A, B> B foldRightViaFoldLeft(
        List<A> list, B identify, Function<A, Function<B, B>> right){

        return list.reverse().foldLeft(
            identify, a -> b -> right.apply(b).apply(a));
    }

    public static <A> List<A> flatten(List<List<A>> list) {
        return list.flatMap(x -> x);
    }

    public static <A> List<A> filterViaFlatMap(
        List<A> list, Function<A, Boolean> p) {

        return list.flatMap(a -> p.apply(a) ? list(a): list());

    }

    public static <A> List<A> flattenResult(List<Result<A>> list) {
        return flatten(
            list.foldRight(list(), x -> y -> y.cons(
                x.map(List::list).getOrElse(list()))));
    }

    public static <A> Result<List<A>> sequence(List<Result<A>> list) {
        return traverse(list, Function.identity());
    }

    public static <A, B> Result<List<B>> traverse(
        List<A> list, Function<A, Result<B>> f) {

            return list.foldRight(Result.success(List.list()),
                x -> y -> Result.map2(f.apply(x), y, a -> b -> b.cons(a)));
    }

    public static <A, B, C> List<C> zipWith(List<A> list1, List<B> list2,
                                            Function<A, Function<B, C>> f) {
        return zipWith_(list(), list1, list2, f).eval().reverse();
    }
    private static <A, B, C> TailCall<List<C>> zipWith_(
        List<C> acc, List<A> list1, List<B> list2, Function<A, Function<B, C>> f) {

        return list1.isEmpty() || list2.isEmpty()
            ? ret(acc)
            : sus(() -> zipWith_(
            new Cons<>(f.apply(list1.head()).apply(list2.head()), acc),
            list1.tail(), list2.tail(), f));
    }

    public static <A, B, C> List<C> product(
        List<A> list1, List<B> list2, Function<A, Function<B, C>> f) {

        return list1.flatMap(a -> list2.map(b -> f.apply(a).apply(b)));
    }

    public static <A, B> Tuple2<List<A>, List<B>> unzip(
        List<Tuple2<A, B>> list) {

        return list.foldRight(
            new Tuple2<>(list(), list()), a -> b -> new Tuple2<>(b._1.cons(a._1), b._2.cons(a._2)));
    }

    public static <A> boolean hasSubsequence(List<A> list, List<A> sub) {
        return hasSubsequence_(list, sub).eval();
    }

    private static <A> TailCall<Boolean> hasSubsequence_(
        List<A> list, List<A> sub) {
        return list.isEmpty()
                ? ret(sub.isEmpty())
                : startsWith(list, sub)
                    ? ret(true)
                    : hasSubsequence_(list.tail(), sub);

    }

    public static <A> Boolean startsWith(List<A> list, List<A> sub) {
        return startsWith_(list, sub).eval();
    }

    public static <A> TailCall<Boolean> startsWith_(
        List<A> list, List<A> sub) {

        return sub.isEmpty()
            ? ret(Boolean.TRUE)
            : list.isEmpty()
                ? ret(Boolean.FALSE)
                : list.head().equals(sub.head())
                    ? sus(() -> startsWith_(list.tail(), sub.tail()))
                    : ret(Boolean.FALSE);
    }

    public static <A, S> List<A> unfold(
        S z, Function<S, Result<Tuple2<A, S>>> f) {

        return unfold(list(), z, f).eval().reverse();
    }

    private static <A, S> TailCall<List<A>> unfold(
        List<A> acc, S z, Function<S, Result<Tuple2<A, S>>> f) {

        Result<Tuple2<A, S>> r = f.apply(z);

        Result<TailCall<List<A>>> result =
            r.map(rt -> sus(() -> unfold(acc.cons(rt._1), rt._2, f)));

        return result.getOrElse(ret(acc));
    }

    public static List<Integer> range(int start, int end) {
        return List.unfold(start, i -> i < end
            ? Result.success(new Tuple2<>(i, i + 1))
            : Result.empty());
    }

}
