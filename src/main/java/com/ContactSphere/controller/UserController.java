package com.ContactSphere.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ContactSphere.dao.ContactRepository;
import com.ContactSphere.dao.UserRepository;
import com.ContactSphere.entities.Contact;
import com.ContactSphere.entities.User;
import com.ContactSphere.helper.Message;

import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@ModelAttribute
	public void addCommonData(Model model,Principal principal)
	{
		String userName=principal.getName();
		//get the user using username
		User user=userRepository.getUserByUsername(userName);
		model.addAttribute("user",user);
	}
	
	@GetMapping("/index")
	public String dashboard()
	{
		return "normal/dashboard";
	}
	
	@GetMapping("/add-contact")
	public String addContact(Model model)
	{
		model.addAttribute("title","ADD CONTACT");
		model.addAttribute("contact",new Contact());
		return "normal/add-contact";
	}
	
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,
			@RequestParam("profileImage") MultipartFile file,
			Principal principal,HttpSession session)
	{
		try {
		String name=principal.getName();
		User user=this.userRepository.getUserByUsername(name);
		
		
		if(file.isEmpty())
		{
			System.out.println("File is empty");
			contact.setImageUrl("contact.png");
		}
		else {
			contact.setImageUrl(file.getOriginalFilename());
			File saveFile=new ClassPathResource("static/img").getFile();
			Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
		    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Image is uploaded");
		}
		contact.setUser(user);
		user.getContacts().add(contact);
		
		this.userRepository.save(user);
		System.out.println("Data ="+contact);
		System.out.println("Added contacts");
		
		//success message
		session.setAttribute("message", new Message("Successfully Added","success"));
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
			//error message
			session.setAttribute("message", new Message("Something went wrong try again","danger"));
		}
		return "/normal/add-contact";
	}
	
	//show contact handler
	//PER PAGE=5[n]
	//CURRENT PAGE=0[page]
	@GetMapping("/showContacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model model,Principal principal)
	{
		model.addAttribute("title","SHOW CONTACTS");
		String userName=principal.getName();
		User user=this.userRepository.getUserByUsername(userName);
		PageRequest pageable=PageRequest.of(page, 5);
		Page<Contact> contacts=this.contactRepository.findContactsByUser(user.getId(),pageable);
		model.addAttribute("contacts",contacts);
		model.addAttribute("currentPage",page);
		model.addAttribute("totalPage",contacts.getTotalPages());
		return "normal/showContacts";
	}
	
	//handler for showing contact details
	@GetMapping("/contact/{cId}")
	public String showContactDetails(@PathVariable("cId") Integer cId,Model model,Principal principal)
	{
		Optional<Contact> contactOptional=this.contactRepository.findById(cId);
		Contact contact=contactOptional.get();
		String userName=principal.getName();
		User user= this.userRepository.getUserByUsername(userName);
		if(user.getId()==contact.getUser().getId())
		model.addAttribute("contact",contact);
		return "normal/contactDetails";
	}
	
	//handler for delete contact
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") int cId,Model model,HttpSession session)
	{
		Optional<Contact> contactOptional=this.contactRepository.findById(cId);
		Contact contact=contactOptional.get();
		this.contactRepository.delete(contact);
		session.setAttribute("message",new Message("Contact Deleted Successfully...","success"));
		return "redirect:/user/showContacts/0";
	}
	
	//open update form handler
	@PostMapping("/updateContact/{cid}")
	public String updateContact(@PathVariable("cid") Integer cid,Model model)
	{
		Contact contact=this.contactRepository.findById(cid).get();
		model.addAttribute("contact",contact);
		return "normal/updateContact";
	}
	
	//handler for update contact
	@PostMapping("/process-update")
	public String updatehandler(@ModelAttribute Contact contact,@RequestParam("profileImage")MultipartFile file
			,Model m,HttpSession session,Principal principal)
	{
		try {
			//image
			//old contact details
			Contact oldContactDetails=this.contactRepository.findById(contact.getcId()).get();
			if(!file.isEmpty())
			{
				//rewrite file
			//delete old photo
				File deleteFile=new ClassPathResource("static/img").getFile();
				File file1=new File(deleteFile,oldContactDetails.getImageUrl());
				file1.delete();
			//update new photo
				File saveFile=new ClassPathResource("static/img").getFile();
				Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			    contact.setImageUrl(file.getOriginalFilename());
				
			}
			else {
				contact.setImageUrl(oldContactDetails.getImageUrl());
			}
			User user=this.userRepository.getUserByUsername(principal.getName());
			contact.setUser(user);
			this.contactRepository.save(contact);
			session.setAttribute("message",new Message("Your contact is updated....","success"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return "redirect:/user/contact/"+contact.getcId();
	}
	
	//handler for profile
	@GetMapping("/profile")
	public String yourProfile()
	{
		return "normal/profile";
	}
	

}
