package it.cbnoc.example;

public interface Input {

	Type type();

	boolean isDeposit();

	boolean isWithdraw();

	int getAmount();

	enum Type {DEPOSIT,WITHDRAW}

}