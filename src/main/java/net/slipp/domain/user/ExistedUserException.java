package net.slipp.domain.user;

public class ExistedUserException extends Exception {
	private static final long serialVersionUID = -989452880795135125L;

	public ExistedUserException() {
		super();
	}

	public ExistedUserException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ExistedUserException(String arg0) {
		super(arg0);
	}

	public ExistedUserException(Throwable arg0) {
		super(arg0);
	}
}
