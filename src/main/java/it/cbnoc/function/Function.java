package it.cbnoc.function;

import it.cbnoc.tuple.Tuple2;

@FunctionalInterface
public interface Function<T, U> {

    U apply(T arg);

    default <V> Function<V, U> compose(Function<V, T> f) {
        return x -> apply(f.apply(x));
    }

    default <V> Function<T, V> andThen(Function<U, V> f) {
        return x -> f.apply(apply(x));
    }

    static <T> Function<T, T> identity() {
        return t -> t;
    }

    static <T, U, V> Function<V, U> compose(Function<T, U> f, Function<V, T> g) {
        return x -> f.apply(g.apply(x));
    }

    static <T, U, V> Function<T, V> andThen(Function<T, U> f, Function<U, V> g) {
        return x -> g.apply(f.apply(x));
    }

    static <T, U, V> Function<Function<T, U>, Function<Function<U, V>, Function<T, V>>> compose() {
        return x -> y -> y.compose(x);
    }

    static <T, U, V> Function<Function<T, U>, Function<Function<V, T>, Function<V, U>>> andThen() {
        return x -> y -> y.andThen(x);
    }

    static <T, U, V> Function<Function<T, U>, Function<Function<U, V>, Function<T, V>>> higherAndThen() {
        return x -> y -> z -> y.apply(x.apply(z));
    }

    static <T, U, V> Function<Function<U, V>, Function<Function<T, U>, Function<T, V>>> higherCompose() {
        return (Function<U, V> x) -> (Function<T, U> y) -> (T z) -> x.apply(y.apply(z));
    }

    static <A, B, C> Function<B,C> partialA(A a, Function<A, Function<B, C>> f) {
        return f.apply(a);
    }

    static <A, B, C> Function<A,C> partialB(B b, Function<A, Function<B, C>> f) {
        return a -> f.apply(a).apply(b);
    }

    static <A,B,C,D,E> Function<D,E> partialABC(A a, B b, C c, Function<A, Function<B,Function<C,Function<D, E>>>> f) {
        return f.apply(a).apply(b).apply(c);
    }

    static <A, B, C> Function<A, Function<B, C>> curry(Function<Tuple2<A, B>, C> f) {
        return a -> b -> f.apply(new Tuple2<>(a, b));
    }

    static <A, B, C> Function<B, Function<A, C>> reverseArgs(Function<A, Function<B, C>> f) {
        return b -> a -> f.apply(a).apply(b);
    }
}
