package it.cbnoc.test;

import it.cbnoc.collection.List;
import it.cbnoc.function.Function;
import it.cbnoc.utils.Option;

import static it.cbnoc.collection.List.list;

public class TestOption {


    static Function<List<Double>, Double> sum =
            ds -> ds.foldLeft(0.0, a -> b -> a + b);

    static Function<List<Double>, Option<Double>> mean =
            ds -> ds.isEmpty()
                    ? Option.none()
                    : Option.some(sum.apply(ds) / ds.length());

    static Function<List<Double>, Option<Double>> variance =
            ds -> mean.apply(ds)
                    .flatMap(m -> mean.apply(ds.map(x -> Math.pow(x - m, 2))));

    public static void main(String[] args) {
        List<Integer> ints = list(1, 2, 3, 4, 5, 6);

        /*System.out.println(TestOption.<Integer>max().apply(list(1,2,3,4,5)).getOrThrow());

        int max1 = TestOption.<Integer>max().apply(list(3, 5, 7, 2, 1)).getOrElse(() -> getDefault());
        System.out.println(max1);
        int max2 = TestOption.<Integer>max().apply(list()).getOrElse(() -> getDefault());
        System.out.println(max2);*/



        Function<Integer, Function<String, Integer>> parseWithRadix =
                radix -> string -> Integer.parseInt(string, radix);

        Function<String, Option<Integer>> parse16 =
                Option.hlift(parseWithRadix.apply(16));

        List<String> list = List.list("4", "5", "6", "7", "8", "9");

        Option<List<Integer>> result = Option.sequence(list.map(parse16));

        System.out.println(result);

        System.out.println(variance.apply(list(3.0,1.0,2.0)));

    }

    public static <A extends Comparable<A>> Function<List<A>, Option<A>> max() {
        return xs -> xs.isEmpty()
                ? Option.none()
                : Option.some(xs.foldLeft(xs.head(),
                x -> y -> x.compareTo(y) > 0 ? x : y));

    }
    static int getDefault() {
        throw new RuntimeException();
    }

}
