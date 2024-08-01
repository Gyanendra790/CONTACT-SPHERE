package com.ContactSphere.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ContactSphere.dao.UserRepository;
import com.ContactSphere.entities.User;
import com.ContactSphere.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("/")
	public String home(Model m)
	{
	m.addAttribute("title","HOME-Contact Sphere");
		return "home";
		
	}

	@GetMapping("/about")
	public String about(Model m)
	{
	m.addAttribute("title","ABOUT-Contact Sphere");
		return "about";
		
	}

	@GetMapping("/signup/")
	public String signup(Model m)
	{
	m.addAttribute("title","REGISTER-Contact Sphere");
	m.addAttribute("user",new User());
		return "signup";
		
	}
	//handler for registering user
	@PostMapping("/do_register")
	public String success(@Valid @ModelAttribute("user") User user,BindingResult result1,
			@RequestParam(value="agreement",
			defaultValue="false") boolean agreement,Model model,HttpSession session)
	{
		try {
		if(!agreement)
		{
			System.out.println("You have not agreed the terms and conditions");
			throw new Exception("You have not agreed the terms and conditions");
		}
		if(result1.hasErrors())
		{
			System.out.println("ERROR"+result1.toString());
			model.addAttribute("user",user);
			return "signup";
		}
		user.setRole("ROLE_USER");
		user.setEnabled(true);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User result=this.userRepository.save(user);
		System.out.println("Agreement"+agreement);
		System.out.println("User"+user);
		model.addAttribute("user",new User());
		session.setAttribute("message", new Message("Successfully Registered","alert-success"));
		return "signup";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something wrong!!"+e.getMessage(),"alert-danger"));
			return "signup";
		}
		
	}
	
	//custom login page handler
	@GetMapping("/signin")
	public String customLogin(Model model)
	{
		model.addAttribute("title","SIGNIN-Contact Sphere");
		return "login";
		
	}

}
