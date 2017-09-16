package it.cbnoc.tuple;

import java.util.Objects;

public class Tuple2<A, B> {

    public A _1;
    public B _2;

    public Tuple2(A a, B b) {
        this._1 = Objects.requireNonNull(a);
        this._2 = Objects.requireNonNull(b);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;

        if (_1 != null ? !_1.equals(tuple2._1) : tuple2._1 != null) return false;
        return _2 != null ? _2.equals(tuple2._2) : tuple2._2 == null;
    }

    @Override
    public int hashCode() {
        int result = _1 != null ? _1.hashCode() : 0;
        result = 31 * result + (_2 != null ? _2.hashCode() : 0);
        return result;
    }


}
