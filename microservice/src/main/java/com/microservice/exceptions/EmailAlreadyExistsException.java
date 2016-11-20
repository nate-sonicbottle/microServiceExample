package com.microservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason="Cannot create account email already exists") 
public class EmailAlreadyExistsException extends ApplicationException {

	private static final long serialVersionUID = 7899805379872825500L;

}
