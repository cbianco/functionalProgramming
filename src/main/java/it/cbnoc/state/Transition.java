package it.cbnoc.state;

import it.cbnoc.function.Function;

public interface Transition<A, S> extends Function<StateTuple<A, S>, S> {}