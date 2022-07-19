package com.cognixia.jump.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.model.User;
import com.cognixia.jump.model.Like;
import com.cognixia.jump.model.Tweet;
import com.cognixia.jump.repository.LikeRepository;
import com.cognixia.jump.repository.TweetRepository;
import com.cognixia.jump.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class TweetController {

	@Autowired
	UserRepository userRepo;
	@Autowired
	LikeRepository likeRepo;
	@Autowired
	TweetRepository tweetRepo;
	
	@GetMapping("/tweet/my")
	public ResponseEntity<?> thisUsersTweets(Authentication authentication, 
			Principal principal){
		Optional<User> user = userRepo.findByUsername(authentication.getName());
		
		if(user.isEmpty()) {
			return ResponseEntity.status(404).body("User Not Found.");
		}
		Integer id = user.get().getId();
		List<Tweet> found = tweetRepo.haveSameUser(id);
		Collections.reverse(found);
		return ResponseEntity.status(200)
				.body(found);
	}
	@GetMapping("/tweet/{username}")
	public ResponseEntity<?> userTweets(@PathVariable String username){
		Optional<User> user = userRepo.findByUsername(username);
		
		if(user.isEmpty()) {
			return ResponseEntity.status(404).body("User Not Found.");
		}
		Integer id = user.get().getId();
		List<Tweet> found = tweetRepo.haveSameUser(id);
		Collections.reverse(found);
		return ResponseEntity.status(200)
				.body(found);
	}
	@GetMapping("/tweet/all")
	public ResponseEntity<?> allTweets(@PathVariable String username){
		List<Tweet> found = tweetRepo.findAll();
		Collections.reverse(found);
		return ResponseEntity.status(200)
				.body(found);
	}
	@PostMapping("/tweet")
	public ResponseEntity<?> newTweet(@Valid @RequestBody Tweet tweet, Authentication authentication, 
			Principal principal){
		Optional<User> user = userRepo.findByUsername(authentication.getName());
		
		if(user.isEmpty()) {
			return ResponseEntity.status(404).body("User Not Found.");
		}
		tweet.setId(null);
		tweet.setUser(user.get());
		Tweet created = tweetRepo.save(tweet);
		return ResponseEntity.status(201).body(created);
	}
	@DeleteMapping("/tweet")
	public ResponseEntity<?> destroyTweet(@RequestBody Tweet tweet, Authentication authentication, 
			Principal principal){
		Optional<Tweet> tw = tweetRepo.findById(tweet.getId());
		if(tw.isEmpty()) {
			return ResponseEntity.status(404).body("Tweet Not Found.");
		}
		Optional<User> user = userRepo.findByUsername(authentication.getName());
		if(user.isEmpty()) {
			return ResponseEntity.status(404).body("User Not Found.");
		}
		if (tw.get().getUser().getId() == user.get().getId()) {
			return ResponseEntity.status(404).body("Delete your own Tweet.");
		}
		tweetRepo.deleteById(tw.get().getId());
		return ResponseEntity.status(200).body(tw.get());
	}
	
	@PutMapping("/tweet/{id}/like")
	public ResponseEntity<?> likeTweet(@PathVariable Integer id , Authentication authentication, 
			Principal principal) throws Exception {
		Optional<Tweet> tw = tweetRepo.findById(id);
		if(tw.isEmpty()) {
			return ResponseEntity.status(404).body("Tweet Not Found.");
		}
		Optional<User> user = userRepo.findByUsername(authentication.getName());
		if(user.isEmpty()) {
			return ResponseEntity.status(404).body("User Not Found.");
		}
		Optional<Like> like = likeRepo.findByData(tw.get().getId(), user.get().getId());
		if(like.isEmpty()) {
			likeRepo.save(like.get());
			return ResponseEntity.status(201).body(like.get());
		}
		likeRepo.delete(like.get());
		return ResponseEntity.status(200).body(like.get());
	}
}
