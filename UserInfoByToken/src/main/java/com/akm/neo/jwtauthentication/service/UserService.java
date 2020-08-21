package com.akm.neo.jwtauthentication.service;

public interface UserService {

	public String addUser(String dashboardRequest) throws Exception;

	public String getAllActiveUsers() throws Exception;

	public String editUser(String dashBoardRequest, String uname) throws Exception;

	public String deleteUser(String uname) throws Exception;

	public String softDelete(String uname) throws Exception;

	public String getAllUsers(String dashBoardRequest) throws Exception;

	public String searchByEmail(String email) throws Exception;

	public String sortBy(String anything) throws Exception;

}
