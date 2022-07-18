package com.cognixia.jump.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Tweet {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private String text;
	
	
	private Integer parentTweet;      // Is this a subtweet? if so what is the id of the original tweet? 
	
	@ManyToOne
	@JoinColumn(name = "user_id")    
	private User user;
	
	
	public Tweet() {
		
	}


	public Tweet(Integer id, String text, Integer parentTweet, User user) {
		super();
		this.id = id;
		this.text = text;
		this.parentTweet = parentTweet;
		this.user = user;
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


	public Integer getParentTweet() {
		return parentTweet;
	}


	public void setParentTweet(Integer parentTweet) {
		this.parentTweet = parentTweet;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}
	
	
	
}
