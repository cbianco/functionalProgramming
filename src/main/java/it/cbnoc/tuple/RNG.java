package it.cbnoc.tuple;

public interface RNG  {
	Tuple2<Integer, RNG> nextInt();
}