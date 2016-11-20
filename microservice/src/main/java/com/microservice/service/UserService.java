package com.microservice.service;

import java.util.Set;

import com.microservice.exceptions.ApplicationException;
import com.microservice.model.CreateUserRequest;
import com.microservice.model.UserDTO;

public interface UserService {

	UserDTO createNewUser(CreateUserRequest createUserRequest) throws ApplicationException;

	void deleteUser(Long userId) throws ApplicationException;

	UserDTO updateUser(UserDTO userDTO) throws ApplicationException;

	UserDTO findUser(String email) throws ApplicationException;

	UserDTO findUser(Long userId) throws ApplicationException;

	Set<UserDTO> findUsersByCountry(String country) throws ApplicationException;

}
