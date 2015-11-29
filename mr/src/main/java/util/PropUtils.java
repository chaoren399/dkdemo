package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropUtils {
	private static Properties prop = new Properties();
	public static Properties getProp(String propName){
		InputStream resourceAsStream = PropUtils.class.getClassLoader().getResourceAsStream(propName);
		try {
			prop.load(resourceAsStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
}
