package com.microservice.exceptions;

public abstract class ApplicationException extends Exception {

	private static final long serialVersionUID = 1169314314006670592L;

	public ApplicationException() {
		super();
	}
	
	public ApplicationException(Throwable throwable) {
		super(throwable);
	}
}
