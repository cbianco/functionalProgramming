package it.cbnoc.function;

@FunctionalInterface
interface Function2<T, U, V> {
    V apply(T t, U u);
}
