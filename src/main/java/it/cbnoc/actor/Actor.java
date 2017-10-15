package it.cbnoc.actor;


import com.fpinjava.common.Result;

public interface  Actor<T> {

	void tell(T message, Result<Actor<T>> sender);

	static <T> Result<Actor<T>> noSender(){
		return Result.empty();
	}

	Result<Actor<T>> self();

	ActorContext<T> getContext();

	default void tell(T message) {
		tell(message,self());
	}

	default void tell(T message, Actor<T> actor) {
		tell(message, Result.of(actor));
	}
	void shutdown();

	enum Type {SERIAL, PARALLEL}
}
