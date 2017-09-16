package it.cbnoc.test;

import it.cbnoc.function.Function;
import it.cbnoc.tuple.Tuple3;
import it.cbnoc.utils.Memoizer;

public class TestMemoizer {

    private static Integer longCalculation(Integer x) {
        try{
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
        }
        return x * 2;
    }

    static Function<Integer, Function<Integer, Function<Integer, Integer>>> f3m =
            Memoizer.memoize(x ->
                    Memoizer.memoize(y ->
                            Memoizer.memoize(z ->
                                    longCalculation(x) + longCalculation(y) - longCalculation(z))));

    public static void automaticMemoizationExample2() {
        long startTime = System.currentTimeMillis();
        Integer result1 = f3m.apply(2).apply(5).apply(4);
        long time1 = System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();
        Integer result2 = f3m.apply(2).apply(5).apply(4);
        long time2 = System.currentTimeMillis() - startTime;
        System.out.println(result1);
        System.out.println(result2);
        System.out.println(time1);
        System.out.println(time2);
    }

    static Function<Tuple3<Integer, Integer, Integer>, Integer> ft =
            x -> longCalculation(x._1)
                    + longCalculation(x._2)
                    - longCalculation(x._3);
    static Function<Tuple3<Integer, Integer, Integer>, Integer> ftm =
            Memoizer.memoize(ft);

    public static void automaticMemoizationExample3() {
        long startTime = System.currentTimeMillis();
        Integer result1 = ftm.apply(new Tuple3<>(2, 3, 4));
        long time1 = System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();
        Integer result2 = ftm.apply(new Tuple3<>(2, 3, 4));
        long time2 = System.currentTimeMillis() - startTime;
        System.out.println(result1);
        System.out.println(result2);
        System.out.println(time1);
        System.out.println(time2);
    }

    public static void main(String[] args) {
        automaticMemoizationExample2();
        System.out.println("___________TUPLE___________");
        automaticMemoizationExample3();
    }

}
