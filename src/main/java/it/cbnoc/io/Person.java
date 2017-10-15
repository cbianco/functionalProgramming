package it.cbnoc.io;

import it.cbnoc.function.Function;
import it.cbnoc.tuple.Tuple2;
import it.cbnoc.utils.Result;
import it.cbnoc.utils.Stream;

public class Person {

	private static final String FORMAT =
		"ID: %s, First name: %s, Last name: %s";
	public final int id;
	public final String firstName;
	public final String lastName;

	private Person(int id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public static Person apply(int id, String firstName, String lastName) {
		return new Person(id, firstName, lastName);
	}

	@Override
	public String toString() {
		return String.format(FORMAT, id, firstName, lastName);
	}

	public static Result<Tuple2<Person, Input>> person(Input input) {
		return _person(input);
	}

	private static Result<Tuple2<Person, Input>> _person(Input input) {


		Result<Tuple2<Person, Input>> result =
			input.readInt("Inserisci l ID dell utente: ")
			.flatMap(id -> id._1 == 0
				? Result.empty()
				: id._2.readString("Inserisci nome: ")
				.flatMap(name -> name._1.length() == 0
					? Result.empty()
					: name._2.readString("Inserisci cognome: ")
					.flatMap(surname -> surname._1.length() == 0
						? Result.empty()
						: Result.success(new Tuple2<>(new Person(id._1, name._1, surname._1 ), surname._2)))));

		return result;
	}
}