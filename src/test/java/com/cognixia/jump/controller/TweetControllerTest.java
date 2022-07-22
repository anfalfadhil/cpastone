package com.cognixia.jump.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.cognixia.jump.config.JwtAuthenticationEntryPoint;
import com.cognixia.jump.controller.TweetController;
import com.cognixia.jump.filter.JwtRequestFilter;
import com.cognixia.jump.model.Comment;
import com.cognixia.jump.model.Like;
import com.cognixia.jump.model.Tweet;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.CommentRepository;
import com.cognixia.jump.repository.LikeRepository;
import com.cognixia.jump.repository.TweetRepository;
import com.cognixia.jump.repository.UserRepository;
import com.cognixia.jump.service.MyUserDetails;
import com.cognixia.jump.service.MyUserDetailsService;
import com.cognixia.jump.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(value = TweetController.class, includeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtUtil.class) })
public class TweetControllerTest {

	private static final String STARTING_URI = "http://localhost:8080/api";

	@Autowired
	private MockMvc mvc;

	@MockBean
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@MockBean
	private MyUserDetailsService myUserDetailsService;

	@MockBean
	private TweetRepository repo;
	
	@MockBean
	private UserRepository userRepo;

	@MockBean
	private LikeRepository likeRepo;
	
	@MockBean
	private CommentRepository commRepo;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private JwtRequestFilter filter;
	
	@Autowired
	private JwtAuthenticationEntryPoint entryPoint;
	
	@WithMockUser(value = "spring")
	
	@Test
	void testAllTweet() throws Exception {
		String uri = STARTING_URI + "/tweet/all";
		User alice = new User(1, "alice", "alice@mail.com", "pass", 
				User.Role.ROLE_USER, true);
		User bob = new User(2, "bob", "bob@mail.com", "pass", 
				User.Role.ROLE_USER, true);
				
		UserDetails dummy = new MyUserDetails(alice);
		String jwtToken = jwtUtil.generateTokens(dummy);
		RequestBuilder request = MockMvcRequestBuilders.get(uri).header("Authorization", "Bearer " + jwtToken);
		
		List<Tweet> allTweets = new ArrayList<Tweet>();
		List<Like> allLikes = new ArrayList<Like>();
		List<Comment> allComments = new ArrayList<Comment>();
		Tweet tweet1 = new Tweet(1, "Hello world", alice, new ArrayList<Like> (), new ArrayList<Comment> ());
		Tweet tweet2 = new Tweet(2, "Hello earth", bob, new ArrayList<Like> (), new ArrayList<Comment> ());
		allTweets.add(tweet1);
		allTweets.add(tweet2);
		when(repo.findAll()).thenReturn(allTweets);
		
//		when(repo.findAll()).thenReturn(allLikes);
//		
//		when(repo.findAll()).thenReturn(allComments);
		
		// when(myUserDetailsService.loadUserByUsername("alice")).thenReturn(dummy);
		//  when(myUserDetailsService.loadUserByUsername("bob")).thenReturn(dummy);
		
		mvc.perform(request).andDo(print()).andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$[0].id").value(allTweets.get(0).getId()))
			.andExpect(jsonPath("$[0].text").value(allTweets.get(0).getText()))
			.andExpect(jsonPath("$[0].user").value(allTweets.get(0).getUser()))
			.andExpect(jsonPath("$[1].id").value(allTweets.get(1).getId()))
			.andExpect(jsonPath("$[1].text").value(allTweets.get(1).getText()))
			.andExpect(jsonPath("$[1].user").value(allTweets.get(1).getUser()));
		
		verify(repo, times(1)).findAll();
		verifyNoMoreInteractions(repo);
	}
	
	public static String asJsonString(final Object obj) {
		
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new RuntimeException();
		}
		
	}
}
