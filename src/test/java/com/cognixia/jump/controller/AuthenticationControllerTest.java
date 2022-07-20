package com.cognixia.jump.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
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
import com.cognixia.jump.model.User.Role;
import com.cognixia.jump.repository.UserRepository;
import com.cognixia.jump.service.MyUserDetailsService;
import com.cognixia.jump.util.JwtUtil;

@RunWith(SpringRunner.class)
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
	void testLogin() {
		
		String uri = STARTING_URI + "authenticate";
		
		List<User> users = new ArrayList<User>();
		User alice = new User(1, "alice", "alice@mail.com", "$2a$10$XFUUczfOM8MNR/yUUEyraeklyQLLRutR6iLVmSLosCOsn1RQKJbJW", 
				User.Role.ROLE_USER, true);
		
		//ResponseEntity<?>
		users.add(alice);
		
		when(repo.findAll()).thenReturn(users);
		
		mvc.perform( post(uri)   // perform get request
				.contentType(MediaType.APPLICATION_JSON)
				.contentType(toJson)
	 
        		.andDo( print() ) // print request sent/response given
				.andExpect( status().isOk() ); // expect a 200 status code
		 
		InputStream stdin = System.in;
		System.setIn(new ByteArrayInputStream("default\n1234\n".getBytes()));
		
		
		PrintStream originalOut = System.out;
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        
        try {
			User user = new User();
			
			
			assertTrue(user.getUsername().equals("default"), "Login successfull");
			user.exit();
			assertTrue(user.getConn().isClosed(), "Connection successfully closed");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.setIn(stdin);
		System.setOut(originalOut);
	}

}