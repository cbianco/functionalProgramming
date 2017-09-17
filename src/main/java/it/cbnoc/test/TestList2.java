package it.cbnoc.test;

import it.cbnoc.collection.List;
import it.cbnoc.function.Function;

import static it.cbnoc.collection.List.*;

public class TestList2 {

    public static void main(String[] args) {
        List<Integer> ex1 = list();
        List<Integer> ex2 = list(6, 7, 8, 9, 10);
        List<Integer> ex3 = list(1, 2, 3, 4, 5);
        List<Double> ex4 = list(1.0, 2.0, 3.0, 4.0, 5.0);


        List<Integer> list = list(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        /*  List<List<Integer>> listList = list(list(1), list(1,2), list(1,2,3), list(1,2,3));
        System.out.println(List.flatten(listList));
        System.out.println(ex4.map(a -> a * 3));
        System.out.println(ex4.map(a -> a * 3).filter(a -> a > 14));

        System.out.println(List.list(1,2,3).flatMap(i -> List.list(i, -i)));
        System.out.println(List.filterViaFlatMap(ex4,a -> a > 3));
        ex4.foldLeft(0.0, a -> b -> a + b);
        System.out.println(List.concat(ex2,ex3));
        System.out.println(List.concat(ex2,ex3));
        System.out.println(List.reverseViaFoldLeft(ex4));
        System.out.println(sum(ex3, 0));
        System.out.println(product(ex4));
        System.out.println(List.foldRight(ex4, 1.0, a -> b -> a * b));
        System.out.println(List.foldRight(list(1,2,3), list(), a -> b -> b.cons(a)));*/

    }

    public static int sum(List<Integer> list, int acc) {
        return list.isEmpty()
                ? acc
                : sum(list.tail(), acc + list.head());
    }

    public static Double product(List<Double> ds) {
        return ds.isEmpty()
                ? 1.0
                : ds.head() == 0.0
                    ? 0.0
                    : ds.head() * product(ds.tail());
    }

}
