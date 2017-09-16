package it.cbnoc.example;

import it.cbnoc.function.Function;

public class Price {

    public final double value;

    public static final Price ZERO = new Price(0.0);

    public static Function<Price, Function<OrderLine, Price>> sum =
            x -> y -> x.add(y.getAmount());

    private Price(double value) {
        this.value = value;
    }

    public static Price price(double value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        } else {
            return new Price(value);
        }
    }

    public Price add(Price that) {
        return new Price(this.value + that.value);
    }

    public Price mult(int count) {
        return new Price(this.value * count);
    }

    public String toString() {
        return Double.toString(this.value);
    }

}