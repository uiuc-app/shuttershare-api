package com.apollo.shuttershare.common;

/**
 * @author Daniel
 */
public class NotImplementedException extends ShutterShareException {
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
