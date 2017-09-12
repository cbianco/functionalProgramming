package it.cbnoc.test;

import it.cbnoc.manning.function.Effect;
import it.cbnoc.manning.function.Function;
import it.cbnoc.manning.function.Result;

import java.util.regex.Pattern;

import static it.cbnoc.manning.function.Case.match;
import static it.cbnoc.manning.function.Case.mcase;
import static it.cbnoc.manning.function.Result.failure;
import static it.cbnoc.manning.function.Result.success;

public class TestCase {

	static Pattern pattern = Pattern.compile("(ciao)");

	static Effect<String> success =
		s -> System.out.println("success ".concat(s));

	static Effect<String> failure =
		s -> System.out.println("failure ".concat(s));

	static Function<String, Result<String>> emailChecker = s -> match(
		mcase(() -> success(s)),
		mcase(() -> s == null, () -> failure("email null")),
		mcase(() -> s.length() == 0, () -> failure("email empty")),
		mcase(() -> !pattern.matcher(s).matches(),
			() -> failure("email "+ s +" is invalid")
		)
	);

	public static void main(String[] args){
		emailChecker.apply(null).bind(success, failure);
		emailChecker.apply("").bind(success, failure);
		emailChecker.apply("v").bind(success, failure);
		emailChecker.apply("ciao").bind(success, failure);
	}

}
