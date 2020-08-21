package com.akm.neo.jwtauthentication.service;

import com.akm.neo.jwtauthentication.model.User;

public interface UserInfoService {

	
	public String getUserEducation() throws Exception;

	public User getLoggedInUserBean() throws Exception;

}
