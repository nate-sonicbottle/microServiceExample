package com.microservice.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Set;

import org.dozer.DozerBeanMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Sets;
import com.microservice.data.User;
import com.microservice.data.UserDAO;
import com.microservice.exceptions.EmailAlreadyExistsException;
import com.microservice.exceptions.UserNotFound;
import com.microservice.model.CreateUserRequest;
import com.microservice.model.UserDTO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

	@Mock
	private UserDAO userDAO;
	
	@Mock
	private InternalServices internalServices;
	
	@InjectMocks
	private UserServiceImpl userServiceImpl;
	
	@Autowired
	private DozerBeanMapper dozerBeanMapper;
	
	private UserDTO input;
	private User user1;

	@Before
	public void setUp() {
		userServiceImpl.setDozerBeanMapper(dozerBeanMapper);
		
		// Global Given
		input = new UserDTO(2l, "John", "Smith", "JSmith", "pa55word", "jsmith@example.com", "england");
		user1 = new User(2l, "John", "Smith", "JSmith", "pa55word", "jsmith@example.com", "england", 1);
	}
	
	@Test
	public void thatCreateNewUserIsSuccessfulWithCorrectData() throws Exception {
		// Given
		CreateUserRequest createUserRequest= new CreateUserRequest("John", "Smith", "JSmith", "pa55word", "jsmith@example.com", "england");
		
		// When
		UserDTO result = userServiceImpl.createNewUser(createUserRequest);

		// Then
		ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
		verify(userDAO, times(1)).save(argument.capture());
		
		assertEquals(input.getFirstName(), argument.getValue().getFirstName());
	}
	
	@Test
	public void thatCreateNewUserTriggersEventWithCorrectData() throws Exception {
		// Given
		CreateUserRequest createUserRequest= new CreateUserRequest("John", "Smith", "JSmith", "pa55word", "jsmith@example.com", "england");
		
		// When
		UserDTO result = userServiceImpl.createNewUser(createUserRequest);

		// Then
		ArgumentCaptor<UserDTO> argument = ArgumentCaptor.forClass(UserDTO.class);
		verify(internalServices, times(1)).triggerUserCreatedEvent(argument.capture());
		
		assertEquals(input.getLastName(), argument.getValue().getLastName());
	}
	

	@Test(expected=EmailAlreadyExistsException.class)
	public void thatCreateNewUserThrowsEmailAlreadyExists() throws Exception {
		// Given 
		CreateUserRequest createUserRequest= new CreateUserRequest("John", "Smith", "JSmith", "pa55word", "jsmith@example.com", "england");
		when(userDAO.findByEmail(input.getEmail())).thenReturn(user1);
		
		// When
		userServiceImpl.createNewUser(createUserRequest);
	}
	
	@Test(expected=EmailAlreadyExistsException.class)
	public void thatCreateNewUserDoesNotTriggerEventOnError() throws Exception {
		// Given 
		CreateUserRequest createUserRequest= new CreateUserRequest("John", "Smith", "JSmith", "pa55word", "jsmith@example.com", "england");
		when(userDAO.findByEmail(input.getEmail())).thenReturn(user1);
		
		// When
		userServiceImpl.createNewUser(createUserRequest);
		
		verify(internalServices, never()).triggerUserCreatedEvent(anyObject());
	}
	
	@Test
	public void thatDeleteUserIsSuccessfulWithValidId() throws Exception {
		// Given
		when(userDAO.findByUserId(input.getUserId())).thenReturn(user1);
		
		// When
		userServiceImpl.deleteUser(input.getUserId());
		
		// Then
		verify(userDAO).delete(user1);
	}
	
	@Test
	public void thatDeleteUserTriggerEventWithCorrectData() throws Exception {
		// Given
		when(userDAO.findByUserId(input.getUserId())).thenReturn(user1);
		
		// When
		userServiceImpl.deleteUser(input.getUserId());
		
		// Then
		verify(internalServices).triggerUserDeletedEvent(input.getUserId());
	}
	
	
	@Test(expected=UserNotFound.class)
	public void thatDeleteUserFailsWhenUserNotFound() throws Exception {
		// Given
		when(userDAO.findByUserId(input.getUserId())).thenReturn(null);
		
		// When
		userServiceImpl.deleteUser(input.getUserId());
	}
	
	@Test
	public void thatUpdateUserIsSuccessfulWithCorrectData() throws Exception {
		// Given 
		input.setFirstName("NotJohn");
		when(userDAO.findByUserId(input.getUserId())).thenReturn(user1);
		
		// When
		userServiceImpl.updateUser(input);
		
		// Then
		ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
		verify(userDAO, times(1)).save(argument.capture());
		
		assertEquals(input.getFirstName(), argument.getValue().getFirstName());
		
	}
	
	@Test
	public void thatUpdateUserTriggerEventWithCorrectData() throws Exception {
		// Given 
		input.setFirstName("NotJohn");
		when(userDAO.findByUserId(input.getUserId())).thenReturn(user1);
		
		// When
		userServiceImpl.updateUser(input);
		
		// Then
		verify(internalServices, times(1)).triggerUserUpdatedEvent(input);
		
	}
	
	@Test(expected=UserNotFound.class)
	public void thatUpdateUserFailsWhenUserNotFound() throws Exception {
		// Given
		when(userDAO.findByUserId(input.getUserId())).thenReturn(null);
		
		// When
		userServiceImpl.updateUser(input);
	}
	
	@Test
	public void thatSearchUserIsSuccessfulWithCorrectSearch() throws Exception {
		// Given
		String searchInput = "england";
		Set<User> users = Sets.newHashSet(user1);
		when(userDAO.findByCountry(searchInput)).thenReturn(users);
		
		// When
		Set<UserDTO> result = userServiceImpl.findUsersByCountry(searchInput);
	}
	
}
