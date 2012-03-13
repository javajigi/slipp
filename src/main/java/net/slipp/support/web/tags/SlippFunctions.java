package net.slipp.support.web.tags;

import java.util.Collection;

import net.slipp.support.utils.SlippStringUtils;

/**
 * JSP EL Functions
 */
public class SlippFunctions {

	/**
	 * 문자열의 HTML을 Escape한다.
	 *
	 * @param str
	 * @return
	 */
	public static String h(String str) {
		return SlippStringUtils.escapeHtml(str);
	}

	/**
	 * 문자열의 새줄기호를 &lt;br&gt; 로 변경한다.
	 *
	 * @param str
	 * @return
	 */
	public static String br(String str) {
		return SlippStringUtils.newLineToBr(str);
	}

	/**
	 * 문자열의 HTML을 Escape해 주고, 새줄기호를 &lt;br&gt;로 변경한다.
	 *
	 * @param str
	 * @return
	 */
	public static String hbr(String str) {
		return SlippStringUtils.escapeHtmlAndNewLineToBr(str);
	}

	/**
	 * 문자열에 있는 링크를 a 태그로 변환한다.
	 *
	 * @param str
	 * @return
	 */
	public static String links(String str) {
		return SlippStringUtils.populateLinks(str);
	}

	/**
	 * 일반 문자열을 범용적인 형태로 화면상에 출력한다. HTML Escape를 수행하고, 새줄 기호를 &lt;br&gt; 태그로 바꾸고,
	 * 링크 문자열(http://... 등)에 &lt;a&gt; 태그를 지정해준다.
	 *
	 * @param str
	 * @return
	 */
	public static String plainText(String str) {
		return SlippStringUtils.plainText(str);
	}

	public static String trimPlainText(String str) {
		return SlippStringUtils.trimPlainText(str);
	}

	/**
	 * URL Encoding을 수행한다.
	 *
	 * @param str
	 * @return
	 */
	public static String urlEncode(String str) {
		return SlippStringUtils.urlEncode(str);
	}

	/**
	 * 표현식이 true이면 value를 출력하고 아니면 아무것도 출력하지 않는다.
	 *
	 * @param expression
	 * @param value
	 * @return
	 */
	public static Object whenTrue(boolean expression, Object value) {
		if (expression) {
			return value;
		}

		return null;
	}

	public static Object whenTrueOr(boolean expression, Object trueValue, Object falseValue) {
		if (expression) {
			return trueValue;
		}
		return falseValue;
	}

	/**
	 * 표현식이 false이면 value를 출력하고 아니면 아무것도 출력하지 않는다.
	 *
	 * @param expression
	 * @param value
	 * @return
	 */
	public static Object whenFalse(boolean expression, Object value) {
		return whenTrue(!expression, value);
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
		return SlippStringUtils.stripTagsAndCut(str, maxLength, tail);
	}

	/**
	 * 특정 길이 이하의 글자만 남기고 리턴한다.
	 *
	 * @param str
	 * @param maxLength
	 * @param tail
	 * @return
	 */
	public static String cut(String str, int maxLength, String tail) {
		return SlippStringUtils.cut(str, maxLength, tail);
	}

	/**
	 * instanceOf
	 *
	 * @param o
	 * @param className
	 * @return
	 */
	public static boolean instanceOf(Object o, String className) {
		boolean returnValue;
		try {
			returnValue = Class.forName(className).isInstance(o);
		} catch (ClassNotFoundException e) {
			returnValue = false;
		}

		return returnValue;
	}

	@SuppressWarnings("rawtypes")
	public static boolean hasItem(Collection collection, Object element) {
		if (collection == null || element == null) {
			return false;
		}

		return collection.contains(element);
	}
}