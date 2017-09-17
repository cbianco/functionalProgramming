package it.cbnoc.collection;

import it.cbnoc.function.Function;
import it.cbnoc.utils.TailCall;

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
            throw new IllegalStateException("head called en empty list");
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
        public String toString() {
            return "[NIL]";
        }
    }

    private static class Cons<A> extends List<A> {

        private final A _head;
        private final List<A> _tail;

        private Cons(A head, List<A> tail) {
            _head = head;
            _tail = tail;
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
            return foldLeft(0, a -> b -> a + 1);
        }

        @Override
        public <B> B foldLeft(B identity, Function<B, Function<A, B>> f) {
            return foldLeft_(this, identity, f).eval();
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
}
