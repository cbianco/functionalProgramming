package it.cbnoc.test;

import it.cbnoc.io.*;
import it.cbnoc.utils.Result;
import it.cbnoc.utils.Stream;

public class TestIO {

	public static void main1(String[] args) {

		Input input = ConsoleReader.consoleReader();

		/*Result<String> rString =
			input.readString("Enter your name: ").map(a -> a._1);

		Result<String> result =
			rString.map(s -> String.format("Hello %s", s));

		result.forEachOrFail(System.out::println)
			.forEach(System.out::println);*/

		//Result<Input> input = FileReader.fileReader("C:\\Users\\Cristian\\Documents\\test.txt");


		/*Stream<Person> personStream = Stream.unfold(input, Person::person);

		System.out.println(personStream.toList());*/

		//IO.forever(() -> System.out.println("ciao"));
	}
	public static void main(String... args) {
		IO program = IO.forever(IO.unit("Hi again!")
			.flatMap(Console::printLine));
		program.run();
	}
	static IOutPut println(String message) {
		return () -> System.out.print(message);
	}

	static <A> String toString(Result<A> rd) {
		return rd.map(Object::toString).getOrElse(rd::toString);
	}

	static Result<Double> inverse(int i) {
		return i == 0
			? Result.failure("Div by 0")
			: Result.success(1.0 / i);
	}

}
