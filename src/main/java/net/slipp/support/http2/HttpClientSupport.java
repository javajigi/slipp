package net.slipp.support.http2;

public abstract class HttpClientSupport {
	private HttpClientTemplate httpClientTemplate;

	public void setBaseUrl(String baseUrl) {
		setHttpClientTemplate(new HttpClientTemplate(baseUrl));
	}

	public void setHttpClientTemplate(HttpClientTemplate httpClientTemplate) {
		this.httpClientTemplate = httpClientTemplate;
	}

	public HttpClientTemplate getHttpClientTemplate() {
		return httpClientTemplate;
	}
}
