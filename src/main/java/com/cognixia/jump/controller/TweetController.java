package com.cognixia.jump.controller;

import java.security.Principal;
import java.util.ArrayList;
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
import com.cognixia.jump.model.Comment;
import com.cognixia.jump.model.Like;
import com.cognixia.jump.model.Tweet;
import com.cognixia.jump.repository.CommentRepository;
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
	@Autowired
	CommentRepository commRepo;
	
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
		for (Tweet tw: found) {
			tw.setComments(commRepo.findByTweet(tw));
			tw.setLikes(likeRepo.findByTweet(tw));
		}
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
		for (Tweet tw: found) {
			tw.setComments(commRepo.findByTweet(tw));
			tw.setLikes(likeRepo.findByTweet(tw));
		}
		return ResponseEntity.status(200)
				.body(found);
	}
	@GetMapping("/tweet/all")
	public ResponseEntity<?> allTweets(){
		List<Tweet> found = tweetRepo.findAll();
		Collections.reverse(found);
		for (Tweet tw: found) {
			tw.setComments(commRepo.findByTweet(tw));
			tw.setLikes(likeRepo.findByTweet(tw));
		}
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
		tweet.setLikes(new ArrayList<Like>());
		tweet.setComments(new ArrayList<Comment>());
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
		if (tw.get().getUser().getId() != user.get().getId()) {
			return ResponseEntity.status(404).body("Delete your own Tweet.");
		}
		for(Comment comm : commRepo.findByTweet(tw.get())){
			commRepo.deleteById(comm.getId());
		};
		for(Like like: likeRepo.findByTweet(tw.get())){
			likeRepo.deleteById(like.getId());
		};
		tweetRepo.deleteById(tw.get().getId());
		
		return ResponseEntity.status(200).body(tw.get());
	}
	
	@PutMapping("/tweet/like/{id}")
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
		Optional<Like> like = likeRepo.findByUserAndTweet(user.get(), tw.get());
		if(like.isEmpty()) {
			Like created = new Like(-1, tw.get(), user.get());
			created.setId(null);
			likeRepo.save(created);
			return ResponseEntity.status(201).body(created);
		}
		else {
			likeRepo.delete(like.get());
			return ResponseEntity.status(200).body(like.get());
		}
	}
	
	@PostMapping("/comment/{tw_id}")
	public ResponseEntity<?> newComment(@PathVariable Integer tw_id, 
			@Valid @RequestBody Comment comment, 
			Authentication authentication, Principal principal){
		Optional<User> user = userRepo.findByUsername(authentication.getName());
		
		if(user.isEmpty()) {
			return ResponseEntity.status(404).body("User Not Found.");
		}
		Optional<Tweet> tw = tweetRepo.findById(tw_id);
		if(tw.isEmpty()) {
			return ResponseEntity.status(404).body("Tweet Not Found.");
		}
		User u = user.get();
		Tweet t = tw.get(); 
		comment.setId(null);
		comment.setUser(u);
		comment.setTweet(t);
		Comment created = commRepo.save(comment);
		t.setComments(commRepo.findByTweet(t));
		tweetRepo.save(t);
		u.setComments(commRepo.findByUser(u));
		userRepo.save(u);
		return ResponseEntity.status(201).body(created);
	}
	
	@DeleteMapping("/comment")
	public ResponseEntity<?> destroyComment(@RequestBody Comment comment, Authentication authentication, 
			Principal principal){
		Optional<Comment> comm = commRepo.findById(comment.getId());
		if(comm.isEmpty()) {
			return ResponseEntity.status(404).body("Comment Not Found.");
		}
		Optional<User> user = userRepo.findByUsername(authentication.getName());
		if(user.isEmpty()) {
			return ResponseEntity.status(404).body("User Not Found.");
		}
		if (comm.get().getUser().getId() != user.get().getId()) {
			return ResponseEntity.status(404).body("Delete your own Comment.");
		}
		commRepo.deleteById(comm.get().getId());
		return ResponseEntity.status(200).body(comm.get());
	}
	@GetMapping("/comment/{username}")
	public ResponseEntity<?> userComments(@PathVariable String username){
		Optional<User> user = userRepo.findByUsername(username);
		if(user.isEmpty()) {
			return ResponseEntity.status(404).body("User Not Found.");
		}
		List<Comment> comments = commRepo.findByUser(user.get());
		Collections.reverse(comments);
		return ResponseEntity.status(200)
				.body(comments);
	}
	@PutMapping("/comment")
	public ResponseEntity<?> updateComment(@RequestBody Comment comment, Authentication authentication, 
			Principal principal){
		Optional<Comment> comm = commRepo.findById(comment.getId());
		if(comm.isEmpty()) {
			return ResponseEntity.status(404).body("Comment Not Found.");
		}
		Optional<User> user = userRepo.findByUsername(authentication.getName());
		if(user.isEmpty()) {
			return ResponseEntity.status(404).body("User Not Found.");
		}
		if (comm.get().getUser().getId() != user.get().getId()) {
			return ResponseEntity.status(404).body("Change your own Comment.");
		}
		Comment updated = comm.get();
		updated.setText(comment.getText());
		updated = commRepo.save(comment);
		return ResponseEntity.status(200).body(updated);
	}
}
