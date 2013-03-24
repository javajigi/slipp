package net.slipp.support.http;

public class HttpInvocationException extends RuntimeException {
	private static final long serialVersionUID = -1099056566202417250L;

	public HttpInvocationException(String message, Throwable cause) {
		super(message, cause);
	}

	public HttpInvocationException(String message) {
		super(message);
	}
}
