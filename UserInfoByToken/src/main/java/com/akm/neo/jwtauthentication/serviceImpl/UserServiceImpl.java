package com.akm.neo.jwtauthentication.serviceImpl;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.akm.neo.jwtauthentication.bean.DashboardResponse;
import com.akm.neo.jwtauthentication.bean.UserBean;
import com.akm.neo.jwtauthentication.bean.UserDetailsBean;
import com.akm.neo.jwtauthentication.bean.UserEducationBean;
import com.akm.neo.jwtauthentication.bean.UserEmploymentBean;
import com.akm.neo.jwtauthentication.common.CommonConstants;
import com.akm.neo.jwtauthentication.model.Role;
import com.akm.neo.jwtauthentication.model.RoleName;
import com.akm.neo.jwtauthentication.model.User;
import com.akm.neo.jwtauthentication.model.UserEducation;
import com.akm.neo.jwtauthentication.model.UserEmployment;
import com.akm.neo.jwtauthentication.model.UserPersonal;
import com.akm.neo.jwtauthentication.repository.RoleRepository;
import com.akm.neo.jwtauthentication.repository.UserRepository;
import com.akm.neo.jwtauthentication.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final String USER_DETAILS = "user_info";
	private static final Integer DEFAULT_PAGE_SIZE = 2;
	private static final Integer DEFAULT_PAGE_NUMBER = 1;

	public final DateTimeFormatter yyyy_mm_dd = DateTimeFormatter.ofPattern("yyyy-mm-dd");

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Override
	public String addUser(String dashboardRequest) throws Exception {
		LOGGER.trace("Starting addUser() from UserServiceImpl with arguments:: dashboardRequest: "+dashboardRequest);
		String returnValue = null;
		String errorMsg = null;
		Set<Role> roles = new HashSet<>();

		DashboardResponse dashboardResponse = new DashboardResponse();
		try {
			UserBean request = MAPPER.readValue(dashboardRequest, UserBean.class);
			System.out.println(request);
			User user = new User();
			user.setActive(true);
			user.setUsername(request.getUserName() != null ? request.getUserName() : null);
			user.setPassword(request.getUserPassword() != null ? encoder.encode(request.getUserPassword()) : null);
			Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
			roles.add(userRole);
			user.setRoles(roles);
			

			if(request.getUserDetails() != null) {
				UserPersonal userDetails = new UserPersonal();
				userDetails.setUser(user);
				userDetails.setFirstName(request.getUserDetails().getFirstName() != null ? request.getUserDetails().getFirstName() : null);
				userDetails.setLastName(request.getUserDetails().getLastName() != null ? request.getUserDetails().getLastName() : null);
				//userDetails.setAddress(request.getUserDetails().getAddress());
				userDetails.setEmail(request.getUserDetails().getEmail() != null ? request.getUserDetails().getEmail() : null);
				userDetails.setGender(request.getUserDetails().getGender() != null ? request.getUserDetails().getGender() : null);
				user.setUserDetails(userDetails);
			}

			if(request.getUserEducationBean() != null) {
				UserEducation userEducation = new UserEducation();
				userEducation.setUser(user);
				userEducation.setCgpa(request.getUserEducationBean().getCgpa() != null ? Float.valueOf(request.getUserEducationBean().getCgpa()) : null);
				userEducation.setHscBoardName(request.getUserEducationBean().getHscBoardName() != null ? request.getUserEducationBean().getHscBoardName() : null);
				userEducation.setHscPercentage(request.getUserEducationBean().getHscPercentage() != null ? Float.valueOf(request.getUserEducationBean().getHscPercentage()) : null);
				userEducation.setSscBoardName(request.getUserEducationBean().getSscBoardName() != null ? request.getUserEducationBean().getSscBoardName() : null);
				userEducation.setSscPercentage(request.getUserEducationBean().getSscPercentage() != null ? Float.valueOf(request.getUserEducationBean().getSscPercentage()) : null);
				userEducation.setUniversityName(request.getUserEducationBean().getUniversityName() != null ? request.getUserEducationBean().getUniversityName() : null);
				user.setUserEducation(userEducation);
			}

			if(request.getUserEmploymentBean() != null) {
				List<UserEmploymentBean> uEmpBean = request.getUserEmploymentBean();
				List<UserEmployment> empList = new ArrayList<>();
				for(UserEmploymentBean empBean : uEmpBean) {
					UserEmployment userEmp = new UserEmployment();
					userEmp.setUser(user);
					userEmp.setCompanyLocation(empBean.getCompanyLocation() != null ? empBean.getCompanyLocation() : null);
					userEmp.setCompanyName(empBean.getCompanyName() != null ? empBean.getCompanyName() : null);

					if(empBean.getEndDate() != null) {
						Date d = new SimpleDateFormat("yyyy-dd-mm").parse(empBean.getEndDate());
						userEmp.setEndDate(d);
					} 
					if(empBean.getStartDate() != null) {
						Date d = new SimpleDateFormat("yyyy-dd-mm").parse(empBean.getStartDate());
						userEmp.setStartDate(d);
					} 

					userEmp.setTechnology(empBean.getTechnology() != null ? empBean.getTechnology() : null);
					empList.add(userEmp);
				}
				user.setUserEmployment(empList);
			}
			User user1 = this.userRepo.save(user);

			if(user1 != null) {
				dashboardResponse.setStatusCode(CommonConstants.SUCCESS);
				dashboardResponse.setResponseData(USER_DETAILS," User saved" );
			} else
				errorMsg = "No Records found for requested input.";
		} catch (Exception e) {
			errorMsg = "Following exception occur while fetching User Details.";
			LOGGER.error(errorMsg + "\n\r : "+e.getStackTrace());
			e.printStackTrace();
		}
		if(errorMsg != null){
			dashboardResponse.setStatusCode(CommonConstants.FAIL);
			dashboardResponse.setErrorMsg(errorMsg);
		}
		returnValue = MAPPER.writeValueAsString(dashboardResponse);
		LOGGER.trace("Exiting addUser() from UserServiceImpl with return:: returnValue: "+returnValue);
		return returnValue;
	}

	@Override
	public String getAllActiveUsers() throws Exception {
		LOGGER.trace("Starting getAllActiveUsers() from UserServiceImpl");
		String returnValue = null;
		String errorMsg = null;
		DashboardResponse dashboardResponse = new DashboardResponse();
		try {
			List<User> userDetailsList = this.userRepo.findActiveUser();
			List<UserBean> userList = new ArrayList<>();

			if(!(userDetailsList.isEmpty())) {

				for(User user :userDetailsList) {
					UserBean userBean = new UserBean();

					if(user.getUsername() != null)
						userBean.setUserName(user.getUsername());
					if(user.getPassword() != null)
						userBean.setUserPassword(user.getPassword());
					if(user.isActive())
						userBean.setActive(Boolean.toString(user.isActive()));

					if(user.getUserDetails() != null) {
						UserDetailsBean userDetailsBean = new UserDetailsBean();
						if(user.getUserDetails().getFirstName() != null)
							userDetailsBean.setFirstName(user.getUserDetails().getFirstName());
						if(user.getUserDetails().getLastName() != null)
							userDetailsBean.setLastName(user.getUserDetails().getLastName());
						if(user.getUserDetails().getEmail() != null)
							userDetailsBean.setEmail(user.getUserDetails().getEmail());
						if(user.getUserDetails().getGender() != null)
							userDetailsBean.setGender(user.getUserDetails().getGender());
						userBean.setUserDetails(userDetailsBean);
					}
					if(user.getUserEducation() != null) {
						UserEducationBean userEducationBean = new UserEducationBean();
						if(user.getUserEducation().getCgpa() != null)
							userEducationBean.setCgpa((user.getUserEducation().getCgpa()).toString());
						if(user.getUserEducation().getHscBoardName() != null)
							userEducationBean.setHscBoardName(user.getUserEducation().getHscBoardName());
						if(user.getUserEducation().getHscPercentage() != null)
							userEducationBean.setHscPercentage(user.getUserEducation().getHscPercentage().toString());
						if(user.getUserEducation().getSscBoardName() != null)
							userEducationBean.setSscBoardName(user.getUserEducation().getSscBoardName());
						if(user.getUserEducation().getSscPercentage() != null)
							userEducationBean.setSscPercentage(user.getUserEducation().getSscPercentage().toString());
						if(user.getUserEducation().getUniversityName() != null)
							userEducationBean.setUniversityName(user.getUserEducation().getUniversityName());
						userBean.setUserEducationBean(userEducationBean);
					}
					List<UserEmployment> userEmployment = user.getUserEmployment();
					List<UserEmploymentBean> userEmploymentList = new ArrayList<>();

					if(userEmployment != null ) {
						for(UserEmployment uEmp : userEmployment) {
							UserEmploymentBean userEmploymentBean = new UserEmploymentBean();
							if(uEmp.getCompanyLocation() != null)
								userEmploymentBean.setCompanyLocation(uEmp.getCompanyLocation());
							if(uEmp.getCompanyName() != null)
								userEmploymentBean.setCompanyName(uEmp.getCompanyName());
							if(uEmp.getEndDate() != null)
								userEmploymentBean.setEndDate(uEmp.getEndDate().toString());
							if(uEmp.getStartDate() != null)
								userEmploymentBean.setStartDate(uEmp.getStartDate().toString());
							if(uEmp.getTechnology() != null)
								userEmploymentBean.setTechnology(uEmp.getTechnology());
							userEmploymentList.add(userEmploymentBean);
						}
						userBean.setUserEmploymentBean(userEmploymentList);
					}
					userList.add(userBean);
				}
			}
			LOGGER.trace("USER_DETAILS_LIST:: "+userList);
			if(!userDetailsList.isEmpty()) {
				dashboardResponse.setStatusCode(CommonConstants.SUCCESS);
				dashboardResponse.setResponseData(USER_DETAILS, userList);
			} else
				errorMsg = "No Records found for requested input.";
		} catch (Exception e) {
			errorMsg = "Following exception occur while fetching User Details.";
			LOGGER.error(errorMsg + "\n\r : "+ e.getStackTrace());
			e.printStackTrace();
		}
		if(errorMsg != null){
			dashboardResponse.setStatusCode(CommonConstants.FAIL);
			dashboardResponse.setErrorMsg(errorMsg);
		}
		returnValue = MAPPER.writeValueAsString(dashboardResponse);
		LOGGER.trace("Exiting getAllActiveUsers() from UserServiceImpl with return:: returnValue: "+returnValue);
		return returnValue;
	}

	@Override
	public String deleteUser(String uname) throws Exception {
		LOGGER.trace("Starting deleteUser() from UserServiceImpl");
		String returnValue = null;
		String errorMsg = null;
		DashboardResponse dashboardResponse = new DashboardResponse();
		try {
			Optional<User> u = userRepo.findByUsername(uname);

			if(u.isPresent()) {
				this.userRepo.delete(u.get());;
				dashboardResponse.setStatusCode(CommonConstants.SUCCESS);
				dashboardResponse.setResponseData(USER_DETAILS, "User Deleted");
			} else
				errorMsg = "No User found for this ID";

		} catch (Exception e) {
			errorMsg = "Following exception occur while fetching User Details.";
			LOGGER.error(errorMsg + "\n\r : "+e.getStackTrace());
			e.printStackTrace();


		}
		if(errorMsg != null){
			dashboardResponse.setStatusCode(CommonConstants.FAIL);
			dashboardResponse.setErrorMsg(errorMsg);
		}
		returnValue = MAPPER.writeValueAsString(dashboardResponse);
		LOGGER.trace("Exiting deleteUser() from UserServiceImpl with return:: returnValue: "+returnValue);
		return returnValue;
	}

	@Override
	public String softDelete(String uname) throws Exception {
		LOGGER.trace("Starting deleteUser() from UserServiceImpl");
		String returnValue = null;
		String errorMsg = null;
		DashboardResponse dashboardResponse = new DashboardResponse();
		try {
			Optional<User> u = userRepo.findByUsername(uname);
			//Optional<User> user = userRepo.findById(id);

			if(u != null) {
				u.get().setActive(false);
				//user.get().setActive(false);
				//userRepo.save(user.get());
				userRepo.save(u.get());
				dashboardResponse.setStatusCode(CommonConstants.SUCCESS);
				dashboardResponse.setResponseData(USER_DETAILS, "User Deleted");
			} else
				errorMsg = "No User found for this ID";

		} catch (Exception e) {
			errorMsg = "Following exception occur while fetching User Details.";
			LOGGER.error(errorMsg + "\n\r : "+e.getStackTrace());
			e.printStackTrace();
		}
		if(errorMsg != null){
			dashboardResponse.setStatusCode(CommonConstants.FAIL);
			dashboardResponse.setErrorMsg(errorMsg);
		}
		returnValue = MAPPER.writeValueAsString(dashboardResponse);
		LOGGER.trace("Exiting deleteUser() from UserServiceImpl with return:: returnValue: "+returnValue);
		return returnValue;
	}

	@Override
	public String getAllUsers(String dashboardRequest) throws Exception {
		LOGGER.trace("Starting getAllUsers() from UserServiceImpl");
		String returnValue = null;
		String errorMsg = null;
		DashboardResponse dashboardResponse = new DashboardResponse();
		try {
			JsonNode request = MAPPER.readTree(dashboardRequest);
			Integer pageNumber=request.get("page_number") != null ? request.get("page_number").asInt() : DEFAULT_PAGE_NUMBER;
			Integer pageSize=request.get("page_size") != null ? request.get("page_size").asInt() : DEFAULT_PAGE_SIZE;

			//Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
			//Page<User> userDetailsList = this.userRepo.findAll(pageable);
			List<User> userDetailsList = this.userRepo.findAll();
			List<UserBean> userList = new ArrayList<>();

			if(!(userDetailsList.isEmpty())) {

				for(User user :userDetailsList) {
					UserBean userBean = new UserBean();

					if(user.getUsername() != null)
						userBean.setUserName(user.getUsername());
					if(user.getPassword() != null)
						userBean.setUserPassword(user.getPassword());
					if(user.isActive())
						userBean.setActive(Boolean.toString(user.isActive()));

					if(user.getUserDetails() != null) {
						UserDetailsBean userDetailsBean = new UserDetailsBean();
						if(user.getUserDetails().getFirstName() != null)
							userDetailsBean.setFirstName(user.getUserDetails().getFirstName());
						if(user.getUserDetails().getLastName() != null)
							userDetailsBean.setLastName(user.getUserDetails().getLastName());
						if(user.getUserDetails().getEmail() != null)
							userDetailsBean.setEmail(user.getUserDetails().getEmail());
						if(user.getUserDetails().getGender() != null)
							userDetailsBean.setGender(user.getUserDetails().getGender());
						userBean.setUserDetails(userDetailsBean);
					}


					if(user.getUserEducation() != null) {
						UserEducationBean userEducationBean = new UserEducationBean();
						if(user.getUserEducation().getCgpa() != null)
							userEducationBean.setCgpa((user.getUserEducation().getCgpa()).toString());
						if(user.getUserEducation().getHscBoardName() != null)
							userEducationBean.setHscBoardName(user.getUserEducation().getHscBoardName());
						if(user.getUserEducation().getHscPercentage() != null)
							userEducationBean.setHscPercentage(user.getUserEducation().getHscPercentage().toString());
						if(user.getUserEducation().getSscBoardName() != null)
							userEducationBean.setSscBoardName(user.getUserEducation().getSscBoardName());
						if(user.getUserEducation().getSscPercentage() != null)
							userEducationBean.setSscPercentage(user.getUserEducation().getSscPercentage().toString());
						if(user.getUserEducation().getUniversityName() != null)
							userEducationBean.setUniversityName(user.getUserEducation().getUniversityName());
						userBean.setUserEducationBean(userEducationBean);
					}

					List<UserEmployment> userEmployment = user.getUserEmployment();
					List<UserEmploymentBean> userEmploymentList = new ArrayList<>();

					if(userEmployment != null ) {
						for(UserEmployment uEmp : userEmployment) {
							UserEmploymentBean userEmploymentBean = new UserEmploymentBean();
							if(uEmp.getCompanyLocation() != null)
								userEmploymentBean.setCompanyLocation(uEmp.getCompanyLocation());
							if(uEmp.getCompanyName() != null)
								userEmploymentBean.setCompanyName(uEmp.getCompanyName());
							if(uEmp.getEndDate() != null)
								userEmploymentBean.setEndDate(uEmp.getEndDate().toString());
							if(uEmp.getStartDate() != null)
								userEmploymentBean.setStartDate(uEmp.getStartDate().toString());
							if(uEmp.getTechnology() != null)
								userEmploymentBean.setTechnology(uEmp.getTechnology());
							userEmploymentList.add(userEmploymentBean);
						}
						userBean.setUserEmploymentBean(userEmploymentList);
					}
					userList.add(userBean);
				}
			}
			LOGGER.trace("USER_DETAILS_LIST:: "+userList);
			if(!userDetailsList.isEmpty()) {
				dashboardResponse.setStatusCode(CommonConstants.SUCCESS);
				dashboardResponse.setResponseData(USER_DETAILS, userList);
			} else
				errorMsg = "No Records found for requested input.";

		} catch (Exception e) {
			errorMsg = "Following exception occur while fetching User Details.";
			LOGGER.error(errorMsg + "\n\r : "+ e.getStackTrace());
			e.printStackTrace();
		}
		if(errorMsg != null){
			dashboardResponse.setStatusCode(CommonConstants.FAIL);
			dashboardResponse.setErrorMsg(errorMsg);
		}
		returnValue = MAPPER.writeValueAsString(dashboardResponse);
		LOGGER.trace("Exiting getAllUsers() from UserServiceImpl with return:: returnValue: "+returnValue);
		return returnValue;
	}

	@Override
	public String searchByEmail(String email) throws Exception {
		LOGGER.trace("Starting searchByEmail() from UserServiceImpl");
		String returnValue = null;
		String errorMsg = null;
		DashboardResponse dashboardResponse = new DashboardResponse();
		try {
			List<User> userDetailsList = this.userRepo.findByUserDetailsEmail(email);
			List<UserBean> userList = new ArrayList<>();

			if(!(userDetailsList.isEmpty())) {

				for(User user :userDetailsList) {
					UserBean userBean = new UserBean();

					if(user.getUsername() != null)
						userBean.setUserName(user.getUsername());
					if(user.getPassword() != null)
						userBean.setUserPassword(user.getPassword());
					if(user.isActive())
						userBean.setActive(Boolean.toString(user.isActive()));

					UserDetailsBean userDetailsBean = new UserDetailsBean();
					if(user.getUserDetails().getFirstName() != null)
						userDetailsBean.setFirstName(user.getUserDetails().getFirstName());
					if(user.getUserDetails().getLastName() != null)
						userDetailsBean.setLastName(user.getUserDetails().getLastName());
					if(user.getUserDetails().getEmail() != null)
						userDetailsBean.setEmail(user.getUserDetails().getEmail());
					if(user.getUserDetails().getGender() != null)
						userDetailsBean.setGender(user.getUserDetails().getGender());
					userBean.setUserDetails(userDetailsBean);

					UserEducationBean userEducationBean = new UserEducationBean();

					if(user.getUserEducation() != null) {
						if(user.getUserEducation().getCgpa() != null)
							userEducationBean.setCgpa((user.getUserEducation().getCgpa()).toString());
						if(user.getUserEducation().getHscBoardName() != null)
							userEducationBean.setHscBoardName(user.getUserEducation().getHscBoardName());
						if(user.getUserEducation().getHscPercentage() != null)
							userEducationBean.setHscPercentage(user.getUserEducation().getHscPercentage().toString());
						if(user.getUserEducation().getSscBoardName() != null)
							userEducationBean.setSscBoardName(user.getUserEducation().getSscBoardName());
						if(user.getUserEducation().getSscPercentage() != null)
							userEducationBean.setSscPercentage(user.getUserEducation().getSscPercentage().toString());
						if(user.getUserEducation().getUniversityName() != null)
							userEducationBean.setUniversityName(user.getUserEducation().getUniversityName());
					}
					userBean.setUserEducationBean(userEducationBean);

					List<UserEmployment> userEmployment = user.getUserEmployment();

					List<UserEmploymentBean> userEmploymentList = new ArrayList<>();

					if(userEmployment != null ) {
						for(UserEmployment uEmp : userEmployment) {
							UserEmploymentBean userEmploymentBean = new UserEmploymentBean();
							if(uEmp.getCompanyLocation() != null)
								userEmploymentBean.setCompanyLocation(uEmp.getCompanyLocation());
							if(uEmp.getCompanyName() != null)
								userEmploymentBean.setCompanyName(uEmp.getCompanyName());
							if(uEmp.getEndDate() != null)
								userEmploymentBean.setEndDate(uEmp.getEndDate().toString());
							if(uEmp.getStartDate() != null)
								userEmploymentBean.setStartDate(uEmp.getStartDate().toString());
							if(uEmp.getTechnology() != null)
								userEmploymentBean.setTechnology(uEmp.getTechnology());
							userEmploymentList.add(userEmploymentBean);
						}
						userBean.setUserEmploymentBean(userEmploymentList);
					}
					userList.add(userBean);
				}
				if(!userDetailsList.isEmpty()) {
					dashboardResponse.setStatusCode(CommonConstants.SUCCESS);
					dashboardResponse.setResponseData(USER_DETAILS, userList);
				} 
			} else {
				errorMsg = "No Records found for requested input.";
			}

		} catch (Exception e) {
			errorMsg = "Following exception occur while fetching User Details.";
			LOGGER.error(errorMsg + "\n\r : "+ e.getStackTrace());
			e.printStackTrace();
		}
		if(errorMsg != null){
			dashboardResponse.setStatusCode(CommonConstants.FAIL);
			dashboardResponse.setErrorMsg(errorMsg);
		}
		returnValue = MAPPER.writeValueAsString(dashboardResponse);
		LOGGER.trace("Exiting getAllActiveUsers() from UserServiceImpl with return:: returnValue: "+returnValue);
		return returnValue;
	}


	@Override
	public String editUser(String dashboardRequest, String uname) throws Exception {
		LOGGER.trace("Starting editUser() from UserServiceImpl with arguments:: dashboardRequest: "+dashboardRequest);
		String returnValue = null;
		String errorMsg = null;
		DashboardResponse dashboardResponse = new DashboardResponse();
		try {
			Optional<User> u = userRepo.findByUsername(uname);
			//Optional<User> u = this.userRepo.findById(id);

			if(u.isPresent()) {

				UserBean request = MAPPER.readValue(dashboardRequest, UserBean.class);
				System.out.println(request);
				User user = u.get();
				user.setActive(true);
				user.setUsername(request.getUserName() != null ? request.getUserName() : user.getUsername());
				user.setPassword(request.getUserPassword() != null ? request.getUserPassword() : user.getPassword());

				UserPersonal userDetails = user.getUserDetails();

				if(request.getUserDetails() != null) {
					userDetails.setFirstName(request.getUserDetails().getFirstName() != null ? request.getUserDetails().getFirstName() : user.getUserDetails().getFirstName());
					userDetails.setLastName(request.getUserDetails().getLastName() != null ? request.getUserDetails().getLastName() : user.getUserDetails().getLastName());
					//userDetails.setAddress(request.getUserDetails().getAddress());
					userDetails.setEmail(request.getUserDetails().getEmail() != null ? request.getUserDetails().getEmail() : user.getUserDetails().getEmail());
					userDetails.setGender(request.getUserDetails().getGender() != null ? request.getUserDetails().getGender() : user.getUserDetails().getGender());
					user.setUserDetails(userDetails);
				} 
				UserEducation userEducation = user.getUserEducation();
				if(request.getUserEducationBean() != null) {
					userEducation.setCgpa(request.getUserEducationBean().getCgpa() != null ? Float.valueOf(request.getUserEducationBean().getCgpa()) : user.getUserEducation().getCgpa());
					userEducation.setHscBoardName(request.getUserEducationBean().getHscBoardName() != null ? request.getUserEducationBean().getHscBoardName() : user.getUserEducation().getHscBoardName());
					userEducation.setHscPercentage(request.getUserEducationBean().getHscPercentage() != null ? Float.valueOf(request.getUserEducationBean().getHscPercentage()) : user.getUserEducation().getHscPercentage());
					userEducation.setSscBoardName(request.getUserEducationBean().getSscBoardName() != null ? request.getUserEducationBean().getSscBoardName() : user.getUserEducation().getSscBoardName());
					userEducation.setSscPercentage(request.getUserEducationBean().getSscPercentage() != null ? Float.valueOf(request.getUserEducationBean().getSscPercentage()) : user.getUserEducation().getSscPercentage());
					userEducation.setUniversityName(request.getUserEducationBean().getUniversityName() != null ? request.getUserEducationBean().getUniversityName() : user.getUserEducation().getUniversityName());
					user.setUserEducation(userEducation);
				}

				List<UserEmployment> userEmpmt = user.getUserEmployment();
				List<UserEmploymentBean> uEmpBean = request.getUserEmploymentBean();
				System.out.println(uEmpBean);

				if(uEmpBean != null ) {
					List<UserEmployment> empList = new ArrayList<>();
					for(UserEmploymentBean empBean : uEmpBean) {
						if(!(userEmpmt.isEmpty())) {
							for(UserEmployment userEmp : userEmpmt) {
								userEmp.setCompanyLocation(empBean.getCompanyLocation() != null ? empBean.getCompanyLocation() : userEmp.getCompanyLocation());
								userEmp.setCompanyName(empBean.getCompanyName() != null ? empBean.getCompanyName() : userEmp.getCompanyName());

								if(empBean.getEndDate() != null) {
									Date d = new SimpleDateFormat("yyyy-dd-mm").parse(empBean.getEndDate());
									userEmp.setEndDate(d);
								} 
								if(empBean.getStartDate() != null) {
									Date d = new SimpleDateFormat("yyyy-dd-mm").parse(empBean.getStartDate());
									userEmp.setStartDate(d);
								} 

								userEmp.setTechnology(empBean.getTechnology() != null ? empBean.getTechnology() : userEmp.getTechnology());
								empList.add(userEmp);
							}
						} else {
							for(UserEmploymentBean empBean1 : uEmpBean) {
								UserEmployment userEmp = new UserEmployment();
								userEmp.setUser(user);
								userEmp.setCompanyLocation(empBean1.getCompanyLocation() != null ? empBean1.getCompanyLocation() : null);
								userEmp.setCompanyName(empBean1.getCompanyName() != null ? empBean1.getCompanyName() : null);

								if(empBean1.getEndDate() != null) {
									Date d = new SimpleDateFormat("yyyy-dd-mm").parse(empBean1.getEndDate());
									userEmp.setEndDate(d);
								} 
								if(empBean1.getStartDate() != null) {
									Date d = new SimpleDateFormat("yyyy-dd-mm").parse(empBean1.getStartDate());
									userEmp.setStartDate(d);
								} 

								userEmp.setTechnology(empBean1.getTechnology() != null ? empBean1.getTechnology() : null);
								empList.add(userEmp);
							}
							
						}
					}
					user.setUserEmployment(empList);
				}
				User user1 = this.userRepo.save(user);

				if(user1 != null) {
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					System.out.println(user1);
					dashboardResponse.setStatusCode(CommonConstants.SUCCESS);
					dashboardResponse.setResponseData(USER_DETAILS," Changes saved" );
				}
			} else {
				errorMsg = "No Records found for requested input.";
			}
		} catch (Exception e) {
			errorMsg = "Following exception occur while fetching User Details.";
			LOGGER.error(errorMsg + "\n\r : "+e.getStackTrace());
			e.printStackTrace();
		}
		if(errorMsg != null){
			dashboardResponse.setStatusCode(CommonConstants.FAIL);
			dashboardResponse.setErrorMsg(errorMsg);
		}
		returnValue = MAPPER.writeValueAsString(dashboardResponse);
		LOGGER.trace("Exiting editUser() from UserServiceImpl with return:: returnValue: "+returnValue);
		return returnValue;
	}

	@Override
	public String sortBy(String anything) throws Exception {
		LOGGER.trace("Starting sortBy() from UserServiceImpl");
		String returnValue = null;
		String errorMsg = null;
		DashboardResponse dashboardResponse = new DashboardResponse();
		try {
			List<User> userDetailsList = this.userRepo.findAll(Sort.by(anything).ascending());
			List<UserBean> userList = new ArrayList<>();

			if(!(userDetailsList.isEmpty())) {

				for(User user :userDetailsList) {
					UserBean userBean = new UserBean();

					if(user.getUsername() != null)
						userBean.setUserName(user.getUsername());
					if(user.getPassword() != null)
						userBean.setUserPassword(user.getPassword());
					if(user.isActive())
						userBean.setActive(Boolean.toString(user.isActive()));

					if(user.getUserDetails() != null) {
						UserDetailsBean userDetailsBean = new UserDetailsBean();
						if(user.getUserDetails().getFirstName() != null)
							userDetailsBean.setFirstName(user.getUserDetails().getFirstName());
						if(user.getUserDetails().getLastName() != null)
							userDetailsBean.setLastName(user.getUserDetails().getLastName());
						if(user.getUserDetails().getEmail() != null)
							userDetailsBean.setEmail(user.getUserDetails().getEmail());
						if(user.getUserDetails().getGender() != null)
							userDetailsBean.setGender(user.getUserDetails().getGender());
						userBean.setUserDetails(userDetailsBean);
					}


					if(user.getUserEducation() != null) {
						UserEducationBean userEducationBean = new UserEducationBean();
						if(user.getUserEducation().getCgpa() != null)
							userEducationBean.setCgpa((user.getUserEducation().getCgpa()).toString());
						if(user.getUserEducation().getHscBoardName() != null)
							userEducationBean.setHscBoardName(user.getUserEducation().getHscBoardName());
						if(user.getUserEducation().getHscPercentage() != null)
							userEducationBean.setHscPercentage(user.getUserEducation().getHscPercentage().toString());
						if(user.getUserEducation().getSscBoardName() != null)
							userEducationBean.setSscBoardName(user.getUserEducation().getSscBoardName());
						if(user.getUserEducation().getSscPercentage() != null)
							userEducationBean.setSscPercentage(user.getUserEducation().getSscPercentage().toString());
						if(user.getUserEducation().getUniversityName() != null)
							userEducationBean.setUniversityName(user.getUserEducation().getUniversityName());
						userBean.setUserEducationBean(userEducationBean);
					}

					List<UserEmployment> userEmployment = user.getUserEmployment();
					List<UserEmploymentBean> userEmploymentList = new ArrayList<>();

					if(userEmployment != null ) {
						for(UserEmployment uEmp : userEmployment) {
							UserEmploymentBean userEmploymentBean = new UserEmploymentBean();
							if(uEmp.getCompanyLocation() != null)
								userEmploymentBean.setCompanyLocation(uEmp.getCompanyLocation());
							if(uEmp.getCompanyName() != null)
								userEmploymentBean.setCompanyName(uEmp.getCompanyName());
							if(uEmp.getEndDate() != null)
								userEmploymentBean.setEndDate(uEmp.getEndDate().toString());
							if(uEmp.getStartDate() != null)
								userEmploymentBean.setStartDate(uEmp.getStartDate().toString());
							if(uEmp.getTechnology() != null)
								userEmploymentBean.setTechnology(uEmp.getTechnology());
							userEmploymentList.add(userEmploymentBean);
						}
						userBean.setUserEmploymentBean(userEmploymentList);
					}
					userList.add(userBean);
				}
			}
			LOGGER.trace("USER_DETAILS_LIST:: "+userList);
			if(!userDetailsList.isEmpty()) {
				dashboardResponse.setStatusCode(CommonConstants.SUCCESS);
				dashboardResponse.setResponseData(USER_DETAILS, userList);
			} else
				errorMsg = "No Records found for requested input.";

		} catch (Exception e) {
			errorMsg = "Following exception occur while fetching User Details.";
			LOGGER.error(errorMsg + "\n\r : "+ e.getStackTrace());
			e.printStackTrace();
		}
		if(errorMsg != null){
			dashboardResponse.setStatusCode(CommonConstants.FAIL);
			dashboardResponse.setErrorMsg(errorMsg);
		}
		returnValue = MAPPER.writeValueAsString(dashboardResponse);
		LOGGER.trace("Exiting sortBy() from UserServiceImpl with return:: returnValue: "+returnValue);
		return returnValue;
	}

}
