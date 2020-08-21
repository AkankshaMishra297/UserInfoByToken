package com.akm.neo.jwtauthentication.bean;

import java.io.Serializable;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.OptBoolean;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserEmploymentBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("company_name")
	private String companyName;

	@JsonProperty("start_date")
    @JsonFormat(pattern = "yyyy-MM-dd", lenient = OptBoolean.FALSE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String startDate;
	
	
	@JsonProperty("end_date")
	private String endDate;

	@JsonProperty("technology")
	private String technology;

	@JsonProperty("company-location")
	private String companyLocation;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getCompanyLocation() {
		return companyLocation;
	}

	public void setCompanyLocation(String companyLocation) {
		this.companyLocation = companyLocation;
	}

	@Override
	public String toString() {
		return "UserEmploymentBean [companyName=" + companyName + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", technology=" + technology + ", companyLocation=" + companyLocation + "]";
	}
	
	
	
	
}
