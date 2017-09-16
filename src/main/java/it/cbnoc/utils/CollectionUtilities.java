package it.cbnoc.utils;

import it.cbnoc.function.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static it.cbnoc.utils.TailCall.*;
import it.cbnoc.utils.TailCall;

public class CollectionUtilities {

	public static <T> List<T > list() {
		return Collections.emptyList();
	}

	public static <T> List<T> list(T t) {
		return Collections.singletonList(t);
	}

	public static <T> List<T> list(List<T> ts) {
		return Collections.unmodifiableList(new ArrayList<>(ts));
	}

	@SafeVarargs
	public static <T> List<T> list(T... t) {
		return Collections.unmodifiableList(
			Arrays.asList(Arrays.copyOf(t, t.length)));
	}

	public static <T> T head(List<T> list) {
		if (list.size() == 0) {
			throw new IllegalStateException("head of empty list");
		}
		return list.get(0);
	}

	private static <T> List<T > copy(List<T> ts) {
		return new ArrayList<>(ts);
	}

	public static <T> List<T> tail(List<T> list) {

		if (list.size() == 0) {
			throw new IllegalStateException("tail of empty list");
		}

		List<T> workList = copy(list);
		workList.remove(0);

		return Collections.unmodifiableList(workList);
	}

	public static <T> List<T> append(List<T> list, T t) {
		List<T> ts = copy(list);
		ts.add(t);
		return Collections.unmodifiableList(ts);
	}

	public static <T> List<T> prepend(T t, List<T> list) {
		return foldLeft(list, list(t), a -> b -> append(a, b));
	}


	public static <T, U> U foldLeft(
			List<T> list, U acc, Function<U, Function<T, U>> f) {
		return foldLeft_(list, acc, f).eval();

	}

	private static <T, U> TailCall<U> foldLeft_(
			List<T> list, U acc, Function<U, Function<T, U>> f){

		return list.isEmpty()
				? ret(acc)
				: sus(() -> foldLeft_(tail(list), f.apply(acc).apply(head(list)), f));
	}

	public static <T> List<T> reverse(List<T> list) {

		return foldLeft(list, list(), a -> b -> prepend(b, a));
	}

	public static <T, U> List<U> map(List<T> list, Function<T, U> f) {
		List<U> newList = new ArrayList<>();
		for (T value : list) {
			newList.add(f.apply(value));
		}
		return newList;
	}

	public static <T, U> List<U> mapLeft(List<T> list, Function<T, U> f) {
		return foldLeft(list, list(), a -> b -> append(a, f.apply(b)));
	}

	public static <T, U> List<U> mapRight(List<T> list, Function<T, U> f) {
		return foldRight(list, list(), a -> b -> prepend(f.apply(a), b));
	}

	public static <T> List<T> unfold(
		T seed, Function<T, T> f, Function<T, Boolean> p) {

		List<T> result = new ArrayList<>();

		T temp = seed;

		while (p.apply(temp)) {
			result = append(result, temp);
			temp = f.apply(temp);
		}

		return result;
	}

	public static List<Integer> range(int start, int end) {
		return range_(start, end, list()).eval();
	}

	private static TailCall<List<Integer>> range_(int start, int end, List<Integer> acc) {
		return end <= start
				? ret(acc)
				: sus(() -> range_(start + 1, end, append(acc, start)));
	}

	public static <T, U> U foldRight(
			List<T> ts, U identity, Function<T, Function<U, U>> f) {

		return foldRight_(reverse(ts), identity,f).eval();
	}

	private static <T, U> TailCall<U> foldRight_(
			List<T> ts, U identity, Function<T, Function<U, U>> f) {

		return ts.isEmpty()
				? ret(identity)
				: sus(() -> foldRight_(tail(ts), f.apply(head(ts)).apply(identity), f));

	}

	public static <T> Function<T,T> composeAllViaFoldLeft(List<Function<T,T>> list) {
		return x -> foldLeft(reverse(list), x, a -> b -> b.apply(a));
	}

	public static <T> Function<T,T> composeAllViaFoldRight(List<Function<T,T>> list) {
		return x -> foldRight(list, x, a -> a::apply);
	}

	public static <T> Function<T, T> andThenAllViaFoldLeft(List<Function<T, T>> list) {
		return x -> foldLeft(list, x, a -> b -> b.apply(a));
	}

	public static <T> Function<T, T> andThenAllViaFoldRight(List<Function<T, T>> list) {
		return x -> foldRight(reverse(list), x, a -> a::apply);
	}

}
