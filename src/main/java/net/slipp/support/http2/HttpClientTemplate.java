package net.slipp.support.http2;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpClientTemplate {
	private HttpClient httpClient;
	private String baseUrl;

	public HttpClientTemplate(String baseUrl) {
		this.baseUrl = baseUrl;
		httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(3 * 1000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(3 * 1000);
	}

	public Object get(HttpInvocation invocation) throws HttpInvocationException {
		GetMethod method = new GetMethod(baseUrl + invocation.getUri());
		method.setQueryString(invocation.getParameters());
		return invoke(invocation, method);
	}

	public Object post(HttpInvocation invocation) throws HttpInvocationException {
		PostMethod method = new PostMethod(baseUrl + invocation.getUri());
		if (invocation.getParameterEncoding() != null && invocation.getParameterEncoding().length() > 0) {
			HttpMethodParams params = method.getParams();
			params.setContentCharset(invocation.getParameterEncoding());
		}
		method.addParameters(invocation.getParameters());
		return invoke(invocation, method);
	}

	private Object invoke(HttpInvocation invocation, HttpMethod method) {
		try {
			method.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
			for (Header each : invocation.getRequestHeaders()) {
				method.addRequestHeader(each);
			}
			if (invocation.getTimeout() != 0) {
				httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(invocation.getTimeout());
				httpClient.getHttpConnectionManager().getParams().setSoTimeout(invocation.getTimeout());
			}
			httpClient.executeMethod(method);
			invocation.onResponseStatus(method.getStatusCode());
			for (Header each : method.getResponseHeaders()) {
				invocation.onResponseHeader(each.getName(), each.getValue());
			}
			invocation.onResponseBody(method.getResponseBodyAsStream());
		} catch (Exception e) {
			invocation.onException(e);
		} finally {
			method.releaseConnection();
		}
		return invocation.getReturnValue();
	}
}
