package it.cbnoc.example;

import com.fpinjava.common.Result;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ReadXmlFile {

	public static void main(String[] args) {
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File("C:\\Users\\Cristian\\IdeaProjects\\function-programming\\src\\main\\resources\\file\\file.xml");
		try {
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			List list = rootNode.getChildren("staff");
			for (int i = 0; i < list.size(); i++) {
				Element node = (Element) list.get(i);
				System.out.println("First Name : " +
					node.getChildText("firstname"));
				System.out.println("\tLast Name : " +
					node.getChildText("lastname"));
				System.out.println("\tNick Name : " +
					node.getChildText("email"));
				System.out.println("\tSalary : " + node.getChildText("salary"));
			}
		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}
	}

	private static Result<String> getXmlFilePath() {
		return Result.of("<path_to_file>");
	}

	private static Result<String> getRootElementName() {
		return Result.of("staff");
	}

	public static Result<String> readFile2String(String path) {
		try {
			return Result.success(new String(Files.readAllBytes(Paths.get(path))));
		} catch (IOException e) {
			return Result.failure(String.format("IO error while reading file %s",
				path), e);
		} catch (Exception e) {
			return Result.failure(String.format("Unexpected error while reading file %s", path), e);
		}
	}
}