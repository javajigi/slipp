package net.slipp.support.http2;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.EncodingUtil;

public class HttpClientUtil {
	/**
	 * Create PostMethod with basic parameters.
	 */
	public static PostMethod postMethod(String uri, Map<String, Object> parameters, String encoding) {
		PostMethod method = new PostMethod(uri);
		HttpMethodParams params = method.getParams();
		params.setContentCharset(encoding);
		for (Entry<String, Object> entry : parameters.entrySet()) {
			method.addParameter(entry.getKey(), entry.getValue().toString());
		}
		return method;
	}

	/**
	 * Create GetMethod with basic parameters.
	 */
	public static GetMethod getMethod(String uri, Map<String, Object> parameters, String encoding) {
		GetMethod method = new GetMethod(uri);
		HttpMethodParams params = method.getParams();
		params.setContentCharset(encoding);
		method.setQueryString(toQueryString(parameters, encoding));
		return method;
	}

	public static HttpMethod getMethod(String uri, NameValuePair[] parameters, String encoding) {
		GetMethod method = new GetMethod(uri);
		method.setQueryString(parameters);
		return method;
	}

	static String toQueryString(Map<String, Object> parameters, String encoding) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for (Entry<String, Object> each : parameters.entrySet()) {
			pairs.add(new NameValuePair(each.getKey(), each.getValue().toString()));
		}
		return EncodingUtil.formUrlEncode(pairs.toArray(new NameValuePair[pairs.size()]), encoding);
	}

	static String urlEncode(String s, String encoding) {
		try {
			return URLEncoder.encode(s, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

}
