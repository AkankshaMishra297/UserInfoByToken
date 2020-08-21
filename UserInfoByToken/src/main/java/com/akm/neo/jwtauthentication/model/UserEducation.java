package com.akm.neo.jwtauthentication.model;

import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;


@Entity

public class UserEducation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private Float sscPercentage;

	private Float hscPercentage;
	
	private Float cgpa;

	private String sscBoardName;

	private String hscBoardName;

	private String universityName;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY
			)
	private User user;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar createdAt;
	
	public UserEducation() {
		super();
	}

	public UserEducation(int id, Float sscPercentage, Float hscPercentage, Float cgpa, String sscBoardName,
			String hscBoardName, String universityName, Calendar createdAt) {
		super();
		this.id = id;
		this.sscPercentage = sscPercentage;
		this.hscPercentage = hscPercentage;
		this.cgpa = cgpa;
		this.sscBoardName = sscBoardName;
		this.hscBoardName = hscBoardName;
		this.universityName = universityName;
		this.createdAt = createdAt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Float getSscPercentage() {
		return sscPercentage;
	}

	public void setSscPercentage(Float sscPercentage) {
		this.sscPercentage = sscPercentage;
	}

	public Float getHscPercentage() {
		return hscPercentage;
	}

	public void setHscPercentage(Float hscPercentage) {
		this.hscPercentage = hscPercentage;
	}

	public Float getCgpa() {
		return cgpa;
	}

	public void setCgpa(Float cgpa) {
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

	
}
