package com.ContactSphere.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ContactSphere.dao.UserRepository;
import com.ContactSphere.entities.User;

public class UserDetailServiceImpl implements UserDetailsService {
@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// fetching user from database
		
		User user=userRepository.getUserByUsername(username);
		if(user==null)
			throw new UsernameNotFoundException("Could not found User !!!");
		CustomUserDetails customUserDetails=new CustomUserDetails(user);
		return customUserDetails;
	}

}
