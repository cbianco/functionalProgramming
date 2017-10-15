package it.cbnoc.test;

import it.cbnoc.state.State;
import it.cbnoc.tuple.*;

import java.util.Random;

import static it.cbnoc.tuple.Random.*;
public class TestRandom {

	public static void main(String[] args) throws Exception {

		Random random = new Random();

		testInteger();
	}


	public static void testInteger() throws Exception {
		RNG rng = JavaRNG.rng(0);

		Tuple2<Integer, RNG> t1 = Generator.integer(rng);

		System.out.println(String.format("[%s, %s]", Integer.valueOf(-1155484576), t1._1));

		Tuple2<Integer, RNG> t2 = Generator.integer(t1._2);

		System.out.println(String.format("[%s, %s]",Integer.valueOf(-723955400), t2._1));

		Tuple2<Integer, RNG> t3 = Generator.integer(t2._2);

		System.out.println(String.format("[%s, %s]",Integer.valueOf(1033096058), t3._1));

		System.out.println(Random3.intRnd.apply(JavaRNG.rng(0)));

		State<RNG, Point> ns =
			intRnd.flatMap(x ->
				intRnd.flatMap(y ->
					intRnd.map(z -> new Point(x, y, z))));

		System.out.println(ns.run.apply(JavaRNG.rng(0)));

	}
}
