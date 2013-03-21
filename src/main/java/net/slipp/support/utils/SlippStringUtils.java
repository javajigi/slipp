package net.slipp.support.utils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.UriUtils;

public class SlippStringUtils extends StringUtils {

	public static final String DEFAULT_CHAR_ENCODING = "utf-8";

	private static final Pattern LINK_PATTERN = Pattern.compile("(https?|ftp)://[\\S]+");

	public static String escapeHtml(String str) {
		if (isEmpty(str)) {
			return str;
		}
		String escaped = str.replaceAll("&", "&amp;");
		escaped = escaped.replaceAll("\\<", "&lt;");
		escaped = escaped.replaceAll("\\>", "&gt;");
		escaped = escaped.replaceAll("\"", "&quot;");
		escaped = escaped.replaceAll("\'", "&#39;"); // not not use "&apos;" -
														// IE does not allow it.
		escaped = escaped.replaceAll("  ", " &nbsp;");
		return escaped;
	}

	public static String stripTags(String str) {
		if (isEmpty(str)) {
			return "";
		}

		return str.trim().replaceAll("\\<.*?\\>", "");
	}

	public static String populateLinks(String str, int maxLength, String tail) {
		if (isBlank(str)) {
			return str;
		}

		Matcher matcher = LINK_PATTERN.matcher(str);

		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String link = matcher.group();
			link = link.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$");
			String title = cut(link, maxLength, tail);
			matcher.appendReplacement(sb, String.format("<a href=\"%s\" target=\"_blank\">%s</a>", link, title));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	public static String populateLinks(String str) {
		return populateLinks(str, -1, null);
	}

	public static String cut(String str, int maxLength, String tail) {
		if (isEmpty(str)) {
			return str;
		}

		if (maxLength < 0) {
			return str;
		}

		if (str.length() <= maxLength) {
			return str;
		}

		String cutStr = str.substring(0, maxLength) + tail;
		return cutStr;
	}

	public static String urlEncode(String string) {
		return urlEncode(string, DEFAULT_CHAR_ENCODING);
	}

	public static String urlEncode(String string, String encoding) {
		String encoded;
		try {
			encoded = UriUtils.encodeFragment(string, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException (e.getMessage(), e);
		}
		return encoded;
	}

	public static Long[] asLongArray(String[] stringArray) {
		if (ArrayUtils.isEmpty(stringArray)) {
			return new Long[] {};
		}
		Long[] longArray = new Long[stringArray.length];

		for (int i = 0, length = stringArray.length; i < length; i++) {
			longArray[i] = Long.parseLong(stringArray[i]);
		}
		return longArray;
	}

	/**
	 * 문자열의 새줄 기호를 &lt;br&gt; 태그로 변환한다.
	 *
	 * @param str
	 * @return
	 */
	public static String newLineToBr(String str) {
		if (isEmpty(str)) {
			return str;
		}
		String converted = str.replaceAll("\n", "<br />\n");
		return converted;
	}

	/**
	 * 문자열의 HTML을 Escape해 주고, 새줄기호를 &lt;br&gt;로 변경한다.
	 *
	 * @param str
	 * @return
	 */
	public static String escapeHtmlAndNewLineToBr(String str) {
		return newLineToBr(escapeHtml(str));
	}

	/**
	 * 일반 문자열을 범용적인 형태로 화면상에 출력한다. HTML Escape를 수행하고, 새줄 기호를 &lt;br&gt; 태그로 바꾸고,
	 * 링크 문자열(http://... 등)에 &lt;a&gt; 태그를 지정해준다.
	 *
	 * @param str
	 * @return
	 */
	public static String plainText(String str) {
		return newLineToBr(populateLinks(escapeHtml(str)));
	}

	public static String trimPlainText(String str) {
		return newLineToBr(populateLinks(escapeHtml(trim(str))));
	}

	/**
	 * 글의 태그를 모두 제거하고, 특정 길이 이하의 글자만 남기고 리턴한다.
	 *
	 * @param str
	 * @param maxLength
	 *            최대 글 길이
	 * @param tail
	 *            글이 잘렸을 경우 꼬리 표시
	 * @return
	 */
	public static String stripTagsAndCut(String str, int maxLength, String tail) {
		return cut(stripTags(str), maxLength, tail);
	}

	public static String merge(String p, String s) {
		StringBuilder builder = new StringBuilder(p);
		builder.append(s);
		return builder.toString();
	}
}
