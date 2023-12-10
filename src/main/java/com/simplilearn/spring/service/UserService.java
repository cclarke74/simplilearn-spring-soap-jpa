package com.simplilearn.spring.service;

import java.util.GregorianCalendar;
import java.util.List;

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


    public List<User> listUsers() {

        return this.userRepository.findAll().stream()
                   .map(UserService::mapUser)
                   .toList();
    }


    public User findUser(String username) {
        return this.userRepository.findByUsernameIgnoreCase(username)
                         .map(UserService::mapUser)
                         .orElse(null);
    }


    public User createUser(com.simplilearn.spring.jpa.User user) {

        this.validateUser(user);

        this.userRepository.save(user);

        return UserService.mapUser(user);
    }

    public User updateUser(com.simplilearn.spring.jpa.User user) {

        this.validateUser(user);

        this.userRepository.findById(user.getIdUser())
                              .ifPresent(u -> {
                                  u.setFirstName(user.getFirstName());
                                  u.setLastName(user.getLastName());
                                  u.setUsername(user.getUsername());
                                  u.setBirth(user.getBirth());

                                  this.userRepository.save(u);
                              });

        return UserService.mapUser(user);
    }

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
        } catch (DatatypeConfigurationException ex) {
            ex.printStackTrace();
        }
        user.setStatus(u.getStatus());

        return user;
    }

    private void validateUser(com.simplilearn.spring.jpa.User user) {
        if (user.getFirstName().isEmpty() ||
            user.getLastName().isEmpty() ||
            user.getUsername().isEmpty()) {
            throw new RuntimeException("Invalid User Data: " + user);
        }

    }
}
