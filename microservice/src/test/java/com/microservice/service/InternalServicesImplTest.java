package com.microservice.service;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jms.core.JmsTemplate;

import com.microservice.model.UserDTO;
import com.microservice.model.Event.EventType;

@RunWith(MockitoJUnitRunner.class)
public class InternalServicesImplTest {
	
	@Mock
	private JmsTemplate jmsTemplate;
	
	@InjectMocks
	private InternalServicesImpl internalServicesImpl;
	
	private UserDTO input;

	@Before
	public void setUp() throws Exception {
		// Global Given 
		input = new UserDTO(2l, "John", "Smith", "JSmith", "pa55word", "jsmith@example.com", "england");
	}

	@Test
	public void thatTriggerUserUpdatedEventRaisesSuccessfully() {
		// When
		internalServicesImpl.triggerUserUpdatedEvent(input);
		
		// Then
		verify(jmsTemplate, times(1)).convertAndSend("USER_UPDATED", input);
	}
	
	@Test(expected=NullPointerException.class)
	public void thatTriggerUserUpdatedEventThrowsErrorWhenRecievedNull() {
		// When
		internalServicesImpl.triggerUserUpdatedEvent(null);
		
		// Then
		verify(jmsTemplate, never()).convertAndSend("USER_UPDATED", input);
	}
	
	@Test
	public void thatTriggerUserDeletedEventWorkSuccessfulOnCorrectInput() {
		// When
		internalServicesImpl.triggerUserDeletedEvent(input.getUserId());
		
		// Then
		verify(jmsTemplate, times(1)).convertAndSend("USER_DELETED", input.getUserId());
	}

}
