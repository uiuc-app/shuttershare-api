package com.apollo.shuttershare.common;

/**
 * @author Daniel
 */
public class ShutterShareException extends RuntimeException {
	public ShutterShareException() {
	}

	public ShutterShareException(String message) {
		super(message);
	}

	public ShutterShareException(String message, Throwable cause) {
		super(message, cause);
	}

	public ShutterShareException(Throwable cause) {
		super(cause);
	}
}
