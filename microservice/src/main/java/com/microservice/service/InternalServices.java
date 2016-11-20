package com.microservice.service;

import com.microservice.model.UserDTO;

public interface InternalServices {

	public abstract void triggerUserUpdatedEvent(UserDTO userDTO);

	public abstract void triggerUserCreatedEvent(UserDTO userDTO);

	public abstract void triggerUserDeletedEvent(Long userId);

}