package com.cognixia.jump.controller;

import java.security.Principal;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.UserRepository;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	PasswordEncoder encoder;
	
	@GetMapping("/")
	public ResponseEntity<?> thisUser(Authentication authentication, 
			Principal principal){
		return ResponseEntity.status(200)
				.body(userRepo.findByUsername(authentication.getName()));
	}
	@PostMapping("/new")
	public ResponseEntity<?> CreateUser(@Valid @RequestBody User user){
		user.setId(null);
		user.setPassword(encoder.encode(user.getPassword()));
		User created = userRepo.save(user);
		return ResponseEntity.status(201).body(created);
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateUser(@Valid @RequestBody User user) throws Exception {
		Integer id = user.getId();
		int status = userRepo.findById(id).isEmpty() ? 201 : 200;
		User updated = userRepo.save(user);
		return ResponseEntity.status(status).body(user);
	}
}
