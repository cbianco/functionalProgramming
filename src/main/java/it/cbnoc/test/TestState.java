package it.cbnoc.test;

import it.cbnoc.collection.List;
import it.cbnoc.example.Atm;
import it.cbnoc.example.Deposit;
import it.cbnoc.example.Outcome;
import it.cbnoc.example.Withdraw;

public class TestState {


	public static void main(String[] args) {
		Outcome out = Atm.createMachine().process(List.list(new Deposit(100), new Withdraw(50)))
			.eval(new Outcome(0, List.list()));

		System.out.println(out);
	}

}
