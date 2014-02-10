package net.uiucapp.apollo.common;

/**
 * @author Daniel
 */
public class ApolloException extends RuntimeException {
	public ApolloException() {
	}

	public ApolloException(String message) {
		super(message);
	}

	public ApolloException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApolloException(Throwable cause) {
		super(cause);
	}
}
