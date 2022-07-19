package com.cognixia.jump.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Like {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
//	@OneToOne
//	@JoinColumns({
//		@JoinColumn(name= "user_id", referencedColumnName = "user_id"),
//		@JoinColumn(name = "tweet_id", referencedColumnName = "tweet_id")
//	})
//	private Tweet tweet;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	@JsonBackReference
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "tweet", referencedColumnName = "id")
	private Tweet tweet;
	
	public Like() {
		
	}

	public Like(Integer id, Tweet tweet) {
		super();
		this.id = id;
		this.tweet = tweet;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Tweet getTweet() {
		return tweet;
	}

	public void setTweet(Tweet tweet) {
		this.tweet = tweet;
	}

	
	
}