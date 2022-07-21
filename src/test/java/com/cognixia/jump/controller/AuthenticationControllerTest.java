package com.cognixia.jump.controller;

import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.cognixia.jump.config.JwtAuthenticationEntryPoint;
import com.cognixia.jump.filter.JwtRequestFilter;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.UserRepository;
import com.cognixia.jump.service.MyUserDetailsService;
import com.cognixia.jump.util.JwtUtil;


@WebMvcTest(value=AuthenticationController.class, includeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtUtil.class) })
class AuthenticationControllerTest {

	private static final String STARTING_URI = "http://localhost:8080/";
	
	@MockBean
	private AuthenticationController controller;
	
	@MockBean
	private UserController userController;
	

	@MockBean
	private JwtAuthenticationEntryPoint jwtEntryPoint;
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private MyUserDetailsService myUserDetailsService;
	
	@MockBean
	private UserRepository repo;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private JwtRequestFilter filter;
	
	@Test
	void testLogin() throws Exception{
		
		String uri = STARTING_URI + "authenticate";
		
		List<User> users = new ArrayList<User>();
		User alice = new User(1, "alice", "alice@mail.com", "pass", 
				User.Role.ROLE_USER, true);
		
		//ResponseEntity<?>
		users.add(alice);
		
		when(repo.findAll()).thenReturn(users);
		mvc.perform( post(uri)   // perform get request
				.contentType(MediaType.APPLICATION_JSON)
				.content("{'username':'alice','password':'pass','email':'ASDF'}")
				.accept(MediaType.APPLICATION_JSON))
        	.andDo( print() ) // print request sent/response given
			.andExpect( status().isOk() );
		
	}

}