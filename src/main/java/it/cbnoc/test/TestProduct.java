package it.cbnoc.test;

import it.cbnoc.example.OrderLine;
import static it.cbnoc.example.Price.*;

import it.cbnoc.example.Price;
import it.cbnoc.example.Product;
import it.cbnoc.example.Weight;

import static it.cbnoc.example.Weight.*;

import java.util.List;

import static it.cbnoc.utils.CollectionUtilities.*;

public class TestProduct {

    public static void main(String[] args) {

        Product toothPaste = new Product("Tooth paste", price(1.5), weight(0.5));
        Product toothBrush = new Product("Tooth brush", price(3.5), weight(0.3));
        List<OrderLine> order =
                list(new OrderLine(toothPaste, 2), new OrderLine(toothBrush, 3));
        Price price = foldLeft(order, Price.ZERO, Price.sum);
        Weight weight = foldLeft(order, Weight.ZERO, Weight.sum);
        System.out.println(String.format("Total price: %s", price));
        System.out.println(String.format("Total weight: %s", weight));
    }

}
