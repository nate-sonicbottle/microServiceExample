package com.microservice.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.exceptions.UserNotFound;
import com.microservice.model.CreateUserRequest;
import com.microservice.model.UserDTO;
import com.microservice.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRestControllerTest {
	
	private static final String BASE_URL = "/services/v1/user";

	@Autowired
	private ObjectMapper objectMapper;

	@Mock
	private UserService userService;
	
	@InjectMocks
	private UserRestController userController;
	
	private MockMvc mockMvc;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
	}

	@Test
	public void thatCreateUserIsSuccessfulWithCorrectData() throws Exception {
		// Given
		UserDTO userDTO = new UserDTO(2l, "John", "Smith", "JSmith", "pa55word", "jsmith@example.com", "england");
		CreateUserRequest createUserRequest= new CreateUserRequest("John", "Smith", "JSmith", "pa55word", "jsmith@example.com", "england");
		String input = objectMapper.writeValueAsString(createUserRequest);
		when(userService.createNewUser(createUserRequest)).thenReturn(userDTO);
		
		// When & Then
		mockMvc.perform(put(BASE_URL).content(input).contentType(MediaType.APPLICATION_JSON)).andDo(print())
		.andExpect(status().isCreated()).andExpect(jsonPath("$.data.firstName", is("John")));
		
		verify(userService, times(1)).createNewUser(createUserRequest);
	}
	
	@Test
	public void thatCreateUserThrowErrorOnInValidData() throws Exception {
		// Given
		CreateUserRequest createUserRequest= new CreateUserRequest("", "Smith", "JSmith", "pa55word", "jsmith@example.com", "england");
		String input = objectMapper.writeValueAsString(createUserRequest);
		
		// When & Then
		mockMvc.perform(put(BASE_URL).content(input).contentType(MediaType.APPLICATION_JSON)).andDo(print())
		.andExpect(status().isBadRequest());
		
		verify(userService, never()).createNewUser(createUserRequest);
	}
	
	@Test
	public void thatDeleteUserIsSuccessfulWithCorrectUserId() throws Exception {
		// Given
		Long input = new Long(2);
		
		// When & Then
		mockMvc.perform(delete(BASE_URL + "/" + input)).andDo(print()).andExpect(status().isNoContent());
		
		verify(userService, times(1)).deleteUser(input);
	}

	@Test
	public void thatDeleteUserIs404WhenUserDoesNotExist() throws Exception {
		// Given
		Long input = new Long(2);
		doThrow(new UserNotFound()).when(userService).deleteUser(input);

		// When & Then
		mockMvc.perform(delete(BASE_URL + "/" + input).contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isNotFound());
		
		verify(userService, times(1)).deleteUser(input);
	}

	@Ignore
	public void thatSearchUserFindsUser() throws Exception {

		// When & Then
		mockMvc.perform(get(BASE_URL + "/search?country=england").contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk());
	}
	
	
	@Test
	public void thatUpdateUserIsSuccessfulOnCorrectData() throws Exception {
		// Given
		UserDTO userDTO = new UserDTO(2l, "John", "Smith", "JSmith", "pa55word", "jsmith@example.com", "england");
		String input = objectMapper.writeValueAsString(userDTO);
		
		// When & Then
		mockMvc.perform(post(BASE_URL + "/" + userDTO.getUserId()).content(input).contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.data.firstName", is("John")));
		
		verify(userService, times(1)).updateUser(userDTO);
	}
	
	@Test
	public void thatUpdateUserThrows404OnMissingUser() throws Exception {
		// Given
		UserDTO userDTO = new UserDTO(2l, "John", "Smith", "JSmith", "pa55word", "jsmith@example.com", "england");
		String input = objectMapper.writeValueAsString(userDTO);
		
		doThrow(new UserNotFound()).when(userService).updateUser(userDTO);
		
		// When & Then 
		mockMvc.perform(post(BASE_URL + "/" + userDTO.getUserId()).content(input).contentType(MediaType.APPLICATION_JSON)).andDo(print())
		.andExpect(status().isNotFound());
	}
	
}
