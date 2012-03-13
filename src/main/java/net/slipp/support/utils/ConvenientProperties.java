package net.slipp.support.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 편리한 Properties - Properties 클래스를 상속하여 Properties에서 문자열,숫자,boolean 등의 값을 직접 뽑아
 * 낼 수 있으며, 또한, SystemProperties의 경우 ${system.property.name} 형태로 만들어서, 이 값을 실제
 * System Property값으로 변환하여 저장하도록 처리한다.
 */
public class ConvenientProperties extends Properties {

	private static final long serialVersionUID = 1L;

	private Logger log = LoggerFactory.getLogger(ConvenientProperties.class);

	private static Pattern SYSTEM_PROPERTY_NAME_PATTERN = Pattern.compile("\\$\\{([\\w\\.]+)\\}");

	public ConvenientProperties(Properties properties) {
		super(properties);
		Assert.notNull(properties, "properties should not be null");
		processSystemProperties();
		logProperties();
	}

	private void processSystemProperties() {
		for (String key : stringPropertyNames()) {
			String value = getProperty(key);

			Matcher matcher = SYSTEM_PROPERTY_NAME_PATTERN.matcher(value);

			StringBuffer sb = new StringBuffer();

			while (matcher.find()) {
				convertSystemPropertyToValue(matcher, sb);
			}

			matcher.appendTail(sb);
			setProperty(key, sb.toString());
		}
	}

	private void convertSystemPropertyToValue(Matcher matcher, StringBuffer sb) {
		String sysPropertyName = matcher.group(1);
		String sysPropertyValue = System.getProperty(sysPropertyName);

		log.debug("sysProperty Name: {}, Value: {}", sysPropertyName, sysPropertyValue);
		if (sysPropertyValue == null) {
			throw new NullPointerException(sysPropertyName + " 이 존재하지 않습니다.");
		}
		matcher.appendReplacement(sb, sysPropertyValue.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$"));
	}

	public int getInt(String key) {
		return Integer.parseInt(getProperty(key));
	}

	public int getInt(String key, int defaultValue) {
		String value = getProperty(key);
		if (value == null) {
			return defaultValue;
		}
		return Integer.parseInt(value);
	}

	public long getLong(String key) {
		return Long.parseLong(getProperty(key));
	}

	public long getLong(String key, long defaultValue) {
		String value = getProperty(key);

		if (value == null) {
			return defaultValue;
		}
		return Long.parseLong(value);
	}

	public double getDouble(String key) {
		return Double.parseDouble(getProperty(key));
	}

	public double getDouble(String key, double defaultValue) {
		String value = getProperty(key);

		if (value == null) {
			return defaultValue;
		}

		return Double.parseDouble(value);
	}

	public boolean getBoolean(String key) {
		return Boolean.valueOf(getProperty(key));
	}

	public void logProperties() {
		if (log.isDebugEnabled()) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			this.list(pw);

			log.debug("Convinient Properties loaded : {}", sw.toString());
			pw.close();
		}
	}
}