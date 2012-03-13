package controllers;

public class TagNotFoundException extends Exception {
	public TagNotFoundException() {
		super();
	}

	public TagNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public TagNotFoundException(String arg0) {
		super(arg0);
	}

	public TagNotFoundException(Throwable arg0) {
		super(arg0);
	}
}
