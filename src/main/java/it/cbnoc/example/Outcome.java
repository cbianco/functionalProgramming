package it.cbnoc.example;

import it.cbnoc.collection.List;

public class Outcome {

	public final Integer account;
	public final List<Integer> operations;

	public Outcome(Integer account, List<Integer> operations) {
		super();
		this.account = account;
		this.operations = operations;
	}

	public String toString() {
		return "(" + account.toString() + "," + operations.toString() + ")";
	}
}