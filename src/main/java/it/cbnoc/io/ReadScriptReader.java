package it.cbnoc.io;

import it.cbnoc.tuple.Tuple2;
import it.cbnoc.utils.Result;
import it.cbnoc.utils.Stream;

public class ReadScriptReader {

	public static void main(String... args) {
		Input input = new ScriptReader(
			"0", "Mickey", "Mouse",
			"1", "Minnie", "Mouse",
			"2", "Donald", "Duck",
			"3", "Homer", "Simpson"
		);

		Stream<Person> stream =
			Stream.unfold(input, ReadScriptReader::person);
		stream.toList().forEach(System.out::println);
	}

	public static Result<Tuple2<Person, Input>> person(Input input) {
		return input.readInt("Enter ID:")
			.flatMap(id -> id._2.readString("Enter first name:")
				.flatMap(firstName -> firstName._2.readString("Enter last name:")
					.map(lastName -> new Tuple2<>(Person.apply(id._1, firstName._1,
						lastName._1), lastName._2))));
	}
}