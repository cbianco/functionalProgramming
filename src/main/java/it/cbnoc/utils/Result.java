package it.cbnoc.utils;

import it.cbnoc.function.Effect;

public abstract class Result<T> {

	public abstract void bind(Effect<T> success, Effect<String> failure);

	public static <T> Result<T> success(T value) {
		return new Success<>(value);
	}

	public static <T> Result<T> failure(String value) {
		return new Failure<>(value);
	}

	private static class Success<T> extends Result<T> {

		private final T _value;

		public Success(T value) {
			_value = value;
		}

		@Override
		public void bind(Effect<T> success, Effect<String> failure) {
			success.apply(_value);
		}
	}

	private static class Failure<T> extends Result<T> {

		private final String _errorMessage;

		public Failure(String errorMessage) {
			_errorMessage = errorMessage;
		}

		@Override
		public void bind(Effect<T> success, Effect<String> failure) {
			failure.apply(_errorMessage);
		}
	}


}
