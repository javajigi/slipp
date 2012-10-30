package net.slipp.support.http2;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.junit.Test;

public class HttpClientUtilTest {
	static final String KEY = "key";
	static final String VALUE = "값";

	@Test
	public void testPostMethodParameterEncoding() throws IOException {
		PostMethod method = new PostMethod();
		HttpMethodParams params = method.getParams();
		params.setContentCharset("EUC-KR");
		method.addParameter(KEY, VALUE);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		method.getRequestEntity().writeRequest(out);
		assertEquals(KEY + "=" + HttpClientUtil.urlEncode(VALUE, "EUC-KR"), new String(out.toByteArray()));
	}

	@Test
	public void testGetMethodParameterEncoding() {
		GetMethod method = new GetMethod();
		HttpMethodParams params = method.getParams();
		params.setContentCharset("EUC-KR");
		method.setQueryString(new NameValuePair[] { new NameValuePair(KEY, VALUE) });
		assertEquals(KEY + "=" + HttpClientUtil.urlEncode(VALUE, "UTF-8"), method.getQueryString());
	}

	@Test
	public void testPostMethod() throws IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("key1", "value1");
		PostMethod method = HttpClientUtil.postMethod(null, params, "EUC-KR");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		method.getRequestEntity().writeRequest(out);
		assertEquals("key1=value1", new String(out.toByteArray()));
	}

	@Test
	public void testGetMethod() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("key1", "value1");
		GetMethod method = HttpClientUtil.getMethod(null, params, "EUC-KR");
		assertEquals("key1=value1", method.getQueryString());
	}
}
