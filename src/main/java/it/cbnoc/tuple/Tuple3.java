package it.cbnoc.tuple;

import java.util.Objects;

public class Tuple3<A, B, C> {

    public final A _1;
    public final B _2;
    public final C _3;

    public Tuple3(A a, B b, C c) {
        _1 = Objects.requireNonNull(a);
        _2 = Objects.requireNonNull(b);
        _3 = Objects.requireNonNull(c);
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tuple3)) return false;
        else {
            Tuple3 that = (Tuple3) o;
            return _1.equals(that._1) && _2.equals(that._2)
                    && _3.equals(that._3);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + _1.hashCode();
        result = prime * result + _2.hashCode();
        result = prime * result + _3.hashCode();
        return result;
    }
}