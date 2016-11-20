package com.microservice.response;

import java.io.Serializable;

public class WebResponse<T> implements Serializable{

	private static final long serialVersionUID = 2335637812954269910L;
	
	int code;
	T data;

	public WebResponse(int code, T data) {
		this.code = code;
		this.data = data;
	}

	public WebResponse(T data) {
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
