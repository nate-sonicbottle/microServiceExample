package com.microservice.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;
import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservice.data.User;
import com.microservice.data.UserDAO;
import com.microservice.exceptions.ApplicationException;
import com.microservice.exceptions.EmailAlreadyExistsException;
import com.microservice.exceptions.GenericError;
import com.microservice.exceptions.UserNotFound;
import com.microservice.model.CreateUserRequest;
import com.microservice.model.UserDTO;
import com.microservice.utils.NullAwareBeanUtils;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	
	private static final NullAwareBeanUtils nullAwareBeanUtils = new NullAwareBeanUtils();
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private InternalServices internalServices;
	
	@Autowired
	private DozerBeanMapper dozerBeanMapper;

	@Override
	public UserDTO createNewUser(final CreateUserRequest createUserRequest) throws ApplicationException {
		LOGGER.debug("entering createNewUser {}", createUserRequest);

		Validate.notNull(createUserRequest);

		User existingUser = userDAO.findByEmail(createUserRequest.getEmail());
		if (existingUser != null) {
			throw new EmailAlreadyExistsException();
		}
		
		User userEntity = dozerBeanMapper.map(createUserRequest, User.class);

		userDAO.save(userEntity);
		
		UserDTO userDTO = dozerBeanMapper.map(userEntity, UserDTO.class);
		userDTO.setUserId(userEntity.getUserId());
		
		internalServices.triggerUserCreatedEvent(userDTO);

		LOGGER.debug("exiting createNewUser {}", userDTO);
		return userDTO;
	}

	@Override
	public void deleteUser(final Long userId) throws ApplicationException {
		LOGGER.debug("Entering deleteUser {}", userId);

		Validate.notNull(userId);

		User user = findByUserId(userId);
		
		userDAO.delete(user);
		
		internalServices.triggerUserDeletedEvent(userId);

		LOGGER.debug("Exiting deleteUser");
	}

	@Override
	public UserDTO updateUser(final UserDTO userDTO) throws ApplicationException  {
		LOGGER.debug("Entering updateUser {}", userDTO);
		
		Validate.notNull(userDTO);

		User user = findByUserId(userDTO.getUserId());
		
		try {
			nullAwareBeanUtils.copyProperties(user, userDTO);
		} catch (IllegalAccessException | InvocationTargetException e) {
			LOGGER.error("Error copy updates from UserDTO to User Entity", e);
			throw new GenericError(e);
		}
		
		userDAO.save(user);
		
		internalServices.triggerUserUpdatedEvent(userDTO);
		
		LOGGER.debug("Exiting updateUser {}", userDTO);
		return userDTO;
	}
	
	@Override
	public UserDTO findUser(final Long userId) throws ApplicationException {
		LOGGER.debug("Entering findUser {}", userId);

		Validate.notNull(userId);
		
		User user = findByUserId(userId);
		
		UserDTO response = dozerBeanMapper.map(user, UserDTO.class);
		
		LOGGER.debug("Exiting findUser {}", response);
		return response;
	}

	@Override
	public UserDTO findUser(final String email) throws ApplicationException  {
		LOGGER.debug("Entering findUser {}", email);

		Validate.notBlank(email);
		
		
		LOGGER.debug("Exiting findUser");
		return null;
	}
	
	@Override
	public Set<UserDTO> findUsersByCountry(final String country) throws ApplicationException {
		LOGGER.debug("Entering findUsersByCountry {}", country);
		
		Validate.notBlank(country);
	
		Set<User> usersSet = userDAO.findByCountry(country);
		
		Set<UserDTO> response = usersSet
				.stream()
				.map(from -> dozerBeanMapper.map(from, UserDTO.class))
				.collect(Collectors.toSet());
		
		LOGGER.debug("Exiting findUser {}", usersSet);
		return response;
	}
	
	/**
	 * Searches for User Entity otherwise throws UserNotFound exception
	 * 
	 * @param userId 
	 * @return User Entity
	 * @throws UserNotFound - Thrown if user not found
	 */
	private User findByUserId(final Long userId) throws UserNotFound {
		
		User user = userDAO.findByUserId(userId);
		if (user == null) {
			throw new UserNotFound();
		}
		return user;
	}

	public void setDozerBeanMapper(DozerBeanMapper dozerBeanMapper) {
		this.dozerBeanMapper = dozerBeanMapper;
	}
}
