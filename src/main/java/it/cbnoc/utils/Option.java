package it.cbnoc.utils;

import it.cbnoc.collection.List;
import it.cbnoc.function.Function;
import it.cbnoc.function.Supplier;

import java.util.Objects;

public abstract class Option<A> {

    @SuppressWarnings("rawtypes")
    private static Option none = new None();

    public abstract A getOrThrow();

    public abstract A getOrElse(A defaultValue);

    public abstract A getOrElse(Supplier<A> defaultValue);

    public abstract <B> Option<B> map(Function<A, B> mapper);

    public <B> Option<B> flatMap(Function<A, Option<B>> mapper) {
        return map(mapper).getOrElse(none());
    }

    public Option<A> orElse(Supplier<Option<A>> defaultValue) {
        return map(x -> this).getOrElse(defaultValue);
    }

    public Option<A> filter(Function<A, Boolean> f) {
        return flatMap(a -> f.apply(a) ? this : none());
    }

    private static class None<A> extends Option<A> {

        @Override
        public A getOrThrow() {
            throw new IllegalStateException("get called on None");
        }

        @Override
        public A getOrElse(A defaultValue) {
            return defaultValue;
        }

        @Override
        public A getOrElse(Supplier<A> defaultValue) {
            return defaultValue.get();
        }

        @Override
        public <B> Option<B> map(Function<A, B> mapper) {
            return none();
        }

        @Override
        public String toString() {
            return "None";
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof None;
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

    private static class Some<A> extends Option<A> {

        private final A _value;

        private Some(A value) {
            _value = value;
        }

        @Override
        public A getOrThrow() {
            return this._value;
        }

        @Override
        public A getOrElse(A defaultValue) {
            return this._value;
        }

        @Override
        public A getOrElse(Supplier<A> defaultValue) {
            return this._value;
        }

        @Override
        public <B> Option<B> map(Function<A, B> mapper) {
            return new Some<>(mapper.apply(this._value));
        }

        @Override
        public String toString() {
            return String.format("Some(%s)",this._value);
        }

        @Override
        public boolean equals(Object o) {
            return (this == o || o instanceof Some)
                    && this._value.equals(((Some<?>) o)._value);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(_value);
        }
    }

    public static <A> Option<A> some(A value) {
        return new Some<>(value);
    }

    @SuppressWarnings("unchecked")
    public static <A> Option<A> none() {
        return none;
    }

    public static <A, B> Function<Option<A>, Option<B>> lift(Function<A, B> f) {
        return x -> {
            try {
                return x.map(f);
            } catch (Exception e) {
                return Option.none();
            }
        };
    }

    public static <A, B> Function<A, Option<B>> hlift(Function<A, B> f) {
        return x -> {
            try {
                return Option.some(x).map(f);
            } catch (Exception e) {
                return Option.none();
            }
        };
    }

    public static <A,B,C> Option<C> map2(Option<A> aopt, Option<B> bopt, Function<A, Function<B, C>> f){
        return aopt.flatMap(a -> bopt.map(b -> f.apply(a).apply(b)));
    }

    public static <A, B, C, D> Option<D> map3(Option<A> a, Option<B> b, Option<C> c, Function<A, Function<B, Function<C, D>>> f) {
        return a.flatMap(ax -> b.flatMap(bx -> c.map(cx -> f.apply(ax).apply(bx).apply(cx))));
    }

    public static <A> Option<List<A>> sequence(List<Option<A>> list) {
        return traverse(list, x -> x);
    }

    public static <A, B> Option<List<B>> traverse(List<A> list, Function<A, Option<B>> f) {
        return list.foldRight(some(List.list()), x -> y -> map2(f.apply(x), y, a -> b -> b.cons(a)));
    }
}
