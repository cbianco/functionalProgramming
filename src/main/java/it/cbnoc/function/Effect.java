package it.cbnoc.function;

@FunctionalInterface
public interface Effect<T> {

	void apply(T t);

}
