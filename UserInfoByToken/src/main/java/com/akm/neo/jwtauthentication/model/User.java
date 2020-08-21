package com.akm.neo.jwtauthentication.model;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;


@Entity
@Table(name = "users", uniqueConstraints = {
		@UniqueConstraint(columnNames = {
				"username"
		})
		
})
public class User{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//    @NotBlank
	//    @Size(min=3, max = 50)
	//    private String name;

	@NotBlank
	@Size(min=3, max = 50)
	private String username;

	//    @NaturalId
	//    @NotBlank
	//    @Size(max = 50)
	//    @Email
	//    private String email;

	@NotBlank
	@Size(min=6, max = 100)
	private String password;

	private boolean active;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar createdAt;

	@OneToOne(mappedBy="user", cascade = CascadeType.ALL)
	private UserPersonal userDetails;

	@OneToOne(mappedBy="user", cascade = CascadeType.ALL)
	private UserEducation userEducation;

	@OneToMany(mappedBy="user", cascade = CascadeType.ALL)
	private List<UserEmployment> userEmployment;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", 
	joinColumns = @JoinColumn(name = "user_id"), 
	inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public User() {}

	//    public User(String name, String username, String email, String password) {
	//        this.name = name;
	//        this.username = username;
	//        this.email = email;
	//        this.password = password;
	//    }




	public Long getId() {
		return id;
	}

	public User(String username, String password,
			boolean active, Calendar createdAt, UserPersonal userDetails,
			UserEducation userEducation,
			List<UserEmployment> userEmployment) {
		super();
		this.username = username;
		this.password = password;
		this.active = active;
		this.createdAt = createdAt;
		this.userDetails = userDetails;
		this.userEducation = userEducation;
		this.userEmployment = userEmployment;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}



	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Calendar getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}

	public UserPersonal getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserPersonal userDetails) {
		this.userDetails = userDetails;
	}

	public UserEducation getUserEducation() {
		return userEducation;
	}

	public void setUserEducation(UserEducation userEducation) {
		this.userEducation = userEducation;
	}

	public List<UserEmployment> getUserEmployment() {
		return userEmployment;
	}

	public void setUserEmployment(List<UserEmployment> userEmployment) {
		this.userEmployment = userEmployment;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", active=" + active
				+ ", createdAt=" + createdAt + ", userDetails=" + userDetails + ", userEducation=" + userEducation
				+ ", userEmployment=" + userEmployment + ", roles=" + roles + "]";
	}
	
	
	
	
}