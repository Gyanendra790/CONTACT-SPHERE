package com.ContactSphere.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.ContactSphere.entities.User;
@Component
public interface UserRepository extends JpaRepository<User,Integer> {
	@Query("select u from User u where u.email=:email")
	public User getUserByUsername(@Param("email")String email);

}
