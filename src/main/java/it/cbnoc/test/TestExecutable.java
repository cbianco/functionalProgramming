package it.cbnoc.test;

import it.cbnoc.manning.function.Effect;
import it.cbnoc.manning.function.Executable;
import it.cbnoc.manning.function.Function;
import static it.cbnoc.manning.collection.CollectionUtilities.*;

import java.util.Collection;
import java.util.List;

public class TestExecutable {




	public static void main(String[] args){
		Function<Double, Double> addTax = x -> x * 1.09;
		Function<Double, Double> addShipping = x -> x + 3.50;
		List<Double> prices = list(10.10, 23.45, 32.07, 9.23);
		List<Double> pricesIncludingTax = map(prices, addTax);
		List<Double> pricesIncludingShipping =
			map(pricesIncludingTax, addShipping);
		Effect<Double> printWith2decimals = x -> {
			System.out.printf("%.2f", x);
			System.out.println();
		};

		Function<Executable, Function<Executable, Executable>> compose =
			x -> y -> () -> {
				x.exec();
				y.exec();
			};

		Executable ez = () -> {};


		Executable program = foldLeft(pricesIncludingShipping, ez,
			e -> d -> compose.apply(e).apply(() -> printWith2decimals.apply(d)));

		program.exec();

	}

	public static <T> void forEach(Collection<T> ts, Effect<T> e) {
		for (T t : ts) e.apply(t);
	}

}
