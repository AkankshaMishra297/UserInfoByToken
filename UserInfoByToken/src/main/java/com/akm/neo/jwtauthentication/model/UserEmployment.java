package com.akm.neo.jwtauthentication.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;


@Entity
public class UserEmployment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String companyName;

	private Date startDate;

	private Date endDate;

	private String technology;

	private String companyLocation;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY
			)
	@JoinColumn(name="user_id", nullable=false)
	private User user;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar createdAt;

	public UserEmployment() {
		super();
	}

	public UserEmployment(int id, String companyName, Date startDate, Date endDate, String technology,
			String companyLocation, Calendar createdAt) {
		super();
		this.id = id;
		this.companyName = companyName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.technology = technology;
		this.companyLocation = companyLocation;
		this.createdAt = createdAt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
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

//	public User getUser() {
//		return user;
//	}
//
	public void setUser(User user) {
		this.user = user;
	}

	public Calendar getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "UserEmployment [companyName=" + companyName + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", technology=" + technology + ", companyLocation=" + companyLocation + ", createdAt=" + createdAt
				+ "]";
	}

	

}
