package com.apollo.shuttershare.common;

import org.springframework.http.HttpStatus;

/**
 * @author Daniel
 */
public class WebException extends ShutterShareException {
	public WebException() {
	}

	public WebException(String message) {
		super(message);
	}

	public WebException(String message, Throwable cause) {
		super(message, cause);
	}

	public WebException(Throwable cause) {
		super(cause);
	}

    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }
}
