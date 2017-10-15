package it.cbnoc.state;

import it.cbnoc.function.Function;

public interface Condition<I, S> extends Function<StateTuple<I, S>, Boolean> {}