package net.slipp.support.http;

import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * HTTP 요청을 처리하고 응답을 돌려준다.
 *
 * 요청에 대한 응답은 {@link HttpInvocation} 객체가 처리한다.
 */
public class HttpClientManager {

	/**
	 * 공용 HttpClient 객체. HttpClient는 재사용 가능하다.
	 * http://hc.apache.org/httpclient-3.
	 * x/performance.html#Reuse_of_HttpClient_instance
	 */
	private HttpClient httpClient;

	private int timeout;

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);
	}

	/**
	 * 기본적으로 멀티쓰레드로 HttpClient를 공유해서 쓸 수 있도록 HttpClient객체를 생성한다.
	 */
	public HttpClientManager() {
		// Must use MultiThreadedHttpConnectionManager for sharing httpClient
		// object
		this(new HttpClient(new MultiThreadedHttpConnectionManager()));
	}

	public HttpClientManager(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	/**
	 * GET 요청을 처리한다.
	 *
	 * @param baseUrl
	 *            기본 URL
	 * @param httpInvocation
	 *            기본 URL뒤에 붙는 경로
	 * @return
	 */
	public <T> T get(String baseUrl, HttpInvocation<T> httpInvocation) {
		Assert.notNull(httpInvocation, "httpInvocation 값은 null일 수 없다.");

		GetMethod getMethod = new GetMethod(baseUrl + httpInvocation.getUri());
		if (httpInvocation.getParameters().length > 0) {
			getMethod.setQueryString(httpInvocation.getParameters());
		}
		return invoke(getMethod, httpInvocation);
	}

	/**
	 * POST 요청을 처리한다.
	 *
	 * @param <T>
	 * @param baseUrl
	 * @param httpInvocation
	 * @return
	 */
	public <T> T post(String baseUrl, HttpInvocation<T> httpInvocation) {
		Assert.notNull(httpInvocation, "httpInvocation 값은 null일 수 없다.");
		PostMethod postMethod = new PostMethod(baseUrl + httpInvocation.getUri());

		String parameterEncoding = httpInvocation.getParameterEncoding();
		if (StringUtils.isNotBlank(parameterEncoding)) {
			postMethod.getParams().setContentCharset(parameterEncoding);
		}

		postMethod.addParameters(httpInvocation.getParameters());

		return invoke(postMethod, httpInvocation);
	}

	/**
	 * HTTP 요청 결과를 HttpInvocation 객체에 위임하여 결과를 가공해 리턴한다.
	 *
	 * @param <T>
	 * @param httpMethod
	 * @param httpInvocation
	 * @return
	 */
	public <T> T invoke(HttpMethod httpMethod, HttpInvocation<T> httpInvocation) {
		populateCookiePolicy(httpMethod);
		populateRequestHeaders(httpMethod, httpInvocation);

		try {
			httpClient.executeMethod(httpMethod);

			processOnResponses(httpMethod, httpInvocation);
		} catch (Exception ex) {
			httpInvocation.onException(ex);
		} finally {
			httpMethod.releaseConnection();
		}

		return httpInvocation.getReturnValue();
	}

	protected <T> void populateRequestHeaders(HttpMethod httpMethod, HttpInvocation<T> httpInvocation) {
		List<Header> requestHeaders = httpInvocation.getRequestHeaders();
		if (!CollectionUtils.isEmpty(requestHeaders)) {
			for (Header header : requestHeaders) {
				httpMethod.addRequestHeader(header);
			}
		}
	}

	protected void populateCookiePolicy(HttpMethod httpMethod) {
		httpMethod.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
	}

	protected <T> void processOnResponses(HttpMethod httpMethod, HttpInvocation<T> httpInvocation) throws Exception {
		int statusCode = httpMethod.getStatusCode();
		httpInvocation.onResponseStatus(statusCode);

		Header[] responseHeaders = httpMethod.getResponseHeaders();
		if (ArrayUtils.isNotEmpty(responseHeaders)) {
			for (Header header : responseHeaders) {
				httpInvocation.onResponseHeader(header.getName(), header.getValue());
			}
		}

		httpInvocation.onResponseBody(httpMethod.getResponseBodyAsStream());
	}
}
