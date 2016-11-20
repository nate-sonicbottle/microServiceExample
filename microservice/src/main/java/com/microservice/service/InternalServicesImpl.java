package com.microservice.service;

import org.apache.commons.lang3.Validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.microservice.model.Event;
import com.microservice.model.Event.EventType;
import com.microservice.model.UserDTO;

/**
 * This class triggers events that will be send via the messaging queue to other microservices
 * 
 * @author Nathan
 */
@Service
public class InternalServicesImpl implements InternalServices {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Override
	public void triggerUserUpdatedEvent(final UserDTO userDTO) {
		LOGGER.debug("entering triggerUserUpdatedEvent {}", userDTO);
		
		Validate.notNull(userDTO);
		
		Event<UserDTO> event = new Event<>(EventType.USER_UPDATED, userDTO);
		publishEvent(event);
		
		LOGGER.debug("exiting triggerUserUpdatedEvent");
	}
	
	@Override
	public void triggerUserCreatedEvent(final UserDTO userDTO) {
		LOGGER.debug("entering triggerUserCreatedEvent {}", userDTO);
		
		Validate.notNull(userDTO);
		
		Event<UserDTO> event = new Event<>(EventType.USER_CREATED, userDTO);
		publishEvent(event);
		
		LOGGER.debug("exiting triggerUserCreatedEvent");
	}
	
	@Override
	public void triggerUserDeletedEvent(final Long userId) {
		LOGGER.debug("entering triggerUserDeletedEvent {}", userId);
		
		Validate.notNull(userId);
		
		Event<Long> event = new Event<>(EventType.USER_DELETED, userId);
		publishEvent(event);
		
		LOGGER.debug("exiting triggerUserDeletedEvent");
	}
	
	@SuppressWarnings("rawtypes")
	private void publishEvent(Event event) {
		LOGGER.debug("Sending Event {}", event);

        jmsTemplate.convertAndSend(event.getEventType().toString(), event.getData());
	}
}
