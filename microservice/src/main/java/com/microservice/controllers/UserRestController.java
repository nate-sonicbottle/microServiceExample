package com.microservice.controllers;

import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.exceptions.ApplicationException;
import com.microservice.model.CreateUserRequest;
import com.microservice.model.UserDTO;
import com.microservice.response.WebResponse;
import com.microservice.service.UserService;

@RestController
@RequestMapping("/services/v1/user")
public class UserRestController {

	private static Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.PUT)
	@ResponseStatus(code=HttpStatus.CREATED)
	@ResponseBody
	public WebResponse<UserDTO> createUser(@Valid @RequestBody final CreateUserRequest createUserRequest) throws ApplicationException {
		LOGGER.debug("Entering createUser {}", createUserRequest);

		UserDTO userDTO = userService.createNewUser(createUserRequest);

		LOGGER.debug("Exiting createUser {}", userDTO);
		return new WebResponse<UserDTO>(userDTO);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	@ResponseBody
	public void deleteUser(@PathVariable(name = "id", required = true) final Long userId) throws ApplicationException {
		LOGGER.debug("Entering deleteUser {}", userId);

		userService.deleteUser(userId);

		LOGGER.debug("Exiting deleteUser");
	}

	@RequestMapping(path="/{id}", method=RequestMethod.POST)
	@ResponseBody
	public WebResponse<UserDTO> updateUser(@Valid @RequestBody final UserDTO userDTO,
			@PathVariable(name = "id", required = true) final Long userId) throws ApplicationException {

		LOGGER.debug("Entering updateUser {} {}", userDTO, userId);
		
		userDTO.setUserId(userId);
		userService.updateUser(userDTO);
		
		LOGGER.debug("Exiting updateUser {}", userDTO);
		return new WebResponse<UserDTO>(userDTO);
	}
	
	@RequestMapping(path="/{id}", method=RequestMethod.GET)
	@ResponseBody
	public WebResponse<UserDTO> findUserById(@PathVariable(name="id", required=true) final Long userId) throws ApplicationException {
		
		LOGGER.debug("Entering findUserById {}", userId);

		UserDTO response = userService.findUser(userId);
		
		LOGGER.debug("Exiting findUserById {}", response);
		return new WebResponse<UserDTO>(response);
	}

	@RequestMapping(path = "/search", method = RequestMethod.GET)
	@ResponseBody
	public WebResponse<Set<UserDTO>> SearchForUser(@RequestParam(name = "country") final String country) throws ApplicationException {
		LOGGER.debug("Entering findUser");
	
		Set<UserDTO> response = userService.findUsersByCountry(country);

		LOGGER.debug("Exiting findUser");
		return new WebResponse<Set<UserDTO>>(response);
	}
}
