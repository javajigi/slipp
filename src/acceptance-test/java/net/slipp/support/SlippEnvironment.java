package net.slipp.support;

import java.io.IOException;
import java.util.Properties;

import net.slipp.support.utils.ClasspathResourceUtils;
import net.slipp.support.utils.ConvenientProperties;

public class SlippEnvironment {
	private ConvenientProperties convenientProperties;

	public SlippEnvironment() {
		Properties properties = loadProperties();
		convenientProperties = new ConvenientProperties(properties);
	}
	
	public String getProperty(String key) {
		return convenientProperties.getProperty(key);
	}
	
	private Properties loadProperties() {
		Properties properties = new Properties();
		try {
			properties.loadFromXML(ClasspathResourceUtils.getResourceAsStream("test-application-properties.xml"));
		} catch (IOException e) {
		}
		return properties;
	}
}
