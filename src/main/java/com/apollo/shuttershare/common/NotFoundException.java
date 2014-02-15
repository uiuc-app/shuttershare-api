package com.apollo.shuttershare.common;

import org.springframework.http.HttpStatus;

/**
 * @author Daniel
 */
public class NotFoundException extends ShutterShareException {
	public NotFoundException() {
	}

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundException(Throwable cause) {
		super(cause);
	}

    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }
}
