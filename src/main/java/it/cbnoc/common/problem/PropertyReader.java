package it.cbnoc.common.problem;

import com.fpinjava.common.Result;
import it.cbnoc.io.FileReader;

import java.io.*;
import java.util.Properties;

public class PropertyReader {

	private final Result<Properties> properties;

	public PropertyReader(String configFile) {
		this.properties = readProperties(configFile);
	}

	private Result<Properties> readProperties(String configFile) {
		try(InputStream inputStream = new FileInputStream(new File(configFile))) {
			Properties properties = new Properties();
			properties.load(inputStream);
			return Result.success(properties);
		}
		catch (Exception e) {
			return Result.failure(e);
		}
	}

	public Result<String> getProperty(String name) {
		return properties.flatMap(props -> getProperty(props, name));
	}

	private Result<String> getProperty(Properties properties, String name) {
		return Result.of(properties.getProperty(name));
	}

}
