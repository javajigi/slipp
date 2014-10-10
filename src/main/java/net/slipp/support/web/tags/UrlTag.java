package net.slipp.support.web.tags;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import net.slipp.support.utils.ClasspathResourceUtils;
import net.slipp.support.utils.SlippStringUtils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * 객체의 URL을 구성한다.
 *
 * xxxF(urlFormat, value) 버전은 urlFormat 으로 String의 Format을 받는다.
 * ${url:imgF("/boards/%s", board.type)} 형태로 사용하며 결과는 /boards/free 와 같은 형태로 나온다.
 */
public class UrlTag {

	private static final String XML_EXTENSION = "xml";

	private static final String STATIC_RESOURCE_URL_PROPERTY_KEY = "static.server.urls";

	private static final String PROPERTIES_RESOURCE_PATH = "application-properties.xml";

	private static final String STATIC_LIB_PROPERTEIS_RESOURCE_PATH = "staticlib-properties.xml";

	private static final String DISABLE_JS_PACK_PROPERTY_KEY = "urltag.disable.js.pack";

	protected static final Properties APPLICATION_PROPERTIES = new Properties();

	protected static final Properties STATIC_LIB_PROPERTIES = new Properties();

	/**
	 * js,style 등에 사용할 버전
	 */
	public static final String VERSION;

	public static final String[] STATIC_SERVER_URLS;

	/**
	 * static이 아닌 WAS에서 관리하는 public resource 경로에 대한 prefix
	 */
	public static final String RESOURCE_PREFIX = "/resources";

	public static boolean disableJsPack = false;

	static {
		loadProperties(APPLICATION_PROPERTIES, PROPERTIES_RESOURCE_PATH);
		loadProperties(STATIC_LIB_PROPERTIES, STATIC_LIB_PROPERTEIS_RESOURCE_PATH);

		VERSION = populateVersion();
		STATIC_SERVER_URLS = populateStaticServerUrl();
		disableJsPack = populateDisableJsPack();
	}

	private static void loadProperties(Properties properties, String resourcePath) {
		InputStream is = null;
		try {
			is = ClasspathResourceUtils.getResourceAsStream(resourcePath);
			if (resourcePath.toLowerCase().endsWith(XML_EXTENSION)) {
				properties.loadFromXML(is);
			} else {
				properties.load(is);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	private static String populateVersion() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		return sdf.format(new Date());
	}

	private static String[] populateStaticServerUrl() {
		String staticResourceUrlProperty = APPLICATION_PROPERTIES.getProperty(STATIC_RESOURCE_URL_PROPERTY_KEY);
		if (staticResourceUrlProperty == null) {
			staticResourceUrlProperty = "";
		}
		return staticResourceUrlProperty.split(",");
	}

	private static boolean populateDisableJsPack() {
		String disableJsPackStr = APPLICATION_PROPERTIES.getProperty(DISABLE_JS_PACK_PROPERTY_KEY, "false");
		return Boolean.parseBoolean(disableJsPackStr);
	}

	public String getVersion() {
		return VERSION;
	}

	public static Properties getProperties() {
		return APPLICATION_PROPERTIES;
	}

	/**
	 * static 파일 공통 URL 생성기
	 *
	 * @param url
	 *            기본 URL
	 * @param withVersion
	 *            버전정보 표시 여부
	 * @return
	 */
	private static String staticUrl(String url, boolean withVersion) {
		return staticUrl(url, withVersion, null);
	}

	/**
	 * static 파일 공통 URL 생성기
	 *
	 * @param url
	 *            기본 URL
	 * @param withVersion
	 *            버전정보 표시 여부
	 * @return
	 */
	private static String staticUrl(String url, boolean withVersion, Integer serverId) {
		Assert.hasText(url, "url을 지정해야 합니다.");

		if (serverId == null || serverId.intValue() < 0 || serverId.intValue() >= STATIC_SERVER_URLS.length) {
			char lastCharExceptExt = getLastCharExceptExt(url);
			serverId = lastCharExceptExt % STATIC_SERVER_URLS.length;
		}

		String finalUrl = STATIC_SERVER_URLS[serverId];

		if (withVersion) {
			finalUrl = finalUrl + RESOURCE_PREFIX + UrlTag.VERSION;
		}

		finalUrl += url;
		return SlippStringUtils.escapeHtml(finalUrl);
	}

	private static char getLastCharExceptExt(String url) {
		int dotIdx = url.lastIndexOf(".");
		String nameExceptExt = url;

		if (dotIdx > 0) {
			nameExceptExt = url.substring(0, dotIdx);
		}

		return nameExceptExt.charAt(nameExceptExt.length() - 1);

	}

	/**
	 * WAS 관리 resource URL 생성기
	 *
	 * @param url
	 * @return
	 */
	private static String resourceUrl(String url) {
		Assert.hasText(url, "url을 지정해야 합니다.");
		return String.format("%s%s%s", RESOURCE_PREFIX, "", url);
	}

	/**
	 * js는 관리툴에서만 사용할 것.
	 */
	public static String js(String url) {
		return staticUrl(url, true);
	}

	public static String img(String url) {
		return staticUrl(url, true);
	}

	public static String imgFormat(String urlFormat, String value) {
		String url = String.format(urlFormat, value);
		return img(url);
	}

	public static String imgFormat(String urlFormat, String value1, String value2) {
		String url = String.format(urlFormat, value1, value2);
		return img(url);
	}

	public static String lib(String libName) {
		String url = STATIC_LIB_PROPERTIES.getProperty(libName);
		if (StringUtils.isBlank(url)) {
			throw new IllegalArgumentException(libName + "은 존재하지 않는 Static Library 이름입니다.");
		}
		return staticUrl(url, false);
	}

	public static String resource(String url) {
		return resourceUrl(url);
	}

	public static String resourceFormat(String urlFormat, String value) {
		String url = String.format(urlFormat, value);
		return resourceUrl(url);
	}

	public static String resourceFormat(String urlFormat, String value1, String value2) {
		String url = String.format(urlFormat, value1, value2);
		return resourceUrl(url);
	}

	public static String style(String url) {
		return staticUrl(url, true);
	}
}