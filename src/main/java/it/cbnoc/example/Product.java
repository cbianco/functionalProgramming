package it.cbnoc.example;

public class Product {

    public final String name;

    public final Price price;
    public final Weight weight;

    public Product(String name, Price price, Weight weight) {
        this.name = name;
        this.price = price;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Weight getWeight() {
        return weight;
    }

}