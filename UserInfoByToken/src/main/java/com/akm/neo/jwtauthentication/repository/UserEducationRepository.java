package com.akm.neo.jwtauthentication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.akm.neo.jwtauthentication.model.User;
import com.akm.neo.jwtauthentication.model.UserEducation;

@Repository
public interface UserEducationRepository extends JpaRepository<UserEducation, Long> {
    
    List<UserEducation> findAllByUser(User user);


}