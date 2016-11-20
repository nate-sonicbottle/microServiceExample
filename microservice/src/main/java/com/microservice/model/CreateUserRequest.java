package com.microservice.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Arrays;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.context.annotation.Description;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateUserRequest implements Serializable{
	
	private static final long serialVersionUID = -6188117568120235729L;
	
	@ApiModelProperty(notes = "The first name of the user", required=true)
	@Size(min=1, max=255)
	@NotNull
	private String firstName;
	
	@ApiModelProperty(notes = "The last name of the user", required=true)
	@Size(min=1, max=255)
	@NotNull
	private String lastName;
	
	@ApiModelProperty(notes = "The nickName of the user", required=true)
	@Size(min=1, max=255)
	@NotNull
	private String nickName;
	
	@ApiModelProperty(notes = "User's Password", required=true)
	@Size(min=1, max=255)
	@NotNull
	private String password;
	
	@ApiModelProperty(notes = "Email address of user", required=true)
	@Size(min=1, max=255)
	@NotNull
	@Pattern(regexp="[\\w-]+@([\\w-]+\\.)+[\\w-]+")
	private String email;
	
	@ApiModelProperty(notes = "User's country", required=false)
	@Size(min=1, max=255)
	private String country;
	
	public CreateUserRequest() {}

	public CreateUserRequest(String firstName, String lastName, String nickName, String password, String email, String country) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.nickName = nickName;
		this.password = password;
		this.email = email;
		this.country = country;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this, Arrays.asList("password"));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((nickName == null) ? 0 : nickName.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreateUserRequest other = (CreateUserRequest) obj;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (nickName == null) {
			if (other.nickName != null)
				return false;
		} else if (!nickName.equals(other.nickName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}
}