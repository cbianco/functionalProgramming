package it.cbnoc.example;

public class Payment {

	public final String name;
	public final int amount;

	public Payment(String name, int amount) {
		this.name = name;
		this.amount = amount;
	}

	@Override
	public String toString() {

		return String.format("Payment(name: %s, amount: %s)", name, amount);
	}
}