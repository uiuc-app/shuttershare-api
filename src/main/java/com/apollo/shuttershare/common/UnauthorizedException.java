package com.apollo.shuttershare.common;

import org.springframework.http.HttpStatus;

/**
 * @author Daniel
 */
public class UnauthorizedException extends WebException {
	public UnauthorizedException() {
	}

	public UnauthorizedException(String message) {
		super(message);
	}

	public UnauthorizedException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnauthorizedException(Throwable cause) {
		super(cause);
	}

    public int getStatusCode() {
        return HttpStatus.UNAUTHORIZED.value();
    }
}
