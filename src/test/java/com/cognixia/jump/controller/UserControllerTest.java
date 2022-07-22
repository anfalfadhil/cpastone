package com.cognixia.jump.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.FilterType;

import com.cognixia.jump.config.JwtAuthenticationEntryPoint;
import com.cognixia.jump.controller.UserController;
import com.cognixia.jump.filter.JwtRequestFilter;
import com.cognixia.jump.repository.UserRepository;
import com.cognixia.jump.service.MyUserDetailsService;
import com.cognixia.jump.util.JwtUtil;

@WebMvcTest(value = UserController.class, includeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtUtil.class) })
public class UserControllerTest {

	private static final String STARTING_URI = "http://localhost:8080/api";

	@Autowired
	private MockMvc mvc;

	@MockBean
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@MockBean
	private MyUserDetailsService myUserDetailsService;

	@MockBean
	private UserRepository repo;

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private JwtRequestFilter filter;
}
