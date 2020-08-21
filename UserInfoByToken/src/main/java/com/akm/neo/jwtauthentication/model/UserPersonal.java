package com.akm.neo.jwtauthentication.model;

import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;


@Entity
@Table(name = "userDetails", uniqueConstraints = {
		
		@UniqueConstraint(columnNames = {
				"email"
		})
})
public class UserPersonal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String firstName;

	private String lastName;

	private String address;

	private String email;

	private String gender;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY
			)
	private User user;

	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Calendar createdAt;

	public UserPersonal() {
		super();
	}

	public UserPersonal(int id, String firstName, String lastName, String address, String email, String gender,
			Calendar createdAt) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.email = email;
		this.gender = gender;
		this.createdAt = createdAt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
