package com.akm.neo.jwtauthentication.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akm.neo.jwtauthentication.service.UserInfoService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserInfoController extends UserValidation {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoController.class);

	@Autowired
	private UserInfoService userInfoService;

	
	//Get logged in user's education
	@GetMapping(value = "/api/test/getEducation", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getUserEducation() throws Exception {
		LOGGER.trace("Starting getUserEducation() from UserInfoController");
		ResponseEntity<?> responseEntity = null;
		String jsonString = userInfoService.getUserEducation();
		if(jsonString != null){
			responseEntity = ResponseEntity.ok(jsonString);
		} else
			responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		LOGGER.trace("Exiting getUserEducation() from UserInfoController");
		return responseEntity;
	}

	

	
}
