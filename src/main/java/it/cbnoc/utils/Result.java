package it.cbnoc.utils;

import it.cbnoc.function.Effect;
import it.cbnoc.function.Function;

import java.io.Serializable;
import java.util.function.Supplier;

public abstract class Result<V> implements Serializable {

	@SuppressWarnings("rawtypes")
	private static final Result _EMPTY = new Empty();

	public abstract V getOrElse(final V defaultValue);

	public abstract V getOrElse(final Supplier<V> defaultValue);

	public abstract <U> Result<U> map(Function<V, U> f);

	public abstract <U> Result<U> flatMap(Function<V, Result<U>> f);

	public abstract Result<V> mapFailure(String message);

	public abstract Result<V> mapFailure(String s, Exception e);

	public abstract Result<V> mapFailure(Exception e);

	public abstract Result<V> mapEmptyToFailure(String message);

	public abstract void forEach(Effect<V> effect);

	public abstract void forEachOrThrow(Effect<V> effect);

	public abstract boolean isEmpty();

	public abstract Result<RuntimeException> forEachOrException(Effect<V> ef);

	public Result<V> orElse(Supplier<Result<V>> defaultValue) {
		return map(x -> this).getOrElse(defaultValue);
	}

	public Result<V> filter(Function<V, Boolean> predicate) {
		return flatMap(x -> predicate.apply(x)
				? this
				: failure("Condition not matched"));
	}

	public Result<V> filter(Function<V, Boolean> p, String message) {
		return flatMap(x -> p.apply(x)
				? this
				: failure(message));
	}

	public boolean exists(Function<V, Boolean> p) {
		return map(p).getOrElse(Boolean.FALSE);
	}

	private Result(){
	}

	private static class Empty<V> extends Result<V> {

		@Override
		public V getOrElse(V defaultValue) {
			return defaultValue;
		}

		@Override
		public V getOrElse(Supplier<V> defaultValue) {
			return defaultValue.get();
		}

		@Override
		public <U> Result<U> map(Function<V, U> f) {
			return empty();
		}

		@Override
		public <U> Result<U> flatMap(Function<V, Result<U>> f) {
			return empty();
		}

		@Override
		public Result<V> mapFailure(String message) {
			return this;
		}

		@Override
		public Result<V> mapFailure(String s, Exception e) {
			return this;
		}

		@Override
		public Result<V> mapFailure(Exception e) {
			return null;
		}

		@Override
		public Result<V> mapEmptyToFailure(String message) {
			return failure(message);
		}

		@Override
		public void forEach(Effect<V> effect) {
			// Empty. Do nothing.
		}

		@Override
		public void forEachOrThrow(Effect<V> effect) {
			// Empty. Do nothing.
		}

		@Override
		public boolean isEmpty() {
			return true;
		}

		@Override
		public Result<RuntimeException> forEachOrException(Effect<V> ef) {
			return empty();
		}

		@Override
		public String toString() {
			return "Empty()";
		}
	}

	private static class Failure<V> extends Result<V> {

		private final RuntimeException _exception;

		private Failure(RuntimeException e) {
			super();
			_exception = e;
		}

		private Failure(String message) {
			super();
			_exception = new IllegalStateException(message);
		}

		private Failure(Exception e) {
			super();
			_exception = new IllegalStateException(e.getMessage(), e);
		}

		@Override
		public String toString() {
			return String.format("Failure(%s)", _exception.getMessage());
		}

		@Override
		public V getOrElse(V defaultValue) {
			return defaultValue;
		}

		@Override
		public V getOrElse(Supplier<V> defaultValue) {
			return defaultValue.get();
		}

		@Override
		public <U> Result<U> map(Function<V, U> f) {
			return failure(_exception);
		}

		@Override
		public <U> Result<U> flatMap(Function<V, Result<U>> f) {
			return failure(_exception);
		}

		@Override
		public Result<V> mapFailure(String message) {
			return failure(new IllegalStateException(message, _exception));
		}

		@Override
		public Result<V> mapFailure(String s, Exception e) {
			return failure(new IllegalStateException(s, e));
		}

		@Override
		public Result<V> mapFailure(Exception e) {
			return failure(new IllegalStateException(_exception.getMessage(), e));
		}

		@Override
		public Result<V> mapEmptyToFailure(String message) {
			return this;
		}

		@Override
		public void forEach(Effect<V> effect) {
			// Failure. Do nothing.
		}

		@Override
		public void forEachOrThrow(Effect<V> effect) {
			throw _exception;
		}

		@Override
		public boolean isEmpty() {
			return true;
		}

		@Override
		public Result<RuntimeException> forEachOrException(Effect<V> ef) {
			return success(_exception);
		}
	}

	private static class Success<V> extends Result<V> {

		private final V _value;

		private Success(V value) {
			super();
			_value = value;
		}

		@Override
		public String toString() {
			return String.format("Success(%s)", _value);
		}

		@Override
		public V getOrElse(V defaultValue) {
			return _value;
		}

		@Override
		public V getOrElse(Supplier<V> defaultValue) {
			return _value;
		}

		@Override
		public <U> Result<U> map(Function<V, U> f) {
			try {
				return success(f.apply(_value));
			} catch (Exception e) {
				return failure(e);
			}
		}

		@Override
		public <U> Result<U> flatMap(Function<V, Result<U>> f) {
			try {
				return f.apply(_value);
			} catch (Exception e) {
				return failure(e.getMessage());
			}
		}

		@Override
		public Result<V> mapFailure(String message) {
			return this;
		}

		@Override
		public Result<V> mapFailure(String s, Exception e) {
			return this;
		}

		@Override
		public Result<V> mapFailure(Exception e) {
			return this;
		}

		@Override
		public Result<V> mapEmptyToFailure(String message) {
			return this;
		}

		@Override
		public void forEach(Effect<V> effect) {
			effect.apply(_value);
		}

		@Override
		public void forEachOrThrow(Effect<V> effect) {
			effect.apply(_value);
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public Result<RuntimeException> forEachOrException(Effect<V> ef) {
			ef.apply(_value);
			return empty();
		}
	}

	public static <S> Result<S> failure(String message) {
		return new Failure<>(message);
	}

	public static <S> Result<S> failure(Exception e) {
		return new Failure<>(e);
	}

	public static <S> Result<S> failure(RuntimeException e) {
		return new Failure<>(e);
	}

	public static <S> Result<S> success(S value) {
		return new Success<>(value);
	}

	@SuppressWarnings("unchecked")
	public static <S> Result<S> empty() {
		return _EMPTY;
	}

	public static <S> Result<S> of(S value) {
		return value != null
				? success(value)
				: failure("Null value");
	}
	public static <S> Result<S> of(S value, String message) {
		return value != null
				? success(value)
				: failure(message);
	}

	public static <T> Result<T> of(Function<T, Boolean> predicate, T value) {
		try {
			return predicate.apply(value)
					? success(value)
					: empty();
		} catch (Exception e) {
			String errMessage =
					String.format(
							"Exception while evaluating predicate: %s", value);
			return Result.failure(new IllegalStateException(errMessage, e));
		}
	}

	public static <T> Result<T> of(
			Function<T, Boolean> predicate, T value, String message) {
		try {
			return predicate.apply(value)
					? Result.success(value)
					: Result.failure(String.format(message, value));
		} catch (Exception e) {
			String errMessage =
					String.format("Exception while evaluating predicate: %s",
							String.format(message, value));
			return Result.failure(new IllegalStateException(errMessage, e));
		}
	}

	public static <A, B> Function<Result<A>, Result<B>> lift(
		final Function<A, B> f) {
		return x -> {
			try {
				return x.map(f);
			} catch (Exception e) {
				return failure(e);
			}
		};
	}

	public static <A, B, C> Function<
		Result<A>, Function<Result<B>, Result<C>>> lift2(
			Function<A, Function<B, C>> f) {
		return a -> b -> {
			try {
				return a.map(f).flatMap(b::map);
			}
			catch (Exception e) {
				return failure(e);
			}
		};
	}
	public static <A, B, C, D> Function<
		Result<A>, Function<
		Result<B>, Function<
		Result<C>, Result<D>>>> lift3(
			Function<A, Function<B, Function<C, D>>> f) {

		return a -> b -> c -> {
			try {
				return a.map(f).flatMap(b::map).flatMap(c::map);
			}
			catch (Exception e) {
				return failure(e);
			}
		};
	}

	public static <A,B,C> Result<C> map2(
		Result<A> v1, Result<B> v2, Function<A, Function<B, C>> f) {

		return lift2(f).apply(v1).apply(v2);
	}
}
