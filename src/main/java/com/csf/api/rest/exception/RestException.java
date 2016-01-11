package com.csf.api.rest.exception;

import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
public final class RestException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public RestException(String message) {
		super(message);
	}
	
}
