package com.microservice.model;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 
 * @author Nathan
 *
 * @param <T> Class Type will be wrapped by the event class
 */
public class Event<T> implements Serializable {
	
	private static final long serialVersionUID = 6446897719803556212L;
	
	public enum EventType { USER_CREATED, USER_UPDATED, USER_DELETED};
	private EventType eventType;
	
	private T data;

	public Event() {}
	
	public Event(EventType eventType, T data) {
		this.eventType = eventType;
		this.data = data;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this, Arrays.asList("password"));
	}
}
