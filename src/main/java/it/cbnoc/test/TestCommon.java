package it.cbnoc.test;

import it.cbnoc.common.problem.PropertyReader;

public class TestCommon {

	public static void main(String[] args) {

		PropertyReader propertyReader = new PropertyReader("C:\\Users\\Cristian\\IdeaProjects\\function-programming\\src\\main\\resources\\file\\test.properties");
		propertyReader.getProperty("host")
			.forEachOrFail(System.out::println)
			.forEach(System.out::println);

		propertyReader.getProperty("name")
			.forEachOrFail(System.out::println)
			.forEach(System.out::println);

		propertyReader.getProperty("year")
			.forEachOrFail(System.out::println)
			.forEach(System.out::println);

	}

}
