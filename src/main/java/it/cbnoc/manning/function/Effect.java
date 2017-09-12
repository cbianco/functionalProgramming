package it.cbnoc.manning.function;

@FunctionalInterface
public interface Effect<T> {

	void apply(T t);

}
