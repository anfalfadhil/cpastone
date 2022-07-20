package com.cognixia.jump.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String text;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	
	@ManyToOne
	@JoinColumn(name = "tweet_id", referencedColumnName = "id")
	@JsonBackReference
	private Tweet tweet;
	
	
	public Comment() {
		
	}


	public Comment(Integer id, String text, User user, Tweet tweet) {
		super();
		this.id = id;
		this.text = text;
		this.user = user;
		this.tweet = tweet;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Tweet getTweet() {
		return tweet;
	}


	public void setTweet(Tweet tweet) {
		this.tweet = tweet;
	}
	
	
	
	
}
