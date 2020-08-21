package com.akm.neo.jwtauthentication.bean;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetailsBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("first_name")
	@Pattern(regexp ="([a-zA-Z]){2,16}", message = "invalid firstName")
	private String firstName;
	
	@JsonProperty("last_name")
	@Pattern(regexp ="([a-zA-Z]){2,16}", message = "invalid lastName")
	private String LastName;
	
	@JsonProperty("email")
	@Pattern(regexp ="^([A-Za-z0-9])(([.])?[0-9a-z])*[@]([a-z])+([.]([a-z])+){1,3}", message = "invalid email")
	private String email;
	
	@JsonProperty("gender")
	private String gender;

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	@Override
	public String toString() {
		return "UserDetailsBean [firstName=" + firstName + ", LastName=" + LastName + ", email=" + email + ", gender="
				+ gender + "]";
	}
	
}
