package it.cbnoc.test;

import java.math.BigInteger;
import java.util.List;

import static it.cbnoc.utils.CollectionUtilities.*;

import it.cbnoc.function.Function;
import it.cbnoc.utils.TailCall;

import static it.cbnoc.utils.TailCall.*;


public class TestRecursion {

    static Function<Integer, Function<Integer, Integer>> add = x -> y -> {
        class AddHelper {
           Function<Integer, Function<Integer, TailCall<Integer>>> addHelper =
                    a -> b -> b == 0
                            ? ret(a)
                            : sus(() -> this.addHelper.apply(a + 1).apply(b - 1));

        }
        return new AddHelper().addHelper.apply(x).apply(y).eval();
    };

    public static void main(String[] args) {
      //  System.out.println(addRec(0, 100000));
      //System.out.println(sum(range(1,10000)).eval());
//        System.out.println(add.apply(1).apply(1000));
        /*int n = 100000;
        for(int i = 0; i <= n; i++) {
            System.out.print(fib(i) + " ");
        }*/

        System.out.println(fibr(1000));

    }

    public static BigInteger fib(int x) {
        return fib_(BigInteger.ONE, BigInteger.ZERO, BigInteger.valueOf(x)).eval();
    }

    public static String fibr(int x) {
        return fib_(list(),BigInteger.ONE, BigInteger.ZERO, BigInteger.valueOf(x)).eval().toString();
    }

    private static TailCall<BigInteger> fib_(
            BigInteger acc1, BigInteger acc2, BigInteger x) {

        if (x.equals(BigInteger.ZERO)) {
            return ret(BigInteger.ZERO);
        } else if (x.equals(BigInteger.ONE)) {
            return ret(acc1.add(acc2));
        } else {
            return sus(() -> fib_(acc2, acc1.add(acc2), x.subtract(BigInteger.ONE)));
        }
    }

    private static TailCall<List<BigInteger>> fib_(
            List<BigInteger> acc, BigInteger acc1, BigInteger acc2, BigInteger x) {

        return x.equals(BigInteger.ZERO)
                ? ret(acc)
                : x.equals(BigInteger.ONE)
                    ? ret(append(acc, acc1.add(acc2)))
                    : sus(() -> fib_(append(acc, acc2), acc2, acc1.add(acc2), x.subtract(BigInteger.ONE)));
    }

    private static String makeString(List<BigInteger> list, String s) {

        return list.isEmpty()
                ? ""
                : tail(list).isEmpty()
                    ? head(list).toString()
                    : head(list) + foldLeft(tail(list), "", a -> b -> a + s + b);

    }


    static int addRec(int x, int y) {
        return y == 0
                ? x
                : addRec(++x, --y);
    }

    static TailCall<Integer> sum(List<Integer> list) {
        return coSumTail(list, 0);
    }

    static Integer sumTail(List<Integer> list, int acc) {
        return list.isEmpty()
                ? acc
                : sumTail(tail(list), acc + head(list));
    }

    static TailCall<Integer> coSumTail(List<Integer> list, int acc) {
        return list.isEmpty()
                ? ret(acc)
                : sus(() -> coSumTail(tail(list), acc + head(list)));
    }
}
