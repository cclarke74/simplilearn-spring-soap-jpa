package com.simplilearn.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simplilearn.spring.jpa.User;

public interface UserRepository extends JpaRepository<User,Integer>{

    public Optional<User> findByUsernameIgnoreCase(String username);
}