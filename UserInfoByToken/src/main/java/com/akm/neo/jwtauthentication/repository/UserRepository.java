package com.akm.neo.jwtauthentication.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.akm.neo.jwtauthentication.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByUserDetailsEmail(String email);
    @Query(value = "SELECT * FROM users where active=1", nativeQuery = true)
	List<User> findActiveUser();
	
    @Query(value = "SELECT * FROM users where active=1", nativeQuery = true)
    //User findByUsername(String uname);
    
	List<User> findByUserDetailsEmail(String email);
    
    Optional<User> findOneWithAuthoritiesByUsername(String login);
    


}