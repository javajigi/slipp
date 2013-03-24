package net.slipp.support.http;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link HttpInvocation} 인터페이스를 미리 구현해 둔 상태
 */
public abstract class HttpInvocationSupport<T> implements HttpInvocation<T> {
	private Logger log = LoggerFactory.getLogger(HttpInvocationSupport.class);

	private String uri;
	private List<NameValuePair> parameters = new ArrayList<NameValuePair>();
	private List<Header> requestHeaders = new ArrayList<Header>();
	private String encoding;
	private String parameterEncoding;
	protected List<Header> responseHeaders = new ArrayList<Header>();
	protected T returnValue;

	protected HttpInvocationSupport(String url) {
		this(url, "UTF-8");
	}

	protected HttpInvocationSupport(String uri, String encoding) {
		this.uri = uri;
		this.encoding = encoding;
	}

	@Override
	public void addParameter(String name, Object value) {
		if (value != null) {
			parameters.add(new NameValuePair(name, value.toString()));
		}
	}

	@Override
	public void addRequestHeader(String name, String value) {
		requestHeaders.add(new Header(name, value));
	}

	@Override
	public String getUri() {
		return uri;
	}

	@Override
	public String getEncoding() {
		return encoding;
	}

	@Override
	public String getParameterEncoding() {
		return parameterEncoding;
	}

	public void setParameterEncoding(String parameterEncoding) {
		this.parameterEncoding = parameterEncoding;
	}

	@Override
	public NameValuePair[] getParameters() {
		return parameters.toArray(new NameValuePair[parameters.size()]);
	}

	@Override
	public List<Header> getRequestHeaders() {
		return requestHeaders;
	}

	@Override
	public void onException(Exception exception) {
		throw new HttpInvocationException("Cannot retrieve data", exception);
	}

	@Override
	public void onResponseStatus(int statusCode) {
		log.debug("STATUS CODE : {}", statusCode);
		if ((statusCode / 100) != 2) {
			throw new HttpInvocationException("Reponse status code is not 2xx, but " + statusCode);
		}
	}

	@Override
	public void onResponseHeader(String name, String value) {
		this.responseHeaders.add(new Header(name, value));
	}

	@Override
	public void onResponseBody(InputStream inputStream) throws Exception {
		String body = IOUtils.toString(inputStream, encoding);
		if (log.isDebugEnabled()) {
			log.debug("Response body: " + body);
		}
		returnValue = parseResponseBody(body);
	}

	@Override
	public T getReturnValue() {
		return returnValue;
	}

	protected abstract T parseResponseBody(String body) throws Exception;
}
