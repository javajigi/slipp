package net.slipp.support.http2;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class HttpInvocationSupport implements HttpInvocation {
	private final Log log = LogFactory.getLog(getClass());
	private String uri;
	private List<NameValuePair> parameters = new ArrayList<NameValuePair>();
	private List<Header> requestHeaders = new ArrayList<Header>();
	private String encoding;
	private String parameterEncoding;
	private int timeout = 0;
	protected List<Header> responseHeaders = new ArrayList<Header>();
	protected Object returnValue;

	protected HttpInvocationSupport(String url) {
		this(url, "UTF-8");
	}

	protected HttpInvocationSupport(String uri, String encoding) {
		this.uri = uri;
		this.encoding = encoding;
	}

	public void addParameter(String name, Object value) {
		if (value != null) {
			parameters.add(new NameValuePair(name, value.toString()));
		}
	}

	public void addRequestHeader(String name, String value) {
		requestHeaders.add(new Header(name, value));
	}

	public String getUri() {
		return uri;
	}

	public String getEncoding() {
		return encoding;
	}

	public String getParameterEncoding() {
		return parameterEncoding;
	}

	public void setParameterEncoding(String parameterEncoding) {
		this.parameterEncoding = parameterEncoding;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public NameValuePair[] getParameters() {
		return parameters.toArray(new NameValuePair[parameters.size()]);
	}

	public List<Header> getRequestHeaders() {
		return requestHeaders;
	}

	public void onException(Exception exception) {
		throw new HttpInvocationException("URL : " + getUri() + " Cannot retrieve data", exception);
	}

	public void onResponseStatus(int statusCode) {
		if ((statusCode / 100) != 2) {
			new HttpInvocationException("Reponse status code is not 2xx, but " + statusCode);
		}
	}

	public void onResponseHeader(String name, String value) {
		this.responseHeaders.add(new Header(name, value));
	}

	public void onResponseBody(InputStream inputStream) throws Exception {
		String body = IOUtils.toString(inputStream, encoding);
		if (log.isDebugEnabled()) {
			log.debug("Response body: " + body);
		}
		returnValue = parseResponseBody(body);
	}

	protected abstract Object parseResponseBody(String body) throws Exception;

	public Object getReturnValue() {
		return returnValue;
	}
}
