package it.cbnoc.example;

import it.cbnoc.function.Function;

public class Weight {

    public final double value;

    public static final Weight ZERO = new Weight(0.0);

    public static Function<Weight, Function<OrderLine, Weight>> sum =
            x -> y -> x.add(y.getWeight());

    private Weight(double value) {
        this.value = value;
    }

    public Weight mult(int count) {
        return new Weight(this.value * count);
    }

    public Weight add(Weight that) {
        return new Weight(this.value + that.value);
    }

    public static Weight weight(double value) {
        return new Weight(value);
    }

    public String toString() {
        return Double.toString(this.value);
    }

}
