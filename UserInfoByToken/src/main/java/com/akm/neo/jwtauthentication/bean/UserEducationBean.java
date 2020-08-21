package com.akm.neo.jwtauthentication.bean;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserEducationBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("ssc_percentage")
	@Pattern(regexp ="[0-9.]*$", message = "invalid sscPercentage")
	private String sscPercentage;

	@JsonProperty("hsc_percentage")
	@Pattern(regexp ="[0-9.]*$", message = "invalid hscPercentage")
	private String hscPercentage;
	
	@JsonProperty("cgpa")
	@Pattern(regexp ="[0-9.]*$", message = "invalid cgpa")
	private String cgpa;

	@JsonProperty("ssc_board_name")
	@Pattern(regexp ="([a-zA-Z]){2,16}", message = "invalid sscBoardName")
	private String sscBoardName;

	@JsonProperty("hsc_board_name")
	@Pattern(regexp ="([a-zA-Z]){2,16}", message = "invalid hscBoardName")
	private String hscBoardName;

	@JsonProperty("university_name")
	@Pattern(regexp ="([a-zA-Z]){2,16}", message = "invalid universityName")
	private String universityName;

	public String getSscPercentage() {
		return sscPercentage;
	}

	public void setSscPercentage(String sscPercentage) {
		this.sscPercentage = sscPercentage;
	}

	public String getHscPercentage() {
		return hscPercentage;
	}

	public void setHscPercentage(String hscPercentage) {
		this.hscPercentage = hscPercentage;
	}

	public String getCgpa() {
		return cgpa;
	}

	public void setCgpa(String cgpa) {
		this.cgpa = cgpa;
	}

	public String getSscBoardName() {
		return sscBoardName;
	}

	public void setSscBoardName(String sscBoardName) {
		this.sscBoardName = sscBoardName;
	}

	public String getHscBoardName() {
		return hscBoardName;
	}

	public void setHscBoardName(String hscBoardName) {
		this.hscBoardName = hscBoardName;
	}

	public String getUniversityName() {
		return universityName;
	}

	public void setUniversityName(String universityName) {
		this.universityName = universityName;
	}

	@Override
	public String toString() {
		return "UserEducationBean [sscPercentage=" + sscPercentage + ", hscPercentage=" + hscPercentage + ", cgpa="
				+ cgpa + ", sscBoardName=" + sscBoardName + ", hscBoardName=" + hscBoardName + ", universityName="
				+ universityName + "]";
	}
	
	
	
	
}
