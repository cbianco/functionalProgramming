package it.cbnoc.io;

import it.cbnoc.state.Nothing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

public class Console {

	private static BufferedReader br =
		new BufferedReader(new InputStreamReader(System.in));

	public static IO<String> readLine(Nothing nothing) {
		return () -> {
			try {
				return br.readLine();
			}
			catch (Exception e) {
				throw new IllegalStateException(e);
			}
		};
	}

	public static IO<Nothing> printLine(Object o) {
		return () -> {
			System.out.println(o.toString());
			return Nothing.instance;
		};
	}

}
