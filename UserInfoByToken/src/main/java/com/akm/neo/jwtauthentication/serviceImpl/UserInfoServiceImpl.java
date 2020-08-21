package com.akm.neo.jwtauthentication.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akm.neo.jwtauthentication.bean.DashboardResponse;
import com.akm.neo.jwtauthentication.bean.UserEducationBean;
import com.akm.neo.jwtauthentication.common.CommonConstants;
import com.akm.neo.jwtauthentication.model.User;
import com.akm.neo.jwtauthentication.model.UserEducation;
import com.akm.neo.jwtauthentication.repository.RoleRepository;
import com.akm.neo.jwtauthentication.repository.UserEducationRepository;
import com.akm.neo.jwtauthentication.repository.UserRepository;
import com.akm.neo.jwtauthentication.security.SecurityUtils;
import com.akm.neo.jwtauthentication.service.UserInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class UserInfoServiceImpl implements UserInfoService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoServiceImpl.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final String USER_DETAILS = "user_info";
	
	
	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserEducationRepository userEducationRepository;
	
	@Autowired
	RoleRepository roleRepository;

	
	@Override
	public String getUserEducation() throws Exception {
		LOGGER.trace("Starting getUserEducation() from UserInfoServiceImpl");
		String returnValue = null;
		String errorMsg = null;
		DashboardResponse dashboardResponse = new DashboardResponse();
		try {
			
	        User user1 = userInfoService.getLoggedInUserBean();
			
			List<UserEducation> userDetailsList = this.userEducationRepository.findAllByUser(user1);
			UserEducationBean userEducationBean = new UserEducationBean();


			if(!(userDetailsList.isEmpty())) {

				for(UserEducation user :userDetailsList) {

					if(user!= null) {
						if(user.getCgpa() != null)
							userEducationBean.setCgpa((user.getCgpa()).toString());
						if(user.getHscBoardName() != null)
							userEducationBean.setHscBoardName(user.getHscBoardName());
						if(user.getHscPercentage() != null)
							userEducationBean.setHscPercentage(user.getHscPercentage().toString());
						if(user.getSscBoardName() != null)
							userEducationBean.setSscBoardName(user.getSscBoardName());
						if(user.getSscPercentage() != null)
							userEducationBean.setSscPercentage(user.getSscPercentage().toString());
						if(user.getUniversityName() != null)
							userEducationBean.setUniversityName(user.getUniversityName());
					}

					
				}
			}
			LOGGER.trace("USER_EDUCATION:: "+userEducationBean);
			if(!userDetailsList.isEmpty()) {
				dashboardResponse.setStatusCode(CommonConstants.SUCCESS);
				dashboardResponse.setResponseData(USER_DETAILS, userEducationBean);
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
		LOGGER.trace("Exiting getUserEducation() from UserInfoServiceImpl with return:: returnValue: "+returnValue);
		return returnValue;
	}


	@Override
	public User getLoggedInUserBean() throws Exception {
		User user = getUserWithAuthorities()
	            .orElseThrow(() -> new RuntimeException("Account not found"));
		return user;
	}
	public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepo::findOneWithAuthoritiesByUsername);
    }

	

}
