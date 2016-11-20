package com.microservice.data;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.model.UserDTO;


public interface UserDAO extends JpaRepository<User, Long> {

	User findByEmail(String email);
	
	User findByUserId(Long userId);

	Set<User> findByCountry(String country);
	
}
