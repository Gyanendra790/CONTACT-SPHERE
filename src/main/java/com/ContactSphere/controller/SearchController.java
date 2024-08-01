package com.ContactSphere.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ContactSphere.dao.ContactRepository;
import com.ContactSphere.dao.UserRepository;
import com.ContactSphere.entities.Contact;
import com.ContactSphere.entities.User;

@RestController
public class SearchController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;
	
	//search handler
	@GetMapping("search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query,Principal principal)
	{
		User user=this.userRepository.getUserByUsername(principal.getName());
		List<Contact> contacts=this.contactRepository.findByFirstNameContainingAndUser(query, user);
		return ResponseEntity.ok(contacts);
	}

}
