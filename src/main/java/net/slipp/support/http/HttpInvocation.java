package net.slipp.support.http;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.NameValuePair;

public interface HttpInvocation<T> {
	void addParameter(String name, Object value);

	void addRequestHeader(String name, String value);

	String getUri();

	String getEncoding();

	String getParameterEncoding();

	NameValuePair[] getParameters();

	List<Header> getRequestHeaders();

	void onResponseStatus(int statusCode);

	void onException(Exception exception);

	void onResponseHeader(String name, String value);

	void onResponseBody(InputStream inputStream) throws Exception;

	T getReturnValue();
}
