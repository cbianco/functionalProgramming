package it.cbnoc.test;

import it.cbnoc.manning.Function;

public class MAAAIN {

    public static void main1(String[] args) {

        Function<Integer, Integer> triple = x -> x * 3;
        Function<Integer, Integer> square = x -> x * x;

        Function<Function<Integer, Integer>,
                Function<Function<Integer, Integer>,
                        Function<Integer, Integer>>> compose =
                x -> y -> z -> x.apply(y.apply(z));


        Function<Integer, Integer> function1 = compose.apply(square).apply(triple);

        Function<Integer, Integer> function2 = square.compose(triple);

        Function<Integer, Integer> function3 = Function.compose(square, triple);

        System.out.println(function1.apply(2));
        System.out.println(function2.apply(2));
        System.out.println(function3.apply(2));

        Function.<Integer,Integer,Integer>higherCompose().apply(triple).apply(square).apply(2);

    }

    public static void main3(String[] args) {

        Function<Double, Double> f = x -> Math.PI / 2 - x;
        Function<Double, Double> sin = Math::sin;
        Double cos = Function.compose(f, sin).apply(2.0);

        Double cos1 = Function.compose(x -> Math.PI / 2 - x, Math::sin).apply(2.0);

        Double cos2 = Function.<Double, Double, Double>higherCompose()
                .apply(z -> Math.PI / 2 - z).apply(Math::sin).apply(2.0);

        System.out.println(cos2);

        Function.compose(z -> Math.PI / 2 - z,
                (Function<Double, Double>) (a) -> Math.sin(a)).apply(.0);

    }

    public static final Function<Integer, Integer> factorial =
            n -> n <= 1 ? n : n * MAAAIN.factorial.apply(n - 1);;

    public static void functionRecursive(String[] args) {

        final Function<String, String> partialABC =
                Function.partialABC("1", "2", "3", a -> b -> c -> d -> String.format("%s, %s, %s, %s", a, b, c, d));

        System.out.println(factorial.apply(1000000));


    }

    static public int factorial(int n) {
        return n == 0 ? 1 : n * factorial(n - 1);
    }


    <A, B, C, D> String func(A a, B b, C c, D d) {
        return String.format("%s, %s, %s, %s", a, b, c, d);
    }

}
