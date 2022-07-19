package com.cognixia.jump.model;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Tweet{
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private String text;
	

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	@JsonIgnore
	@OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
	private List<Like> like;
	
	
	@OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
	private List<Comment> comments;
	
	
	public Tweet() {
		
	}


	public Tweet(Integer id, String text, User user, List<Like> like, List<Comment> comments) {
		super();
		this.id = id;
		this.text = text;
		this.user = user;
		this.like = like;
		this.comments = comments;
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


	public List<Like> getLike() {
		return like;
	}


	public void setLike(List<Like> like) {
		this.like = like;
	}


	public List<Comment> getComments() {
		return comments;
	}


	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	
	
	
}
