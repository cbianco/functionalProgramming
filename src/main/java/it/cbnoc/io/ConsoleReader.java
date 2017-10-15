package it.cbnoc.io;

import it.cbnoc.tuple.Tuple2;
import it.cbnoc.utils.Result;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConsoleReader extends AbstractReader {

	protected ConsoleReader(BufferedReader reader) {
		super(reader);
	}

	@Override
	public Result<Tuple2<Integer, Input>> readInt(String message) {

		System.out.println(message + " ");
		return readInt();
	}

	@Override
	public Result<Tuple2<String, Input>> readString(String message) {
		System.out.println(message + " ");
		return readString();
	}

	public static ConsoleReader consoleReader() {
		return new ConsoleReader(new BufferedReader(
			new InputStreamReader(System.in)));
	}
}
