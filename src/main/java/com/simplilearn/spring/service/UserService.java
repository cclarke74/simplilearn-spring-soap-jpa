package com.simplilearn.spring.service;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplilearn.soap.User;
import com.simplilearn.spring.repository.UserRepository;

@Service
public class UserService {

	static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	UserRepository userRepository;
	
	/*
	public List<User> list(){
		
		/*
		 * Add some Business Logic to process the list.
		 */
		
		//return this.userRepository.findAll();
	//}

	/*
	public void createUser(User user) {
		this.validateUser(user);
		
		user.setStatus("A");
		this.userRepository.save(user);
	}*/
	
	public User findUser(String username) {
		return this.userRepository.findByUsernameIgnoreCase(username)
						.map(UserService::mapUser)
						.orElse(null);
	}
	/*
	public void updateUser(User user) {
		this.validateUser(user);
		
		this.userRepository.findById(user.getIdUser())
								.ifPresent(u -> {
									u.setFirstName(user.getFirstName());
									u.setLastName(user.getLastName());
									u.setUsername(user.getUsername());
									u.setBirth(user.getBirth());
									
									this.userRepository.save(u);
								});
				
	}*/
	
	public void deleteUser(int idUser) {
		this.userRepository.deleteById(idUser);
	}
	
	static User mapUser(com.simplilearn.spring.jpa.User u) {
		
		logger.debug("Mapping User: {}", u);
		
		User user = new User();
		
		user.setIdUser(u.getIdUser());
		user.setFirstName(u.getFirstName());
		user.setLastName(u.getLastName());
		user.setUsername(u.getUsername());
		
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(u.getBirth());
		
		try {
			user.setBirth(DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		user.setStatus(u.getStatus());
		
		return user;
		
	}
	
	private void validateUser(User user) {
		if (user.getFirstName().isEmpty() || 
			user.getLastName().isEmpty()  ||
			user.getUsername().isEmpty()) {
			throw new RuntimeException("Invalid User Data: " +user);
			
		}
	}
}
