package com.akm.neo.jwtauthentication.security.services;

import com.akm.neo.jwtauthentication.model.User;
import com.akm.neo.jwtauthentication.model.UserEducation;
import com.akm.neo.jwtauthentication.model.UserEmployment;
import com.akm.neo.jwtauthentication.model.UserPersonal;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserPrinciple implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Long id;

   // private String name;

    private String username;

    //private String email;

    @JsonIgnore
    private String password;
    private boolean active;
    private Calendar createdAt;
    private UserPersonal userPersonal;
    private UserEducation userEducation;
    private List<UserEmployment> userEmployment;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrinciple(Long id, 
			    		String username, String password,boolean active,Calendar createdAt, UserPersonal userPersonal,
			    		UserEducation userEducation, List<UserEmployment> userEmployment,
			    		Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.active = active;
        this.createdAt = createdAt;
        this.userPersonal = userPersonal;
        this.userEducation = userEducation;
        this.userEmployment = userEmployment;
        this.authorities = authorities;
    }

    public static UserPrinciple build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())
        ).collect(Collectors.toList());

        return new UserPrinciple(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUserDetails(),
                user.getUserEducation(),
                user.getUserEmployment(),
                authorities
        );
    }

    public Long getId() {
        return id;
    }

	public boolean isActive() {
		return active;
	}

	public Calendar getCreatedAt() {
		return createdAt;
	}

	public UserPersonal getUserPersonal() {
		return userPersonal;
	}

	public UserEducation getUserEducation() {
		return userEducation;
	}


	public List<UserEmployment> getUserEmployment() {
		return userEmployment;
	}

	@Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        UserPrinciple user = (UserPrinciple) o;
        return Objects.equals(id, user.id);
    }
}