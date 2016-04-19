package com.woniu.base.web.exception;

public class ApplicationException extends RuntimeException {
	private final ErrorCode errorCode;

	public ApplicationException(ErrorCode errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public ApplicationException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public ApplicationException(ErrorCode errorCode) {
		super(errorCode.message);
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}
}
