package net.uiucapp.apollo.common;

/**
 * @author Daniel
 */
public class NotImplementedException extends ApolloException {
	public NotImplementedException(Throwable cause) {
		super(cause);
	}

	public NotImplementedException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotImplementedException(String message) {
		super(message);
	}

	public NotImplementedException() {
	}
}
