package com.woniu.base.web.exception;

public class ErrorCode {
	public static final ErrorCode INTERNAL_ERROR = new ErrorCode(-1, "系统内部错误");

	public static final ErrorCode VALIDATION_ERROR = new ErrorCode(-2, "数据校验错误");

	public static final ErrorCode BUSINESS_ERROR = new ErrorCode(-3, "业务异常");

	public final int code;
	public final String message;
	
	public ErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}
}
