package it.cbnoc.state;

import it.cbnoc.collection.List;
import it.cbnoc.function.Function;
import it.cbnoc.tuple.Tuple2;
import it.cbnoc.utils.Result;

public class StateMachine<A, S> {

	Function<A, State<S, Nothing>> function;

	public StateMachine(List<Tuple2<Condition<A, S>,
		Transition<A, S>>> transitions) {

		function = a -> State.sequence(m ->
			Result.success(new StateTuple<>(a, m)).flatMap((StateTuple<A, S> t) ->
				transitions.filter((Tuple2<Condition<A, S>, Transition<A, S>> x) ->
					x._1.apply(t)).headOption().map((Tuple2<Condition<A, S>,
					Transition<A, S>> y) -> y._2.apply(t))).getOrElse(m));
	}

	public State<S, S> process(List<A> inputs) {
		List<State<S, Nothing>> a = inputs.map(function);
		State<S, List<Nothing>> b = State.compose(a);
		return b.flatMap(x -> State.get());
	}

}
