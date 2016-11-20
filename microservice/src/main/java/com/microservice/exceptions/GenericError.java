package com.microservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Server has experienced an Error") 
public class GenericError extends ApplicationException {

	private static final long serialVersionUID = 8049302923249020918L;

	public GenericError(Throwable throwable) {
		super(throwable);
	}
}
